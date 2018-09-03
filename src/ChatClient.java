import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

class ChatClient {
    private Socket socket;
    private MessageSender sender;
    private MessageReceiver receiver;

    void start() {
        System.out.println("ユーザーIDを入力してください");
        int usId = new java.util.Scanner(System.in).nextInt();
        System.out.println("パスワードを入力してください");
        String usPs = new java.util.Scanner(System.in).nextLine();

        boolean bl = LoginUtils.check(usId,usPs);

        if(bl) {
            System.out.println("正しくログインしました");
            connectServer("127.0.0.1", 10000);
            openStream();
            sender.start();
            receiver.start();
        }
        else{
            System.out.println("ログインに失敗しました");
        }
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
