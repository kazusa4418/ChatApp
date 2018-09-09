package client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ChatClient {
    private MessageSender sender;
    private MessageReceiver receiver;

    public void start() {
        login();
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

    private void login(){
        Socket socket = connectServer(ClientConfiguration.getServerIpAddress(),ClientConfiguration.getServerPortNumber());

        try {
            String display = "";
            String buf = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            display = br.readLine();
            System.out.println(display);

            bw.write(buf);
            bw.newLine();
            bw.flush();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
