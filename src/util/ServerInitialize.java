package util;

import mysql.MySql;

import java.sql.SQLException;
import java.util.logging.Level;

public class ServerInitialize {
    private static final String NOW_LOGIN_OFF_SQL = "UPDATE users SET now_login = false";

    public static void main(String... args) {
        try (MySql mysql = new MySql()) {
            mysql.executeUpdate(NOW_LOGIN_OFF_SQL);
        }
        catch (SQLException err) {
            JLogger.log(Level.SEVERE, "server initialize error", err);
        }
    }
}
