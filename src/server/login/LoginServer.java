package server.login;

import util.JLogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

public class LoginServer {
    private ServerSocket server;

    public LoginServer() {
        try {
            server = new ServerSocket(55555);
        }
        catch (IOException err) {
            JLogger.log(Level.SEVERE, "failed to build the server.", err);
            System.exit(1);
        }
    }

    public void start() {
    }

    public void run() {
        while (!server.isClosed()) {
            try {
                Socket socket = server.accept();
            }
            catch (IOException err) {
                JLogger.log(Level.WARNING, "failed to accept the request.", err);
            }
        }
    }
}
