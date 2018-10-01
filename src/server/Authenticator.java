package server;

import mysql.MySql;
import util.DatabaseUtils;
import util.JLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

class Authenticator {
    private static final String SELECT_SQL = "SELECT user_name, now_login FROM users WHERE user_id = ? and password = ?";

    static Response authenticate(String id, String pw) {
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

enum Status {
    AVAILABLE,
    UNMATCHED,
    ALREADY,
    EXCEPTION
}

class Response {
    private Status status;
    private String userName;

    Response(Status status, String userName) {
        this.status = status;
        this.userName = userName;
    }

    Status getStatus() {
        return status;
    }

    String getUserName() {
        return userName;
    }
}
