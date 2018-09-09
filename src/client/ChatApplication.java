package client;

import login.RegisterUser;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class ChatApplication {
    public static void main(String []args){
        int checkNumberOrString = 0;
        String loginOrRegister;
        int check = 0;

        do {
            System.out.println("ログインなら1\n新規登録なら2\n終了なら3を入力してください");
            loginOrRegister = new java.util.Scanner(System.in).nextLine();
            try {
                checkNumberOrString = Integer.parseInt(loginOrRegister);
                check = isEarnedNum(checkNumberOrString);
                if(check == 1){
                    ChatApplication.login();
                }
                else if(check == 2){
                    RegisterUser.registerInfo();
                }
                else if(check == 3){
                    System.out.println("...by...");
                }
            }
            catch (NumberFormatException e) {
                System.out.println("正しく入力してください。\n打てる文字は数字のみです。");
            }
        }while(check > 3||check < 1);
    }



    static void login(){
        Socket socket = connectServer(ClientConfiguration.getServerIpAddress(),ClientConfiguration.getServerPortNumber());

        try {
            String display = "";
            String buf = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            display = br.readLine();
            System.out.println(display);

            bw.write(buf);
            bw.newLine();
            bw.flush();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int isEarnedNum(int checkNumberOrString){
        if (checkNumberOrString == 1) {
            System.out.println("ログイン画面に遷移します");
            return 1;
        }
        if (checkNumberOrString == 2) {
            System.out.println("新規登録画面に遷移します");
            return 2;
        }
        if(checkNumberOrString == 3){
            System.out.println("チャットプログラムを終了します\nありがとうございました");
            return 3;
        }
        else {
            System.out.println("正しい数値を再度、入力してください");
            return 4;
        }
    }

    static Socket connectServer(String host, int port) {
        try {
            return new Socket(InetAddress.getByName(host), port);
        }
        catch (IOException err) {
            err.printStackTrace();
            throw new AssertionError(err);
        }
    }
}
