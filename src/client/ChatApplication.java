package client;

import login.RegisterUser;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import util.ClientDelivary;
import server.LoginStatus;

import static java.lang.System.exit;


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
                    ChatClient client = new ChatClient();
                    client.start();
                }
                else if(check == 2){
                    RegisterUser.registerInfo();
                    check = 5;
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



    static void login() {
        Socket socket = connectServer(ClientConfiguration.getServerIpAddress(), 33332);
        Scanner sr = new Scanner(System.in);
        ClientDelivary clientDelivary = new ClientDelivary();
        boolean flag = false;

        do {
            System.out.println("名前を入力してください");
            String name = sr.nextLine();
            System.out.println("パスワードを入力してください");
            String password = sr.nextLine();
            clientDelivary.set(name, password);
            ClientDelivary clientDelivary1 = new ClientDelivary();

            try {
                ObjectOutputStream transferData = new ObjectOutputStream(socket.getOutputStream());
                transferData.writeObject(clientDelivary);
                transferData.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                ObjectInputStream resiver = new ObjectInputStream(socket.getInputStream());
                LoginStatus lo = (LoginStatus) resiver.readObject();
                if (lo == LoginStatus.OK) {
                    System.out.println("成功しました");
                    flag = true;
                } else if (lo == LoginStatus.UNMATCHED) {
                    System.out.println("名前、パスワードの片方、または両方が違います。\nもう一度入力くしてください");
                    flag = false;
                } else if (lo == LoginStatus.EXCEPTION) {
                    System.out.println("エラーが発生しました\nプログラムを終了します");
                    exit(1);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }while(!flag);
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
