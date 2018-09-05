import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

class ChatClient {
    private Socket socket;
    private MessageSender sender;
    private MessageReceiver receiver;

    void start() {
        login();
        connectServer("127.0.0.1", 10000);
        openStream();
        sender.start();
        receiver.start();
    }

    private void connectServer(String host, int port) {
        try {
            socket = new Socket(InetAddress.getByName(host), port);
        } catch (IOException err) {
            err.printStackTrace();
            throw new AssertionError(err);
        }
    }

    private void openStream() {
        sender = new MessageSender(socket);
        receiver = new MessageReceiver(socket);
    }

    private void login() {
        boolean bl = false;
        while (!bl) {
            System.out.println("ユーザーIDを入力してください");
            int userId = new Scanner(System.in).nextInt();
            System.out.println("パスワードを入力してください");
            String userPass = new Scanner(System.in).nextLine();

            bl = LoginUtils.check(userId, userPass);
            if (bl) {
                System.out.println("正しくログインしました");
            } else {
                System.out.println("ログインに失敗しました\nもう一度お願いします");
            }
        }
    }
}