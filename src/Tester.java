import event.Command;
import mysql.MySql;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.ResultSet;

public class Tester {
    private Socket socket;
    private static BufferedWriter writer;

    public static void main(String[] args) {
        final String regex = "/>[a-zA-Z0-9-_]+";
        String message = "/>";

        System.out.println("");
        if (message.matches(regex)) {
            System.out.println("ok");
        }
        else {
            System.out.println("ng");
        }
    }
}
