import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Tester {
    private Socket socket;
    private static BufferedWriter writer;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 10000);

        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void sendMessage(String msg) throws IOException {
        writer.write(msg);
        writer.newLine();
        writer.flush();
    }
}
