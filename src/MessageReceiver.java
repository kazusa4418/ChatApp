import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MessageReceiver implements Runnable {
    private Socket socket;
    private BufferedReader reader;

    private Thread thread;

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
