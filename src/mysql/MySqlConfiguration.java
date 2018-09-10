package mysql;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

class MySqlConfiguration {
    private static String hostName;
    private static String userName;
    private static String password;
    private static String databaseName;
    private static String useSSL;
    private static String autoConnect;

    static {
        loadMysqlProperties();
    }

    private static void loadMysqlProperties() {
        File file = new File("./mysql.properties");

        try (PropertyReader reader = new PropertyReader(file)) {
            reader.load();
            hostName = reader.getProperty("hostName");
            userName = reader.getProperty("userName");
            password = reader.getProperty("password");
            databaseName = reader.getProperty("databaseName");
            useSSL = reader.getProperty("useSSL");
            autoConnect = reader.getProperty("autoConnect");
        }
        catch (FileNotFoundException err) {
            err.printStackTrace();
            System.err.println(file.getName() + "が見つかりません");
        }
        catch (IOException err) {
            err.printStackTrace();
            System.err.println("ファイルの読み込みに失敗しました");
        }
    }

    static String getHostName() {
        return hostName;
    }

    static String getUserName() {
        return userName;
    }

    static String getPassWord() {
        return password;
    }

    static String getDatabaseName() {
        return databaseName;
    }

    static String getUseSSL() {
        return useSSL;
    }

    static String getAutoConnect() {
        return autoConnect;
    }
}
