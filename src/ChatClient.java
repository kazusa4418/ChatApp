import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

class ChatClient {
    private Socket socket;
    private MessageSender sender;
    private MessageReceiver receiver;

    void start() {
        connectServer("127.0.0.1", 10000);
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
}
