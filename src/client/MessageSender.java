package client;

import util.JLogger;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;

class MessageSender implements Runnable {
    private Socket socket;
    private BufferedWriter writer;

    private Thread thread;

    MessageSender(Socket socket) throws IOException {
        this.socket = socket;
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

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

                // 死ぬほど気に入らない
                if (msg.equals("/logout")) {
                    System.out.println("ログアウトしました。");
                    System.exit(0);
                }
            }
        }
        catch (IOException err) {
            JLogger.log(Level.SEVERE, "the output stream failed to send the message.\n" +
                                                            "at MessageSender#run I/O error occurred.", err);
            System.err.println("サーバーとの接続が切れました。");
            // これどうにかならないかな
            System.exit(1);
        }
    }

    void sendToServer(String msg) throws IOException {
        writer.write(msg);
        writer.newLine();
        writer.flush();
    }
}
