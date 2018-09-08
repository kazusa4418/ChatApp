package server;

import event.Command;
import event.MessageEvent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    private ServerSocket server;
    private ChatRoomList roomList;

    public ChatServer() {
        try {
            // ポートを指定してServerSocketを立てる
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
        Command command = event.getCommand();
        String body = event.getBody();

        switch (command) {
            case EXIT:
                exit(creator);
                break;
            case SEND_MESSAGE:
                // TODO: ・・・。
                sendMessageToMembersExceptMyself(creator, creator.getName() + " : " + body);
                break;
            case MAKE_ROOM:
                makeRoom(creator, body);
                break;
            case JOIN_ROOM:
                joinRoom(creator, body);
                break;
            case LEAVE_ROOM:
                leaveRoom(creator);
                break;
            case SHOW_ROOM:
                showRoom(creator);
                break;
            case SHOW_MEMBER:
                showMember(creator, body);
                break;
        }
    }

    private void exit(Client client) {

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
        // 既に指定された名前を持つルームが存在していた場合
        if (roomList.existRoom(roomName)) {
            client.send("## the room with the specified name already exists.");
            return;
        }

        // 現在入っているルームを抜ける
        ChatRoom leavedRoom = roomList.getRoomWith(client);
        leavedRoom.remove(client);
        // 抜けたことを自分に通知する
        client.send("## you left the room `" + leavedRoom.getName() + "`.");
        // メンバーが抜けたことを抜けたルームのメンバーに通知する
        sendMessageToMembersExceptMyself(client, "## user `" + client.getName() + "` left this room.", leavedRoom);

        // 新しくルームを作り、ルームの管理者としてルームに入る
        roomList.createNewRoom(roomName, client).add(client);
        // 作成したルームに参加したことを自分に通知する
        client.send("## made a new room ! welcome `" + roomName + "` !");
    }

    private void joinRoom(Client client, String roomName) {
        // 指定したルーム名を持つルームが存在しなかった場合
        if (!roomList.existRoom(roomName)) {
            client.send("## The specified room does not exist. the invalid name `" + roomName + "`.");
            return;
        }

        // 現在入っているルームを抜ける
        ChatRoom leavedRoom = roomList.getRoomWith(client);
        leavedRoom.remove(client);
        // 抜けたことを自分に通知する
        client.send("## you left the room `" + leavedRoom.getName() + "`.");
        // メンバーが抜けたことを抜けたルームのメンバーに通知する
        sendMessageToMembersExceptMyself(client, "## user `" + client.getName() + "` left this room.", leavedRoom);

        // 新しいルームに入る
        ChatRoom joinedRoom = roomList.getRoom(roomName);
        joinedRoom.add(client);
        // 参加したことを自分に通知する
        client.send("## you joined the room `" + joinedRoom.getName() + "`.");
        // 新しいメンバーが参加したことを参加したルームのメンバーに通知する
        sendMessageToMembersExceptMyself(client, "## user `" + client.getName() + "` joined this room.");
    }

    private void leaveRoom(Client client) {
         // 自分の参加しているルームを取得する
        ChatRoom room = roomList.getRoomWith(client);

        // 自分の参加しているルームがロビーだったら抜けられない
        if (roomList.getRoomWith(client) == ChatRoom.getLobby()) {
            client.send("## you have not joined the room.");
            return;
        }

        // ルームから自分を削除（抜ける）してルームのメンバーにそのことを通知する
        room.remove(client);
        sendMessageToMembersExceptMyself(client, "## user `" + client.getName() + "` left this room.", room);
        // ロビーに参加（戻る）して自分にそのことを通知する
        ChatRoom.getLobby().add(client);
        client.send("## you returned to the lobby.");
    }

    private void showRoom(Client client) {
        // 自分の参加しているルームを取得する
        ChatRoom joinedRoom = roomList.getRoomWith(client);

        client.send("## showing the existing rooms...\n");
        for (ChatRoom room : roomList) {
            // 自分の参加しているルームとロビーは表示しない
            if (room == joinedRoom || room == ChatRoom.getLobby())
                continue;

            client.send("## " + room.getName());
        }
    }

    private void showMember(Client client, String roomName) {
        // "/show-members"コマンドで呼ばれた場合、roomNameは空文字なので自分の参加しているルーム扱いにする
        if (roomName.isEmpty()) {
            roomName = roomList.getRoomWith(client).getName();
        }
        // 名前にroomNameをもつルームが存在しなかった場合
        if (!roomList.existRoom(roomName)) {
            client.send("## the room with the specified name does not exist.");
            return;
        }

        // 引数で指定された名前を持つルームを取得する
        ChatRoom room = roomList.getRoom(roomName);

        client.send("## showing the members in `" + roomName + "`\n");
        for (Client member : room.getMemberList()) {
            // 自分だったら表示しない
            if (member == client)
                continue;

            client.send("## " + member.getName());
        }
    }
}
