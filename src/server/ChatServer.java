package server;

import event.Command;
import event.MessageEvent;
import server.authentication.Status;
import util.JLogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

public class ChatServer implements Runnable {
    private ServerSocket server;
    private ChatRoomList roomList;

    public ChatServer() {
        try {
            server = new ServerSocket(33333);
        }
        catch (IOException err) {
            JLogger.log(Level.SEVERE, "could not open the socket", err);
            System.exit(1);
        }

        roomList = new ChatRoomList();
    }

    public void start(int threadNum) {
        for (int i = 0; i < threadNum; i++ ) {
            new Thread(this).start();
        }
    }

    public void run() {
        while (!server.isClosed()) {
            try {
                // クライアントからのアクセスを待つ
                Socket socket = server.accept();
                Client client = new Client(this, socket);

                // クライアントが有効状態でなかったらコネクションを切る
                if (client.getStatus() == Status.AVAILABLE) {
                    // ロビーに入室し、メンバーにログインしたことを通知する。ちなみに気に入ってない。
                    ChatRoom.getLobby().add(client);
                    sendMessageToMembersExceptMyself(client, "## user '" + client.getName() + "' log in.");
                    client.start();
                }
                else {
                    client.disconnect();
                }
            }
            catch (IOException err) {
                // server.acceptは待機中に例外が起こる可能性があるらしい。
                // この例外処理が適切かはわからないけど、一応自らサーバーを落とすのはやめておく
                // (まさかこのエラー起きたらもうそのServerSocketは使い物にならないとかないよね・・・。)
                JLogger.log(Level.SEVERE, "an error occurred during accept.", err);
            }
        }
    }

    void receiveEvent(MessageEvent event) {
        Client creator = event.getCreator();
        Command command = event.getCommand();
        String body = event.getBody().trim();

        runAction(creator, command, body);
    }

    void runAction(Client executor, Command command) {
        runAction(executor, command, "");
    }

    void runAction(Client executor, Command command, String body) {
        switch (command) {
            case LOGOUT:
                logout(executor, body);
                break;
            case SEND_MESSAGE:
                sendMessageToMembersExceptMyself(executor, executor.getName() + " : " + body);
                break;
            case SECRET_MESSAGE:
                sendSecretMessage(executor, body);
                break;
            case MAKE_ROOM:
                makeRoom(executor, body);
                break;
            case JOIN_ROOM:
                joinRoom(executor, body);
                break;
            case LEAVE_ROOM:
                leaveRoom(executor, body);
                break;
            case SHOW_ROOMS:
                showRooms(executor, body);
                break;
            case SHOW_MEMBERS:
                showMembers(executor, body);
                break;
            case KICK_MEMBER:
                kickMember(executor, body);
                break;
            case CHANGE_ADMIN:
                changeAdmin(executor, body);
                break;
            case COMMAND_HELP:
                commandHelp(executor, body);
                break;
            case CLOSE_ROOM:
                closeRoom(executor, body);
                break;
            case NOT_FOUND:
                executor.send("## this is a not system command.");
                break;
        }
    }

    private void sendUsage(Client target, Command command) {
        target.send(Command.usage(command));
    }

