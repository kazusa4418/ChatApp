package client;

import util.ErrorMessage;
import util.JLogger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;

public class ChatClient {
    private Socket socket;
    private MessageSender sender;
    private MessageReceiver receiver;

    public void start() {
        try {
            socket = connectServer(ClientConfiguration.getServerIpAddress(), ClientConfiguration.getServerPortNumber());
            openStream();
            sender.start();
            receiver.start();
        }
        catch (IOException err) {
            String errMsg = ErrorMessage.failedAccessServer(ClientConfiguration.getServerIpAddress(),
                                                                ClientConfiguration.getServerPortNumber());
            JLogger.log(Level.SEVERE, "errMsg", err);
            System.err.println(errMsg);
        }
    }

    private Socket connectServer(String host, int port) throws IOException {
        return new Socket(InetAddress.getByName(host), port);
    }

    private void openStream() {
        sender = new MessageSender(socket);
        receiver = new MessageReceiver(socket);
    }
}
