package client;

import util.Console;
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

            authenticate();

            sender.start();
            receiver.start();
        }
        catch (FailedConnectToServerException err) {
            JLogger.log(Level.SEVERE, err.getLogMsg(), err.getCauseException());
            Console.getInstance().pleaseEnter(err.getErrMsg());
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

    private void authenticate() throws FailedConnectToServerException {
        Console console = Console.getInstance();

        String status;
        try {
            do {
                String id = console.readLine("user id > ");
                sender.sendToServer(id);

                String pw = console.readPassword("user password > ");
                sender.sendToServer(pw);

                status = receiver.receiveMessage();

                switch (status) {
                    case "AVAILABLE":
                        console.pleaseEnter("success to log in.");
                        break;
                    case "UNMATCHED":
                        console.println("Sorry. try again.");
                        break;
                    case "ALREADY":
                        console.println("this account is already logged in.");
                        break;
                    case "EXCEPTION":
                        console.println("an error occurred.");
                        break;
                }
            }
            while (!status.equals("AVAILABLE"));
            console.clear();
        }
        catch (IOException err) {
            throw new FailedConnectToServerException(
                    "サーバーとの接続が切れました。",
                    "connection with the server has expired.",
                    err
            );
        }
    }
}
