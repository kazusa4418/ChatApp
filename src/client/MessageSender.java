package client;

import event.MessageEvent;
import event.MessageEventFactory;
import util.JLogger;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;

class MessageSender implements Runnable {
    private Socket socket;
    private ObjectOutputStream writer;

    private Thread thread;

    MessageSender(Socket socket) {
        try {
            this.socket = socket;
            this.writer = new ObjectOutputStream(socket.getOutputStream());
        }
        catch (IOException err) {
            JLogger.log(Level.SEVERE, "failed to build output stream", err);
            System.err.println("fatal: failed to connect to the server.");
            System.exit(5);
        }

        thread = new Thread(this);
    }

    void start() {
        thread.start();
    }

    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (!socket.isClosed()) {
                String msg = reader.readLine();

                sendToServer(msg);
            }
        }
        catch (IOException err) {
            JLogger.log(Level.SEVERE, "入力ストリームがメッセージの受信に失敗しました" +
                    "入出力のエラーが発生しました", err);
            // そもそもIOExceptionがスローされた時点でsocketもおかしくなってそう・・・。
            // もしかしたら的外れなのかもしれない
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                JLogger.info("入力ストリームを再取得しました");
            }
            catch (IOException ioError) {
                JLogger.log(Level.SEVERE, "入力ストリームの再取得に失敗しました", ioError);
                System.err.println("fatal: サーバーとのコネクションが切れました");
                System.exit(4);
            }
        }
    }

    private void sendToServer(String msg) {
        try {
            MessageEvent event = MessageEventFactory.createMessageEvent(msg);

            if (!socket.isClosed()) {
                writer.writeObject(event);
                writer.flush();
            }
        }
        catch (IOException err) {
            err.printStackTrace();
            throw new AssertionError(err);
        }
    }
}