    private void logout(Client client, String args) {
        System.err.println("RUNRUNRUN");
        if (!args.matches(Command.LOGOUT.getArgumentRegex())) {
            sendUsage(client, Command.LOGOUT);
            return;
        }

        // 管理者だったらルームを閉じる
        if (roomList.getRoomWith(client).isAdmin(client)) {
            closeRoom(client, "");
        }
        else {
            sendMessageToMembersExceptMyself(client, "## the user '" + client.getName() + "' log outs.");
        }
        // 参加しているルームを抜ける
        roomList.getRoomWith(client).remove(client);

        client.disconnect();
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

    private void sendSecretMessage(Client client, String body) {
        if (!body.matches(Command.SECRET_MESSAGE.getArgumentRegex())) {
            sendUsage(client, Command.SECRET_MESSAGE);
            return;
        }

        String userName = body.split(" ", 2)[0];
        String message = body.split(" ", 2)[1];

        ChatRoom joinedRoom = roomList.getRoomWith(client);
        // 送るユーザーがルームにいなかった場合
        if (!joinedRoom.existClient(userName)) {
            client.send("## fatal: the user '" + userName + "' does not exist in '" + joinedRoom.getName() + "'.");
            return;
        }
        // 自分自身には送れない
        if (joinedRoom.get(userName).getName().equals(client.getName())) {
            client.send("## fatal: can not send secret message to yourself.");
            return;
        }

        Client dest = joinedRoom.get(userName);
        dest.send(client.getName() + " >>> " + message);
    }

    private void makeRoom(Client client, String roomName) {
        if (!roomName.matches(Command.MAKE_ROOM.getArgumentRegex())) {
            sendUsage(client, Command.MAKE_ROOM);
            return;
        }
        // 既に指定された名前を持つルームが存在していた場合
        if (roomList.existRoom(roomName)) {
            client.send("## fatal: the room '" + roomName + "' already exists.");
            return;
        }

        ChatRoom joinedRoom = roomList.getRoomWith(client);

        // 現在入っているルームの管理者だったらそのルームを抜けちゃだめ
        if (joinedRoom.isAdmin(client)) {
            client.send("## fatal: you are the administrator of the " + joinedRoom.getName() + "`.\n" +
                             "##        should explicitly leave the room by running \"/change-admin or /close\" command.");
            return;
        }
        // 現在入っているルームを抜ける
        joinedRoom.remove(client);
        // 抜けたことを自分に通知する
        client.send("## you left the room '" + joinedRoom.getName() + "'.");
        // メンバーが抜けたことを抜けたルームのメンバーに通知する
        sendMessageToMembersExceptMyself(client, "## user '" + client.getName() +
                                                            "' left '" + joinedRoom.getName() + "'.", joinedRoom);

        // 新しくルームを作り、ルームの管理者としてルームに入る
        // TODO: これはロビーの管理者がNULLになってしまうためにaddメソッドで追加してる
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
            client.send("## fatal: The room '" + roomName + "' does not exist.");
            return;
        }

        ChatRoom leavedRoom = roomList.getRoomWith(client);
        // 指定したルームに既に参加していた場合
        if (leavedRoom == roomList.getRoom(roomName)) {
            client.send("## fatal: you have already joined the room '" + roomName + "'");
            return;
        }
        // 自分がルームの管理者だった場合
        if (leavedRoom.isAdmin(client)) {
            client.send("## fatal: you are the administrator of the " + leavedRoom.getName() + "`.\n" +
                             "##        should explicitly leave the room by running \"/change-admin or /close\" command.");
            return;
        }

        // 現在入っているルームを抜ける
        leavedRoom.remove(client);
        // 抜けたことを自分に通知する
        client.send("## you left the room '" + leavedRoom.getName() + "'.");
        // メンバーが抜けたことを抜けたルームのメンバーに通知する
        sendMessageToMembersExceptMyself(client, "## user '" + client.getName() +
                                                          "' left '" + leavedRoom.getName() + "'.", leavedRoom);

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

        // 自分がルームの管理者だったら抜けられない
        if (room.isAdmin(client)) {
            client.send("## fatal: you are an administrator.\n" +
                             "##        the administrator can not escape from '" + room.getName() + "'.");
            return;
        }

        room.remove(client);
        sendMessageToMembersExceptMyself(client, "## the user '" + client.getName() +
                                                            "' left '" + room.getName() + "'.", room);

        // ロビーに参加（戻る）して自分にそのことを通知する
        ChatRoom.getLobby().add(client);
        client.send("## you returned to the lobby.");
    }

    private void showRooms(Client client, String args) {
        if (!args.matches(Command.SHOW_ROOMS.getArgumentRegex())) {
            sendUsage(client, Command.SHOW_ROOMS);
            return;
        }
        // 自分の参加しているルームを取得する
        ChatRoom joinedRoom = roomList.getRoomWith(client);

        StringBuilder message = new StringBuilder("## showing the existing rooms...\n\n");
        for (ChatRoom room : roomList) {
            // 送信するメッセージ 形式："## room name"
            message.append("## ").append(room.getName());

            // 自分の参加しているルームにはyou are joined表記を付ける
            if (room == joinedRoom)
                message.append(" <joined>");

            message.append("\n");
        }
        client.send(message.toString());
    }

