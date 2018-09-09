package client;

import util.JLogger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;

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
            String errMsg = "fatal: can not access the server. ip-address.\n" +
                    "ip-address: " + ClientConfiguration.getServerIpAddress() + "\n" +
                    "port: " + ClientConfiguration.getServerPortNumber();

            System.err.println(errMsg);
            JLogger.log(Level.SEVERE, "", err);
            System.exit(2);
            return null;
        }
    }

    private void openStream(Socket socket) {
        sender = new MessageSender(socket);
        receiver = new MessageReceiver(socket);
    }
}
