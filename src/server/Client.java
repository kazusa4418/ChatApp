package server;

import event.Command;
import event.MessageEvent;
import event.MessageEventFactory;
import mysql.MySql;
import util.DatabaseUtils;
import util.JLogger;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;

public class Client implements Runnable {
    private ChatServer server;
    private Socket socket;

    private String name;
    private Status status;

    private Thread thread;

    Client(ChatServer server, Socket socket) {
        this.server = server;
        this.socket = socket;

        authenticate();

        this.thread = new Thread(this);
    }

    void start() {
        thread.start();
    }

    public void run() {
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
            server.runAction(this, Command.LOGOUT);
        }
    }

    private void authenticate() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (status != Status.AVAILABLE) {
                send("user id > ");
                String id = reader.readLine();
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
                    case ALREADY:
                        send("既にログインしています。");
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
            // ソケットに何らかの異常が発生した場合
            // 主に認証中にクライアントが強制終了した場合などに発生する
            JLogger.log(Level.WARNING, "I/O error.", err);
            status = Status.EXCEPTION;
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

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    Status getStatus() {
        return status;
    }

    void disconnect() {
        try {
            DatabaseUtils.updateNowLogin(this, false);
            socket.close();
        }
        catch (IOException err) {
            JLogger.warning("already closed.");
        }
        catch (SQLException err) {
            JLogger.log(Level.SEVERE, "can not update now_login status in table 'users'", err);
        }
    }
}
