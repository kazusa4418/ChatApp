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
        ChatRoom.getDefaultRoom().add(this);

        this.name = name;
    }

    void start() {
        new Thread(this).start();
    }

    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (!socket.isClosed()) {
                if (!reader.ready()) {
                    continue;
                }

                String msg = reader.readLine();

                if (msg == null) {
                    continue;
                }
                // TODO: ここのthis渡しをどうにかできないか検討する
                MessageEvent event = MessageEventFactory.createMessageEvent(this, msg);
                if (event != null) {
                    server.receiveEvent(event);
                }
            }
        }
        catch (IOException err) {
            err.printStackTrace();
            throw new AssertionError(err);
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
