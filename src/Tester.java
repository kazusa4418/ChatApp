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

    public static void main(String[] args) throws Exception {
        MySql mysql = new MySql();
        ResultSet result = mysql.executeQuery("SELECT * FROM user;");
        System.out.println(result);

    }
}
