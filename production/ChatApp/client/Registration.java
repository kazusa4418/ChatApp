package client;

import mysql.MySql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Registration {
    private static MySql mysql;

    private static final Scanner sc = new Scanner(System.in);

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

            mysql.prepareStatement(INSERT_SQL, id, pw, name).executeUpdate();

            System.out.println("会員登録しました。");
        }
        catch (SQLException err) {
            System.err.println("データベースサーバーへの接続に失敗しました。");
        }
    }

    private static String inputId() throws SQLException {
        while (true) {
            System.out.print("ユーザーIDを入力してください > ");
            String id = sc.nextLine();

            if (!id.matches(ID_REGEX)) {
                System.out.println("IDは英数字かﾊｲﾌﾝ、ｱﾝﾀﾞｰｽｺｱの4文字以上32文字以下である必要があります。");
                continue;
            }
            ResultSet result = mysql.prepareStatement(ID_EXIST_CHECK_SQL, id).executeQuery();
            if (result.next()) {
                System.out.println("そのIDは既に使われています。");
                continue;
            }
            return id;
        }
    }

    private static String inputPassword() {
        while (true) {
            System.out.print("パスワードを入力してください > ");
            String pw = sc.nextLine();

            if (!pw.matches(PW_REGEX)) {
                System.out.println("パスワードは英数字かﾊｲﾌﾝ、ｱﾝﾀﾞｰｽｺｱの4文字以上32文字以下である必要があります。");
                continue;
            }
            return pw;
        }
    }

    private static String inputUserName() throws SQLException {
        while (true) {
            System.out.print("ユーザー名を入力してください > ");
            String userName = sc.nextLine();

            if (!userName.matches(NAME_REGEX)) {
                System.out.println("ユーザー名は英数字かｱﾝﾀﾞｰｽｺｱの62文字以下である必要があります。");
                continue;
            }
            ResultSet result = mysql.prepareStatement(USER_NAME_EXIST_CHECK_SQL, userName).executeQuery();
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