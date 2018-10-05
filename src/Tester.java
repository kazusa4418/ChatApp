import event.Command;
import mysql.MySql;
import util.ThreadUtils;

import javax.print.attribute.standard.MediaSize;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Tester {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "");

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM test");

        }
        catch (SQLException err) {
            err.printStackTrace();
        }
    }
}
