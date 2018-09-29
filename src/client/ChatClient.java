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

    // これ本当は使わないよ。応急処置。
    // ログイン処理の実装間に合わなそうだからこれで代用
    private void inputMyName() throws FailedConnectToServerException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("input my name > ");
        try {
            this.name = br.readLine();
        }
        catch (IOException err) {
            throw new FailedConnectToServerException(
                    "WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW",
                    "WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW",
                    err
            );
        }
    }

    // 名前を変えるリクエストを送信してるけど、
    // 実際にはこの方法で最初の名前通知はしません。

    // IOExceptionの例外処理していないのはわざと
    private void sendMyNameToServer() {
        try {
            sender.sendToServer("/name " + name.trim());
        }
        catch (IOException ignore) {}
    }
}
