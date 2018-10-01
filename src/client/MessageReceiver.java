package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MessageReceiver implements Runnable {
    private Socket socket;
    private BufferedReader reader;

    private Thread thread;

    MessageReceiver(Socket socket) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        thread = new Thread(this);
    }

    void start() {
        thread.start();
    }

    public void run() {
        try {
            while (!socket.isClosed()) {
                String msg = reader.readLine();

                System.out.println(msg);
            }
        }
        catch (IOException err) {
            //JLogger.log(Level.SEVERE, "the input stream failed to receive the message.\n" +
            //                                                    "at MessageReceiver#run I/O error occurred.", err);
            System.err.println("サーバーとの接続が切れました。");

            // これどうにかならないかな
            System.exit(1);
        }
    }
}
