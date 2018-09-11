package client;

import util.JLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;

public class ChatClient {
    private Socket socket;
    private MessageSender sender;
    private MessageReceiver receiver;

    private String name;

    public void start() {
        try {
            inputMyName();

            socket = connectServer(ClientConfiguration.getServerIpAddress(), ClientConfiguration.getServerPortNumber());
            openStream();

            sender.start();
            receiver.start();

            sendMyNameToServer();
        }
        catch (IOException err) {
            String errMsg = "fatal: can not access the server. ip-address.\n" +
                                    "       ip-address: " + ClientConfiguration.getServerIpAddress() +
                                    "       port: " + ClientConfiguration.getServerPortNumber();
            JLogger.log(Level.SEVERE, errMsg, err);
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

    private void inputMyName() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("input my name > ");
        try {
            this.name = br.readLine();
        }
        catch (IOException err) {
            throw new AssertionError();
        }
    }

    private void sendMyNameToServer() {
        sender.sendToServer("/name " + name.trim());
    }
}
