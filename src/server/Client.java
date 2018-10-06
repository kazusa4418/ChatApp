package server;

import event.Command;
import event.MessageEvent;
import event.MessageEventFactory;
import server.authentication.Authenticator;
import server.authentication.Response;
import server.authentication.Status;
import util.DatabaseUtils;
import util.JLogger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;

public class Client implements Runnable {
    private ChatServer server;
    private Socket socket;

    private String name = "";
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

    // なんかごちゃごちゃしてる気がする
    public void run() {
        authenticate();

        if (status != Status.AVAILABLE) {
            return;
        }

        try {
            DatabaseUtils.updateNowLogin(this, true);
        }
        catch (SQLException err) {
            // TODO: どうするかまだ迷ってる
            return;
        }

        ChatRoom.getLobby().add(this);
        send("## welcome! '" + name + "'.\n## joined the 'lobby'!");
        server.runAction(this, Command.SEND_MESSAGE, "## user '" + name + "' log in.");
        JLogger.info("[login] user '" + name + "' from: '" + socket.getInetAddress() + "'");


        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (!socket.isClosed()) {
                String msg = reader.readLine();
                JLogger.info("[receive] from '" + name + "' " + socket.getInetAddress() + " : \"" + msg + "\"");

                MessageEvent event = MessageEventFactory.createMessageEvent(this, msg);
                server.receiveEvent(event);
            }
        }
        catch (IOException err) {
            JLogger.warning("connection with the client has expired.");
            // ソケットに異常があるのでログアウトさせる
            // 主にクライアントが/logoutコマンドを使用せず強制終了したときに実行される
            server.runAction(this, Command.LOGOUT);
        }
    }

    private void authenticate() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (status != Status.AVAILABLE) {
                String id = reader.readLine();
                String pw = reader.readLine();

                Response response = Authenticator.authenticate(id, pw);

                send(response.getStatus().toString());

                status = response.getStatus();
                name = response.getUserName();

                JLogger.info("[authenticate] status: '" + status + "' id: '" + id + "' pw: '" + pw + "' from: '" + socket.getInetAddress() + "'");
            }
        }
        catch (IOException err) {
            // ソケットに何らかの異常が発生した場合
            // 主に認証中にクライアントが強制終了した場合などに発生する
            JLogger.warning("connection with the client has expired.");
            status = Status.EXCEPTION;
        }
    }

    void send(String msg) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(msg);
            writer.newLine();
            writer.flush();
            JLogger.info("[send] to '" + name + "' " + socket.getInetAddress() + " : \""+ msg + "\"");
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
            JLogger.info("[logout] user '" + name + "' host: '" + socket.getInetAddress() + "'");
        }
        catch (IOException err) {
            JLogger.warning("already closed.");
        }
        catch (SQLException err) {
            JLogger.log(Level.SEVERE, "can not update now_login status in table 'users'", err);
        }
    }
}
