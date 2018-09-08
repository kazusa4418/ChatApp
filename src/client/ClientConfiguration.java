package client;

import util.PropertyReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ClientConfiguration {
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
            err.printStackTrace();
            System.exit(1);
        }
        catch (IOException err) {
            err.printStackTrace();
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
