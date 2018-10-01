package server.authentication;

import mysql.MySql;
import util.JLogger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class Authenticator {
    private static final String SELECT_SQL = "SELECT user_name, now_login FROM users WHERE user_id = ? and password = ?";

    public static Response authenticate(String id, String pw) {
        try (MySql mysql = new MySql()) {
            ResultSet result = mysql.prepareStatement(SELECT_SQL).set(id).set(pw).executeQuery();

            if (result.next()) {
                if (result.getBoolean("now_login")) {
                    return new Response(Status.ALREADY, "");
                }

                return new Response(Status.AVAILABLE, result.getString("user_name"));
            }
            else {
                return new Response(Status.UNMATCHED, "");
            }
        }
        catch (SQLException err) {
            JLogger.log(Level.SEVERE, "failed to access to database.", err);
            return new Response(Status.EXCEPTION, "");
        }
    }
}

