import java.io.*;
import java.net.Socket;

class MessageSender implements Runnable {
    private BufferedWriter writer;

    private Thread thread;

    MessageSender(Socket socket) {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }
        catch (IOException err) {
            throw new AssertionError(err);
        }

        thread = new Thread(this);
    }

    void start() {
        thread.start();
    }

    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.print(">>> ");
                String msg = reader.readLine();
                send(msg);
            }
        }
        catch (IOException err) {
            err.printStackTrace();
            throw new AssertionError(err);
        }
    }

    private void send(String msg) {
        try {
            writer.write(msg);
            writer.newLine();
            writer.flush();
            System.out.println(msg);
        }
        catch (IOException err) {
            err.printStackTrace();
            throw new AssertionError(err);
        }
    }
}
