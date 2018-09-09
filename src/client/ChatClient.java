package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ChatClient {
    private MessageSender sender;
    private MessageReceiver receiver;

    public void start() {
        Socket socket = connectServer(ClientConfiguration.getServerIpAddress(), ClientConfiguration.getServerPortNumber());
        openStream(socket);
        sender.start();
        receiver.start();
    }

    private Socket connectServer(String host, int port) {
        try {
            return new Socket(InetAddress.getByName(host), port);
        }
        catch (IOException err) {
            err.printStackTrace();
            throw new AssertionError(err);
        }
    }

    private void openStream(Socket socket) {
        sender = new MessageSender(socket);
        receiver = new MessageReceiver(socket);
    }
}
