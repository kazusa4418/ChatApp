package client;

import util.Console;
import util.JLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class MessageReceiver implements Runnable {
    private Socket socket;
    private BufferedReader reader;

    private Thread thread;

    MessageReceiver(Socket socket) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

        thread = new Thread(this);
    }

    void start() {
        thread.start();
    }

    public void run() {
        try {
            while (!socket.isClosed()) {
                String msg = receiveMessage();

                // やだなーこれ
                // 通信先のソケットがクローズされるとEOFになってnull帰ってくるっぽい
                if (msg == null) {
                    break;
                }
                System.out.println(msg);
            }
        }
        catch (IOException err) {
            JLogger.log(Level.SEVERE, "the input stream failed to receive the message.\n" +
                                                                "at MessageReceiver#run I/O error occurred.", err);
            Console.getInstance().pleaseEnter("サーバーとの接続が切れました。");

            // これどうにかならないかな
            System.exit(1);
        }
    }

    String receiveMessage() throws IOException {
        return reader.readLine();
    }
}
