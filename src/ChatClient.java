import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

class ChatClient {
    private Socket socket;
    private MessageSender sender;
    private MessageReceiver receiver;

    void start() {
        login();
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

    private void login(){
        connectServer(ClientConfiguration.getServerIpAddress(),ClientConfiguration.getServerPortNumber());

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

