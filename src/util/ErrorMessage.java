package util;

public class ErrorMessage {
    public static String failedAccessServer(String ip, int port) {
        return "fatal: can not access the server. ip-address.\n" +
                "ip-address: " + ip + ", port: " + port;
    }


}
