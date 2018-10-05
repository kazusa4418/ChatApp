import event.Command;
import mysql.MySql;
import util.ThreadUtils;

import javax.print.attribute.standard.MediaSize;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.Scanner;

public class Tester {
    public static void main(String[] args) throws IOException {
        File file = new File("./file.txt");

        if (!file.exists()) {
            file.createNewFile();
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(file));

        try {
            bw.write("unko");
            bw.flush();
        }
        finally {
            bw.close();
        }
    }
}
