package client;

import util.JLogger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;

public class ChatClient {
    private Socket socket;
    private MessageSender sender;
    private MessageReceiver receiver;

    private String name;

    public void start() {
        try {
            inputMyName();

            connectServer(ClientConfiguration.getServerIpAddress(), ClientConfiguration.getServerPortNumber());
            openStream();

            sender.start();
            receiver.start();

            sendMyNameToServer();
        }
        catch (IOException err) {
            String errMsg = "## fatal: can not access the server. ip-address.\n" +
                                    "       ip-address: " + ClientConfiguration.getServerIpAddress() +
                                    "       port: " + ClientConfiguration.getServerPortNumber();
            JLogger.log(Level.SEVERE, errMsg, err);
            System.err.println(errMsg);
        }
    }

    private void connectServer(String host, int port) throws IOException {
        socket = new Socket(InetAddress.getByName(host), port);
    }

    private void openStream() {
        sender = new MessageSender(socket);
        receiver = new MessageReceiver(socket);
    }

    // これ本当は使わないよ。応急処置。
    // ログイン処理の実装間に合わなそうだからこれで代用
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

    // 名前を変えるリクエストを送信してるけど、
    // 実際にはこの方法で最初の名前通知はしません。
    private void sendMyNameToServer() {
        sender.sendToServer("/name " + name.trim());
    }
}
