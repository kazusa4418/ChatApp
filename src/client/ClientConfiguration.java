package client;

import util.Console;
import util.JLogger;
import util.PropertyReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;

class ClientConfiguration {
    private static String SERVER_IP_ADDRESS;
    private static int SERVER_PORT_NUMBER;

    static {
        loadServerNetworkInformation();
    }

    private static void loadServerNetworkInformation() {
        File propertyFile = new File("./server.properties");

        try (PropertyReader reader = new PropertyReader(propertyFile)) {
            reader.load();

            SERVER_IP_ADDRESS = reader.getProperty("ip_address");
            SERVER_PORT_NUMBER = reader.getIntProperty("port_number");
        }
        catch (FileNotFoundException err) {
            JLogger.log(Level.SEVERE, "the server.properties could not be found. path is './server.properties'", err);
            Console.getInstance().pleaseEnter("サーバーへのアクセス情報が見つかりませんでした。");
            System.exit(2);
        }
        catch (IOException err) {
            JLogger.log(Level.SEVERE, "the server.properties could not be loaded.", err);
            Console.getInstance().pleaseEnter("サーバーへのアクセス情報の読み込みに失敗しました。");
            System.exit(2);
        }
    }

    static String getServerIpAddress() {
        return SERVER_IP_ADDRESS;
    }

    static int getServerPortNumber() {
        return SERVER_PORT_NUMBER;
    }
}
