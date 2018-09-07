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
            System.out.println(c);
            if (c == client) {
                continue;
            }
            c.send(client.getName() + " : " + body);
        }
    }

    private void makeRoom(Client client, String roomName) {
        // 現在入っているルームを抜ける
        roomList.getRoomWith(client).remove(client);
        // 新しくルームを作り、ルームの管理者としてルームに入る
        roomList.createNewRoom(roomName, client).add(client);

        client.send("make a new room ! welcome `" + roomName + "` !");
    }

    private void addRoom(Client client, String roomName) {
        if (!roomList.existRoom(roomName)) {
            client.send("ルーム : " + roomName + " は存在しません。");
            return;
        }

        // 現在入っているルームを抜ける
        roomList.getRoomWith(client).remove(client);
        // 新しいルームに入る
        roomList.getRoom(roomName).add(client);
    }
}
