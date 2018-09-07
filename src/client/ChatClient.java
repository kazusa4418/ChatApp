package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ChatClient {
    private Socket socket;
    private MessageSender sender;
    private MessageReceiver receiver;

    public void start() {
        connectServer(ClientConfiguration.getServerIpAddress(), ClientConfiguration.getServerPortNumber());
        openStream();
        sender.start();
        receiver.start();
    }

    private void connectServer(String host, int port) {
        try {
            socket = new Socket(InetAddress.getByName(host), port);
        }
        catch (IOException err) {
            err.printStackTrace();
            throw new AssertionError(err);
        }
    }

    private void openStream() {
        sender = new MessageSender(socket);
        receiver = new MessageReceiver(socket);
    }
}
