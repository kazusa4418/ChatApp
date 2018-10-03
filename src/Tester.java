import event.Command;
import mysql.MySql;
import util.ThreadUtils;

import javax.print.attribute.standard.MediaSize;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;

public class Tester {
    private Socket socket;
    private static BufferedWriter writer;

    public static void main(String[] args) throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "chcp", "1200").inheritIO().start().waitFor();

        System.out.print("⏎");
    }
}
