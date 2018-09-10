import exception.FailedDatabaseAcceseException;
import login.DbUtils;
import server.LoginStatus;
import util.ClientDelivary;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;

import util.JLogger;


class LoginServer implements Runnable {
    private ServerSocket server;
    private Thread thread;

    LoginServer() {
        try {
            // ポートを指定してServerSocketを立てる
            server = new ServerSocket(33332);
        }
        catch (IOException err) {
            err.printStackTrace();
            JLogger.log(Level.SEVERE,"エラー",err);
        }
        catch (IllegalArgumentException err){
            err.printStackTrace();
            JLogger.log(Level.INFO,"エラー",err);
        }
    }

    public void start(){
        thread = new Thread(this);
        thread.start();

    }

    public void run() {
        LoginStatus status = LoginStatus.OK;
        while (!server.isClosed()) {
            try {
                // クライアントからのアクセスを待つ
                Socket socket = server.accept();

                boolean logincheck;
                String name;
                String password;
                ClientDelivary clientDelivary;

                try {
                    ObjectInputStream reciver = new ObjectInputStream(socket.getInputStream());
                    clientDelivary = (ClientDelivary) reciver.readObject();
                    name = clientDelivary.getName();
                    password = clientDelivary.getPassWord();
                    logincheck = DbUtils.migrateData(name, password);
                    if(logincheck){
                        status = LoginStatus.OK;
                    }
                    else{
                        status = LoginStatus.UNMATCHED;
                    }

                    //ObjectOutputStream sender = new ObjectOutputStream(socket.getOutputStream());
                    //sender.writeObject(status);
                    //sender.flush();

                }
                catch (ClassNotFoundException e) {
                    e.getStackTrace();
                    status = LoginStatus.EXCEPTION;
                }
                catch (FailedDatabaseAcceseException e) {
                    int errorId = e.getErrorId();
                    e.printStackTrace();
                    String errorMsg = e.getErrorMsg(errorId);
                    System.out.println(errorMsg);
                    status = LoginStatus.EXCEPTION;
                    if (errorId == 1) {
                        //ここでログを発生、exit(1);
                    }
                }
                catch (SocketException err){
                    JLogger.log(Level.WARNING,"突如接続が切れたクライアントがいます",err);
                }
                ObjectOutputStream sender = new ObjectOutputStream(socket.getOutputStream());
                sender.writeObject(status);
                sender.flush();
            }
            catch (IOException err) {
                err.printStackTrace();
            }
        }
    }
}