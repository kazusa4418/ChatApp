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
        Client sender = event.getCreator();
        Command command = event.getCommand();
        String body = event.getBody();

        switch (command) {
            case EXIT:
                exit(sender);
                break;
            case SEND_MESSAGE:
                sendMessage(sender, body);
                break;
            case MAKE_ROOM:
                makeRoom(sender, body);
                break;
            case CHANGE_ROOM:
                addRoom(sender, body);
                break;

        }
    }

    private void exit(Client client) {
        ChatRoom room = roomList.getRoomWith(client);

        if (roomList.getRoomWith(client) == ChatRoom.getDefaultRoom()) {
            client.send("デフォルトルームは抜けられません。");
            return;
        }

        room.remove(client);
        ChatRoom.getDefaultRoom().add(client);

        sendMessage(client, client.getName() + "さんが退出しました。", room);
    }

    private void sendMessage(Client client, String body) {
        sendMessage(client, body, roomList.getRoomWith(client));
    }

    private void sendMessage(Client client, String body, ChatRoom room) {
        Client[] members = room.getMemberList();

        for (Client c : members) {
            if (c == client) {
                continue;
            }
            c.send(client.getName() + " : " + body);
        }
    }

    // TODO: メソッド名ながすぎませんか
    private void sendMessageToMembersExceptMyself(Client myself, String message) {
        sendMessageToMembersExceptMyself(myself, message, roomList.getRoomWith(myself));
    }

    private void sendMessageToMembersExceptMyself(Client myself, String message, ChatRoom room) {
        Client[] clients = room.getMemberList();

        for (Client client : clients) {
            if (client == myself)
                continue;

            client.send(message);
        }
    }

    private void makeRoom(Client client, String roomName) {
        // 現在入っているルームを抜ける
        roomList.getRoomWith(client).remove(client);
        // 新しくルームを作り、ルームの管理者としてルームに入る
        roomList.createNewRoom(roomName, client).add(client);

        client.send("made a new room ! welcome `" + roomName + "` !");
    }

    private void addRoom(Client client, String roomName) {
        // 指定したルーム名を持つルームが存在しなかった場合
        if (!roomList.existRoom(roomName)) {
            client.send("## The specified room does not exist. the invalid name `" + roomName + "`.");
            return;
        }

        // 現在入っているルームを抜ける
        ChatRoom removedRoom = roomList.getRoomWith(client);
        removedRoom.remove(client);
        // 抜けたことを自分に通知する
        client.send("## you left the room `" + removedRoom.getName() + "`.");
        // メンバーが抜けたことを抜けたルームのメンバーに通知する
        sendMessageToMembersExceptMyself(client, "## user `" + client.getName() + "` left this room.", removedRoom);

        // 新しいルームに入る
        ChatRoom joinedRoom = roomList.getRoom(roomName);
        joinedRoom.add(client);
        // 参加したことを自分に通知する
        client.send("## you joined the room `" + joinedRoom.getName() + "`.");
        // 新しいメンバーが参加したことを参加したルームのメンバーに通知する
        sendMessageToMembersExceptMyself(client, "## user `" + client.getName() + "` joined this room.");
    }
}
