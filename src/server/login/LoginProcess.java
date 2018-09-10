package server.login;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class LoginProcess implements Runnable {
    private Socket socket;

    LoginProcess(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            ;
        }
        catch (IOException err) {

        }
    }
}
