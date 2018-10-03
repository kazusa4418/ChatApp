package client;

import util.Console;
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
        Console console = Console.getInstance();

        while (!socket.isClosed()) {
            String msg = console.readLine();

            try {
                // これやだ
                if (!msg.isEmpty()) {
                    sendToServer(msg);
                }
            }
            catch (IOException err) {
                JLogger.log(Level.SEVERE, "the output stream failed to send the message.\n" +
                        "at MessageSender#run I/O error occurred.", err);
                console.pleaseEnter("サーバーとの接続が切れました");
                // これどうにかならないかな
                System.exit(1);
            }

            // めっちゃ気に入らない。どうにかしたい・・・。
            if (msg.equals("/logout")) {
                Console.getInstance().sleep(100);
                try {
                    socket.close();
                }
                catch (IOException err) {

                }

                Console.getInstance().pleaseEnter("logged out.");
                System.exit(0);
            }
        }
    }

    void sendToServer(String msg) throws IOException {
        writer.write(msg);
        writer.newLine();
        writer.flush();
    }
}
