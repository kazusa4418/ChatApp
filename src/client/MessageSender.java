package client;

import event.MessageEvent;
import event.MessageEventFactory;

import java.io.*;
import java.net.Socket;

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
            throw new AssertionError(err);
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
            err.printStackTrace();
            throw new AssertionError(err);
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
