import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Tester {
    private Socket socket;
    private static BufferedWriter writer;

    public static void main(String[] args) throws IOException {
        String s = " ";

        String[] ss = s.split(" ");

        System.out.println(ss.length);
    }
}
