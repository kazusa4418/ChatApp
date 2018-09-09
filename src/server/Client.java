package server;

import client.ChatClient;
import event.Command;
import event.MessageEvent;
import server.ChatRoom;
import server.ChatServer;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable {
    private ChatServer server;
    private Socket socket;
    private String name;
    private ChatClient chatClient;

    private Thread thread;

    Client(ChatServer server, Socket socket, String name) {
        this.server = server;
        this.socket = socket;
        ChatRoom.getLobby().add(this);

        this.name = name;
        this.thread = new Thread(this);
    }

    void start() {
        thread.start();
    }

    public void run() {
        try (ObjectInputStream reader = new ObjectInputStream(socket.getInputStream())) {
            while (!socket.isClosed()) {
                MessageEvent event = (MessageEvent) reader.readObject();

                // TODO: まじでここの処理気に入らないいつかなおすわ（なおらない
                if (event.getBody() != null) {
                    // TODO: ここで自分が送信したことを記録するのはダサくない？
                    event.setCreator(this);
                    server.receiveEvent(event);
                }
                // TODO: ここに書くべきではない気がしている
                else {
                    // bodyがnullなので正しくコマンドの引数が与えられていない
                    // コマンドのUsageをクライアントに送信する
                    send(Command.usage(event.getCommand()));
                }
            }
        }
        catch (IOException err) {
            err.printStackTrace();
            throw new AssertionError(err);
        }
        catch (ClassNotFoundException err) {
            err.printStackTrace();
            throw new AssertionError();
        }
    }

    void send(String msg) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(msg);
            writer.newLine();
            writer.flush();
        }
        catch (IOException err) {
            err.printStackTrace();
            throw new AssertionError(err);
        }
    }

    String getName() {
        return name;
    }

    void logout() {
        // Socketをクローズする
        try {
            // もう疲れた・・・。
            // ココらへんは適当です。
            send("/logout");
            socket.close();
        }
        catch (IOException ignore) {}
    }
}
