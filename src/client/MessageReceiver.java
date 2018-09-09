package client;

import util.JLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;

public class MessageReceiver implements Runnable {
    private Socket socket;
    private BufferedReader reader;

    private Thread thread;

    MessageReceiver(Socket socket) {
        try {
            this.socket = socket;
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException err) {
            JLogger.log(Level.SEVERE, "failed to construct input stream.", err);
            System.err.println("fatal: failed to connect to the server.");
            System.exit(3);
        }

        thread = new Thread(this);
    }

    void start() {
        thread.start();
    }

    public void run() {
        try {
            while (!socket.isClosed()) {
                String msg = reader.readLine();

                // もう疲れたのでここの実装は適当です
                if (msg.equals("/logout")) {
                    socket.close();
                    msg = "ログアウトしました";
                }
                System.out.println(msg);
            }
        }
        catch (IOException err) {
            JLogger.log(Level.SEVERE, "fatal: the input stream failed to receive the message.\n" +
                                                                "I/O error occurred", err);
            System.err.println("fatal: failed to connect to the server.");
        }
    }
}