    private void showMembers(Client client, String roomName) {
        if (!roomName.matches(Command.SHOW_MEMBERS.getArgumentRegex())) {
            sendUsage(client, Command.SHOW_MEMBERS);
            return;
        }
        // "/show-members"コマンドで呼ばれた場合、roomNameは空文字なので自分の参加しているルーム扱いにする
        if (roomName.isEmpty()) {
            roomName = roomList.getRoomWith(client).getName();
        }
        // 名前にroomNameをもつルームが存在しなかった場合
        if (!roomList.existRoom(roomName)) {
            client.send("## fatal: the room '" + roomName + "' does not exist.");
            return;
        }

        // 引数で指定された名前を持つルームを取得する
        ChatRoom room = roomList.getRoom(roomName);

        StringBuilder message = new StringBuilder("## showing the members in '" + roomName + "'\n\n");
        for (Client member : room.getMemberList()) {
            // 送信するメッセージ 形式："## client name"
            message.append("## ").append(member.getName());

            // 管理者はadministrator表記を付ける
            if (room.isAdmin(member)) {
                message.append(" <administrator>");
            }
            // 自分自身だったらyourself表記を付ける
            if (member == client) {
                message.append(" <yourself>");
            }
            message.append("\n");
        }
        client.send(message.toString());
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
            client.send("## fatal: the member '" + userName + "' is not in the room.");
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
        leaveRoom(kickedClient, "");
        client.send("## success: you kicked client '" + kickedClient.getName() + "'");
    }

    private void changeAdmin(Client client, String userName){
        if (!userName.matches(Command.CHANGE_ADMIN.getArgumentRegex())) {
            sendUsage(client, Command.CHANGE_ADMIN);
            return;
        }

        // 自分の参加しているルームを取得する
        ChatRoom room = roomList.getRoomWith(client);
        String adminUser = client.getName();

        // 参加しているルームの管理者が自分でないのであればこのコマンドは実行できない
        if (!room.isAdmin(client)) {
            client.send("## fatal: you are not an administrator of this room.\n" +
                             "##        this command can not be executed unless it is an administrator.");
            return;
        }
        // 指定した名前のメンバーがいない場合、管理者権限を譲渡できない
        if (!room.existClient(userName)) {
            client.send("## fatal: the member '" + userName + "' is not in the room.");
            return;
        }
        // 指定したメンバーが自分自身だった場合、既に管理者になっている
        if (room.isAdmin(room.get(userName))) {
            client.send("## fatal: you can not decide yourself.\n## you are already admin.");
            return;
        }

        Client decidedClient = room.get(userName);
        // 管理者権限をdecidedClientが与えられたことを通知する
        decidedClient.send("## you were gave this room's administrator from the " + adminUser + ".");

        room.setAdmin(decidedClient);

        sendMessageToMembersExceptMyself(decidedClient, "## the user '" + decidedClient.getName() + "' had drawn this room's administrator.");
    }

    private void closeRoom(Client client, String body) {
        if (!body.matches(Command.CLOSE_ROOM.getArgumentRegex())) {
            sendUsage(client, Command.CLOSE_ROOM);
            return;
        }

        ChatRoom joinedRoom = roomList.getRoomWith(client);

        // 管理者じゃないならこのコマンドは実行できない
        if (!joinedRoom.isAdmin(client)) {
            client.send("## fatal: you are not an administrator of this room.\n" +
                    "##        this command can not be executed unless it is an administrator.");
            return;
        }

        Client[] clients = joinedRoom.getMemberList();
        // ルームメンバーにルームが閉じられることを通知して退出させる
        for (Client c : clients) {
            c.send("## warning: this room will be closed because the administrator left.");
            joinedRoom.remove(c);
            ChatRoom.getLobby().add(c);
            c.send("## you returned to the lobby.");
        }
        roomList.deleteRoom(joinedRoom);
    }

    private void commandHelp(Client client, String commandStr) {
        Command command = Command.get("/" + commandStr);

        // 指定されたコマンドが存在しなかった時
        if (command == Command.NOT_FOUND) {
            command = Command.COMMAND_HELP;
        }

        client.send(Command.help(command));
    }
}
