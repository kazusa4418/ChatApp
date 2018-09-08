package server;

import event.MessageEvent;
import server.ChatRoom;
import server.ChatServer;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable {
    private ChatServer server;
    private Socket socket;

    private String name;

    Client(ChatServer server, Socket socket, String name) {
        this.server = server;
        this.socket = socket;

        // TODO: 要リファクタリング
        ChatRoom.getLobby().add(this);

        this.name = name;
    }

    void start() {
        new Thread(this).start();
    }

    public void run() {
        try (ObjectInputStream reader = new ObjectInputStream(socket.getInputStream())) {
            while (!socket.isClosed()) {
                MessageEvent event = (MessageEvent) reader.readObject();

                if (event != null) {
                    // TODO: ここで自分が送信したことを記録するのはダサくない？
                    event.setCreator(this);
                    server.receiveEvent(event);
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
}
