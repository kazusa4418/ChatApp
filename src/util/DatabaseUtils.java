package util;

import mysql.MySql;
import server.Client;

import java.sql.SQLException;

public class DatabaseUtils {
    private static final String UPDATE_NOW_LOGIN = "UPDATE users SET now_login = ? WHERE user_name = ?";

    public static void updateNowLogin(Client client, boolean status) throws SQLException {
        MySql mysql = new MySql();
        mysql.prepareStatement(UPDATE_NOW_LOGIN).set(status).set(client.getName()).executeUpdate();
    }
}
