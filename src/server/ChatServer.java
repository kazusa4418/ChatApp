package server;

import event.Command;
import event.MessageEvent;
import util.JLogger;
import util.PropertyReader;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

public class ChatServer {
    private ServerSocket server;
    private ChatRoomList roomList;

    public ChatServer() {
        try {
            server = new ServerSocket(33333);
        }
        catch (IOException err) {
            err.printStackTrace();
            throw new AssertionError(err);
        }

        roomList = new ChatRoomList();
    }

    public void run() {
        while (!server.isClosed()) {
            try {
                // クライアントからのアクセスを待つ
                Socket socket = server.accept();
                Client client = new Client(this, socket, "empty");
                client.start();
            }
            catch (IOException err) {
                err.printStackTrace();
                throw new AssertionError(err);
            }
        }
    }

    void receiveEvent(MessageEvent event) {
        Client creator = event.getCreator();
        String command = event.getCommand();
        String body = event.getBody().trim();
        System.out.println(creator.getName() + command + body);

        switch (command) {
            case "/logout":
                logout(creator, body);
                break;
            case "/send":
                // TODO: ・・・。
                sendMessageToMembersExceptMyself(creator, creator.getName() + " : " + body);
                break;
            case "/make":
                makeRoom(creator, body);
                break;
            case "/join":
                joinRoom(creator, body);
                break;
            case "/leave":
                leaveRoom(creator, body);
                break;
            case "/show-rooms":
                showRoom(creator, body);
                break;
            case "/show-members":
                showMember(creator, body);
                break;
            case "/kick":
                kickMember(creator, body);
                break;
            case "/change-new-admin":
                changeNewAdmin(creator, command, body);
                break;
            case "/help":
                commandHelp(creator, body);
                break;
            default:
                creator.send(command + " is a not system command.");
                break;
        }
    }

    private void sendUsage(Client target, Command command) {
        target.send(Command.usage(command));
    }

    private void logout(Client client, String args) {
        if (!args.matches(Command.LOGOUT.getArgumentRegex())) {
            sendUsage(client, Command.LOGOUT);
            return;
        }
        sendMessageToMembersExceptMyself(client, "## the user '" + client.getName() + "' log outs.");

        // 参加しているルームを抜ける
        roomList.getRoomWith(client).remove(client);
        // クライアントを安全に終了させる
        client.logout();
    }

    // TODO: メソッド名ながすぎませんか
    private void sendMessageToMembersExceptMyself(Client myself, String message) {
        sendMessageToMembersExceptMyself(myself, message, roomList.getRoomWith(myself));
    }

    private void sendMessageToMembersExceptMyself(Client myself, String message, ChatRoom room) {
        // 引数で渡されたルームのメンバーを取得してくる
        Client[] clients = room.getMemberList();

        // 引数で渡された自分（myself）以外のメンバーに引数のメッセージを送る
        for (Client client : clients) {
            if (client == myself)
                continue;

            client.send(message);
        }
    }

    private void makeRoom(Client client, String roomName) {
        if (!roomName.matches(Command.MAKE_ROOM.getArgumentRegex())) {
            sendUsage(client, Command.MAKE_ROOM);
            return;
        }
        // 既に指定された名前を持つルームが存在していた場合
        if (roomList.existRoom(roomName)) {
            client.send("## fatal: the room named '" + roomName + "' already exists.");
            return;
        }

        // 現在入っているルームを抜ける
        ChatRoom leavedRoom = roomList.getRoomWith(client);
        leavedRoom.remove(client);
        // 抜けたことを自分に通知する
        client.send("## you left the room '" + leavedRoom.getName() + "'.");
        // メンバーが抜けたことを抜けたルームのメンバーに通知する
        sendMessageToMembersExceptMyself(client, "## user '" + client.getName() + "' left this room.", leavedRoom);

        // 新しくルームを作り、ルームの管理者としてルームに入る
        roomList.createNewRoom(roomName, client).add(client);
        // 作成したルームに参加したことを自分に通知する
        client.send("## made a new room ! welcome to '" + roomName + "' !");
    }

