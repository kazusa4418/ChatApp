import java.io.IOException;
import java.io.PrintStream;

public class Tester {
    public static void main(String[] args) {
        try {
            Thread.sleep(1000000000);
        }
        catch (InterruptedException err) {
            err.printStackTrace();
        }
    }
}