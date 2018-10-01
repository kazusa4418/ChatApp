package util;

import mysql.MySql;
import server.Client;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseUtils {
    private static final String UPDATE_NOW_LOGIN = "UPDATE users SET now_login = ? WHERE user_name = ?";

    public static void updateNowLogin(Client client, boolean status) throws SQLException {
        executeUpdate(UPDATE_NOW_LOGIN, String.valueOf(status), client.getName());
    }

    public static void executeUpdate(String sql, String... args) throws SQLException {
        MySql mysql = new MySql();
        PreparedStatement statement = mysql.prepareStatement(sql);

        for (int i = 1; i <= args.length; i++ ) {
            statement.setString(i, args[i - 1]);
        }
        statement.executeUpdate();
    }
}
