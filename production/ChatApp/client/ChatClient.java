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

    public void start() {
        try {
            connectServer(ClientConfiguration.getServerIpAddress(), ClientConfiguration.getServerPortNumber());
            openStream();

            sender.start();
            receiver.start();
        }
        catch (FailedConnectToServerException err) {
            JLogger.log(Level.SEVERE, err.getLogMsg(), err.getCauseException());
            System.err.println(err.getErrMsg());
        }
    }

    private void connectServer(String host, int port) throws FailedConnectToServerException {
        try {
            socket = new Socket(InetAddress.getByName(host), port);
        }
        catch (IOException err) {
            throw new FailedConnectToServerException(
                    "サーバーが見つかりませんでした。",
                    "can not access the server.\n" +
                            "\tip-address: " + ClientConfiguration.getServerIpAddress() +
                            "\tport: " + ClientConfiguration.getServerPortNumber(),
                    err
            );
        }
    }

    private void openStream() throws FailedConnectToServerException {
        try {
            sender = new MessageSender(socket);
            receiver = new MessageReceiver(socket);
        }
        catch (IOException err) {
            throw new FailedConnectToServerException(
                    "サーバーに接続できませんでした。",
                    "failed to connect to the server",
                    err
            );
        }
    }
}
