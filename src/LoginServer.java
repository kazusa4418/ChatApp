import exception.FailedDatabaseAcceseException;
import login.DbUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.System.exit;

class LoginServer {
    private ServerSocket server;

    LoginServer() {
        try {
            // ポートを指定してServerSocketを立てる
            server = new ServerSocket(33332);
        }
        catch (IOException err) {
            err.printStackTrace();
            throw new AssertionError(err);
        }
    }

    void run() {
        while (!server.isClosed()) {
            try {
                // クライアントからのアクセスを待つ
                Socket socket = server.accept();
                boolean logincheck = false;
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                do {
                    bw.write("名前を入力してください");
                    bw.newLine();
                    bw.flush();

                    String name = br.readLine();

                    bw.write("パスワードを入力してください");
                    bw.newLine();
                    bw.flush();

                    String password = br.readLine();

                    try {
                        logincheck = DbUtils.migrateData(name, password);
                    } catch (FailedDatabaseAcceseException e) {
                        int errorId = e.getErrorId();
                        String errorMsg = e.getErrorMsg(errorId);
                        System.out.println(errorMsg);
                        if (errorId == 1) {
                            exit(1);
                        }
                    }
                    if(!logincheck){
                        System.out.println("もう一度入力してください");
                    }
                }while(logincheck);
            }
            catch (IOException err) {
                err.printStackTrace();
                throw new AssertionError(err);
            }
        }
    }
}