    private void joinRoom(Client client, String roomName) {
        if (!roomName.matches(Command.JOIN_ROOM.getArgumentRegex())) {
            sendUsage(client, Command.JOIN_ROOM);
            return;
        }
        // 指定したルーム名を持つルームが存在しなかった場合
        if (!roomList.existRoom(roomName)) {
            client.send("## fatal: The room named '" + roomName + "' does not exist.");
            return;
        }
        // 指定したルームに既に参加していた場合
        if (roomList.getRoomWith(client) == roomList.getRoom(roomName)) {
            client.send("## fatal: I have already joined the room '" + roomName + "'");
            return;
        }

        // 現在入っているルームを抜ける
        ChatRoom leavedRoom = roomList.getRoomWith(client);
        leavedRoom.remove(client);
        // 抜けたことを自分に通知する
        client.send("## you left the room '" + leavedRoom.getName() + "'.");
        // メンバーが抜けたことを抜けたルームのメンバーに通知する
        sendMessageToMembersExceptMyself(client, "## user '" + client.getName() + "' left this room.", leavedRoom);

        // 新しいルームに入る
        ChatRoom joinedRoom = roomList.getRoom(roomName);
        joinedRoom.add(client);
        // 参加したことを自分に通知する
        client.send("## you joined the room '" + joinedRoom.getName() + "'.");
        // 新しいメンバーが参加したことを参加したルームのメンバーに通知する
        sendMessageToMembersExceptMyself(client, "## user '" + client.getName() + "' joined this room.");
    }

    private void leaveRoom(Client client, String args) {
        if (!args.matches(Command.LEAVE_ROOM.getArgumentRegex())) {
            sendUsage(client, Command.LEAVE_ROOM);
            return;
        }
        // 自分の参加しているルームを取得する
        ChatRoom room = roomList.getRoomWith(client);

        // 自分の参加しているルームがロビーだったら抜けられない
        if (roomList.getRoomWith(client) == ChatRoom.getLobby()) {
            client.send("## fatal: you have not joined the room.");
            return;
        }

        // 自分がルームの管理者だったらルームを閉じる
        if (room.isAdmin(client)) {
            // ルームのメンバーに管理者が退出したため、ルームをクローズすることを伝える
            sendMessageToMembersExceptMyself(client,
                    "## warning: this room will be closed because the administrator left.");
            // ルームのメンバー全員を退出させる
            for (Client member : room.getMemberList()) {
                if (!room.isAdmin(member)) {
                    room.remove(member);
                    ChatRoom.getLobby().add(member);
                    member.send("## you returned to the lobby.");
                }
            }
            // ルームリストからこのルームを取り除く
            roomList.removeRoom(room);
            // 最後に管理者がルームから出ていく
            room.remove(client);
        }
        else {
            // ルームから自分を削除（抜ける）してルームのメンバーにそのことを通知する
            room.remove(client);
            sendMessageToMembersExceptMyself(client, "## the user '" + client.getName() + "' left this room.", room);
        }

        // ロビーに参加（戻る）して自分にそのことを通知する
        ChatRoom.getLobby().add(client);
        client.send("## you returned to the lobby.");
    }

    private void showRoom(Client client, String args) {
        if (!args.matches(Command.SHOW_ROOM.getArgumentRegex())) {
            sendUsage(client, Command.SHOW_ROOM);
            return;
        }
        // 自分の参加しているルームを取得する
        ChatRoom joinedRoom = roomList.getRoomWith(client);

        client.send("## showing the existing rooms...\n");
        for (ChatRoom room : roomList) {
            // 送信するメッセージ 形式："## room name"
            String message = "## " + room.getName();

            // 自分の参加しているルームにはyou are joined表記を付ける
            if (room == joinedRoom)
                message += " <you are joined>";

            client.send(message);
        }
    }

