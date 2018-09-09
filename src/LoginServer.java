import exception.FailedDatabaseAcceseException;
import login.DbUtils;
import server.LoginStatus;
import util.ClientDelivary;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


class LoginServer {
    private ServerSocket server;

    LoginServer() {
        try {
            // ポートを指定してServerSocketを立てる
            server = new ServerSocket(33332);
        } catch (IOException err) {
            err.printStackTrace();
            throw new AssertionError(err);
        }
    }

    void run() {
        LoginStatus status = LoginStatus.OK;
        while (!server.isClosed()) {
            try {
                // クライアントからのアクセスを待つ
                Socket socket = server.accept();

                boolean logincheck;
                String name = "";
                String password = "";
                ClientDelivary clientDelivary = new ClientDelivary();

                try {
                    ObjectInputStream reciver = new ObjectInputStream(socket.getInputStream());
                    clientDelivary = (ClientDelivary) reciver.readObject();
                    name = clientDelivary.getName();
                    password = clientDelivary.getPassWord();
                    logincheck = DbUtils.migrateData(name, password);
                    if(logincheck){
                        status = LoginStatus.OK;
                    }
                    else if(!logincheck){
                        status = LoginStatus.UNMATCHED;
                    }


                    ObjectOutputStream sender = new ObjectOutputStream(socket.getOutputStream());
                    sender.writeObject(status);
                    sender.flush();

                }
                catch (ClassNotFoundException e) {
                    e.getStackTrace();
                    status = LoginStatus.EXCEPTION;
                }
                catch (FailedDatabaseAcceseException e) {
                    int errorId = e.getErrorId();
                    String errorMsg = e.getErrorMsg(errorId);
                    System.out.println(errorMsg);
                    status = LoginStatus.EXCEPTION;
                    if (errorId == 1) {
                        //ここでログを発生、exit(1);
                    }
                }
            }
            catch (IOException err) {
                err.printStackTrace();
                status = LoginStatus.EXCEPTION;
            }
        }
    }
}