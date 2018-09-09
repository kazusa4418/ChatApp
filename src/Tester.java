import event.Command;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Tester {
    private Socket socket;
    private static BufferedWriter writer;

    public static void main(String[] args) throws IOException {
        String room = "%dsf";

        if (room.matches(Command.MAKE_ROOM.getArgumentRegex())) {
            System.out.println(true);
        }
        else {
            System.out.println(false);
        }
    }
}