    private void showMember(Client client, String roomName) {
        System.out.println(client);
        if (!roomName.matches(Command.SHOW_MEMBER.getArgumentRegex())) {
            sendUsage(client, Command.SHOW_MEMBER);
            return;
        }
        // "/show-members"コマンドで呼ばれた場合、roomNameは空文字なので自分の参加しているルーム扱いにする
        if (roomName.isEmpty()) {
            roomName = roomList.getRoomWith(client).getName();
        }
        // 名前にroomNameをもつルームが存在しなかった場合
        if (!roomList.existRoom(roomName)) {
            client.send("## fatal: the room with the specified name does not exist.");
            return;
        }


        // 引数で指定された名前を持つルームを取得する
        ChatRoom room = roomList.getRoom(roomName);

        System.out.println(room);
        client.send("## showing the members in '" + roomName + "'\n");
        for (Client member : room.getMemberList()) {
            // 送信するメッセージ 形式："## client name"
            String message = "## " + member.getName();

            // 管理者はadministrator表記を付ける
            if (room.isAdmin(member)) {
                message += " <Administrator>";
            }
            // 自分自身だったらyourself表記を付ける
            if (member == client) {
                message += " <Yourself>";
            }

            client.send(message);
        }
    }

    private void kickMember(Client client, String userName) {
        if (!userName.matches(Command.KICK_MEMBER.getArgumentRegex())) {
            sendUsage(client, Command.KICK_MEMBER);
            return;
        }
        // 自分の参加しているルームを取得する
        ChatRoom room = roomList.getRoomWith(client);

        // 参加しているルームの管理者が自分でないのであればこのコマンドは実行できない
        if (!room.isAdmin(client)) {
            client.send("## fatal: you are not an administrator of this room." +
                             "\n##        this command can not be executed unless it is an administrator.");
            return;
        }
        // 指定した名前のメンバーがいない場合削除できない
        if (!room.existClient(userName)) {
            client.send("## fatal: the member with the specified name is not in the room.");
            return;
        }
        // 指定したメンバーが自分自身だった場合削除させない（管理者がルームから消えてしまう）
        if (client.getName().equals(userName)) {
            client.send("## fatal: you can not remove yourself from the room.");
            return;
        }

        // 指定したユーザー名でクライアントをルームから取得する
        Client kickedClient = room.get(userName);
        // 蹴られたことをkickedClientに通知する
        kickedClient.send("## you have been exiled from the room '" + room.getName() + "'.");
        // クライアントを退会させる
        leaveRoom(kickedClient, " ");
    }

    private void changeNewAdmin(Client client, String command, String userName){
        if (!command.matches(Command.CHANGE_NEW_ADMIN.getArgumentRegex())) {
            sendUsage(, Command.CHANGE_NEW_ADMIN);
            return;
        }

        // 自分の参加しているルームを取得する
        ChatRoom room = roomList.getRoomWith(client);
        String adminUser = client.getName();

        // 参加しているルームの管理者が自分でないのであればこのコマンドは実行できない
        if (!room.isAdmin(client)) {
            adminUser = client.getName();
            client.send("## fatal: you are not an administrator of this room." +
                    "\n##        this command can not be executed unless it is an administrator.");
            return;
        }
        // 指定した名前のメンバーがいない場合、管理者権限を譲渡できない
        if (!room.existClient(userName)) {
            client.send("## fatal: the member with the specified name is not in the room.");
            return;
        }
        // 指定したメンバーが自分自身だった場合、もうすでにこのコマンドを実行できているので自分は管理者
        if (client.getName().equals(userName)) {
            client.send("## fatal: you can not decide yourself.\n## you are already admin.");
            return;
        }

        Client decidedClient = room.get(userName);
        // 管理者権限をdecidedClientが与えられたことを通知する
        decidedClient.send("## you were gave this room's administrator from the " + adminUser + ".");

        room.setAdmin(decidedClient);

        sendMessageToMembersExceptMyself(decidedClient, "## the user '" + decidedClient.getName() + "' had drawn this room's administrator.");
    }
    private void commandHelp(Client client, String command) {
        try (PropertyReader reader = new PropertyReader(new File("./help.properties"))) {
            reader.load();

            String helpMsg = reader.getProperty(command + ".help");

            // 指定されたコマンドが存在しない場合は/helpコマンドのusageを送る
            if (helpMsg == null) {
                client.send(Command.help(Command.COMMAND_HELP));
                return;
            }

            client.send(helpMsg);
        }
        catch(IOException err){
            JLogger.log(Level.SEVERE, "./help.properties does not read.", err);
            client.send("## fatal: Help message could not be read.");
        }
    }
}
