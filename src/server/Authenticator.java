package server;

import mysql.MySql;
import util.JLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

class Authenticator {
    private static final String SELECT_SQL = "SELECT user_name, now_login FROM users WHERE user_id = ? and password = ?";
    private static final String NOW_LOGIN_ON_SQL = "UPDATE users SET now_login = true WHERE user_name = ?";

    static Response authenticate(String id, String pw) {
        try (MySql mysql = new MySql()) {
            PreparedStatement statement = mysql.prepareStatement(SELECT_SQL, id, pw);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                if (result.getBoolean("now_login")) {
                    return new Response(Status.ALREADY, "");
                }
                // ログインできたのでユーザーのログイン状況をログイン中にする
                String userName = result.getString("user_name");
                mysql.prepareStatement(NOW_LOGIN_ON_SQL, userName).executeUpdate();
                return new Response(Status.AVAILABLE, userName);
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
