package server;

import event.MessageEvent;
import event.MessageEventFactory;
import util.JLogger;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;

public class Client implements Runnable {
    private ChatServer server;
    private Socket socket;

    private String name;
    private Status status;

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
        authenticate();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (!socket.isClosed()) {
                String msg = reader.readLine();

                MessageEvent event = MessageEventFactory.createMessageEvent(this, msg);
                server.receiveEvent(event);
            }
        }
        catch (IOException err) {
            JLogger.log(Level.WARNING, "connection with the client has expired.", err);
            // ソケットに異常があるのでログアウトさせる
            // 主にクライアントが/logoutコマンドを使用せず強制終了したときに実行される
            MessageEvent event = MessageEventFactory.createMessageEvent(this, "/logout");
            server.receiveEvent(event);
        }
    }

    private void authenticate() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (status != Status.AVAILABLE) {
                send("user id > ");
                String id = reader.readLine();
                System.out.println("user_id: " + id);
                send("user pw > ");
                String pw = reader.readLine();

                Response response = Authenticator.authenticate(id, pw);

                switch (response.getStatus()) {
                    case AVAILABLE:
                        send("welcome! '" + response.getUserName() + "'.");
                        break;
                    case UNMATCHED:
                        send("IDまたはパスワードが違います。");
                        break;
                    case EXCEPTION:
                        send("問題が発生しました。");
                        break;
                }
                status = response.getStatus();
                name = response.getUserName();
            }
        }
        catch (IOException err) {
            JLogger.log(Level.SEVERE, "I/O error.", err);
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
            JLogger.log(Level.SEVERE, "failed to send message.", err);
        }
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    void disconnect() {
        try {
            socket.close();
        }
        catch (IOException err) {
            JLogger.log(Level.WARNING, "already closed.", err);
        }
    }
}
