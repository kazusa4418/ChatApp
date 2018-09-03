import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class MessageReceiver implements Runnable {
    private Socket socket;
    private BufferedReader reader;

    private Thread thread;

    public static void main(String... args) throws IOException {
        System.out.println("out");
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/C", "start");
        Process process = builder.start();
        PrintStream ps = new PrintStream(process.getOutputStream());
        ps.println("hello");
        try {
            Thread.sleep(10);
        }
        catch (InterruptedException err) {
            err.printStackTrace();
        }
    }
    MessageReceiver(Socket socket) {
        try {
            this.socket = socket;
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException err) {
            err.printStackTrace();
            throw new AssertionError(err);
        }

        thread = new Thread(this);
    }

    void start() {
        thread.start();
    }

    public void run() {
        try {
            while (!socket.isClosed()) {
                String msg = reader.readLine();

                if (msg != null) {
                    System.out.println(msg);
                }
            }
        }
        catch (IOException err) {
            err.printStackTrace();
            throw new AssertionError(err);
        }
    }
}
