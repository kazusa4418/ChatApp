package client;

import mysql.MySql;
import util.Console;
import util.JLogger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class Registration {
    private static MySql mysql;

    private static final Console console = Console.getInstance();

    private static final String INSERT_SQL = "INSERT INTO users VALUES(?, ?, ?, false)";

    private static final String ID_REGEX = "[a-zA-Z0-9-_]{6,33}";
    private static final String PW_REGEX = "[a-zA-Z0-9-_]{4,33}";
    private static final String NAME_REGEX = "[\\w]{1,65}";

    private static final String ID_EXIST_CHECK_SQL = "SELECT * FROM users WHERE user_id = ?";
    private static final String USER_NAME_EXIST_CHECK_SQL = "SELECT * FROM users WHERE user_name = ?";

    public static void main(String... args) {
        System.out.println("新規会員登録をします。\n");

        try {
            openConnection();

            String id = inputId();
            String pw = inputPassword();
            String name = inputUserName();

            mysql.prepareStatement(INSERT_SQL).set(id).set(pw).set(name).executeUpdate();

            Console.getInstance().pleaseEnter("会員登録しました。");
        }
        catch (SQLException err) {
            JLogger.log(Level.SEVERE, "failed to register", err);
            Console.getInstance().pleaseEnter("データベースサーバーへの接続に失敗しました。");
        }
    }

    private static String inputId() throws SQLException {
        while (true) {
            String id = console.readLine("ユーザーIDを入力してください > ");

            if (!id.matches(ID_REGEX)) {
                System.out.println("IDは英数字かﾊｲﾌﾝ、ｱﾝﾀﾞｰｽｺｱの6文字以上32文字以下である必要があります。");
                continue;
            }
            ResultSet result = mysql.prepareStatement(ID_EXIST_CHECK_SQL).set(id).executeQuery();
            if (result.next()) {
                System.out.println("そのIDは既に使われています。");
                continue;
            }
            return id;
        }
    }

    private static String inputPassword() {
        while (true) {
            String pw = console.readPassword("パスワードを入力してください > ");

            if (!pw.matches(PW_REGEX)) {
                System.out.println("パスワードは英数字かﾊｲﾌﾝ、ｱﾝﾀﾞｰｽｺｱの4文字以上32文字以下である必要があります。");
                continue;
            }
            return pw;
        }
    }

    private static String inputUserName() throws SQLException {
        while (true) {
            String userName = console.readLine("ユーザー名を入力してください > ");

            if (!userName.matches(NAME_REGEX)) {
                System.out.println("ユーザー名は英数字かｱﾝﾀﾞｰｽｺｱの64文字以下である必要があります。");
                continue;
            }
            ResultSet result = mysql.prepareStatement(USER_NAME_EXIST_CHECK_SQL).set(userName).executeQuery();
            if (result.next()) {
                System.out.println("そのユーザー名は既に使われています。");
                continue;
            }
            return userName;
        }
    }

    private static void openConnection() throws SQLException {
        mysql = new MySql();
    }
}
