package login;

import java.util.Scanner;
import static java.lang.System.exit;
import exception.*;

public class RegisterUser {

    public static boolean registerInfo(){
        boolean result = false;
        String name;
        String ps;
        //boolean checkWordNum;

        System.out.println("会員登録を行います\n下記に必要事項を入力してください");
        do {
            name = inputNameOrPassword("氏名");
            try {
                result = DbUtils.sameNameCheck(name);
            }
            catch (FailedDatabaseAcceseException e) {
                int errorId = e.getErrorId();
                String errorMsg = e.getErrorMsg(errorId);
                System.out.println(errorId);
                System.out.println(errorMsg);
                e.printStackTrace();
                if(errorId == 1) {
                    exit(1);
                }
            }
            if (result) {
                System.out.println("すでに同一の氏名が存在します。もう一度やり直してください");
            }
        } while(result);

        do {
            ps = inputNameOrPassword("パスワード");
            try {
                result = DbUtils.register(name, ps);
            } catch (FailedDatabaseAcceseException e) {
                System.out.println("重大な問題が発生しました/プログラムを終了します");
                exit(1);
            }

            if (result) {
                System.out.println("登録は終了しました");
            } else {
                System.out.println("失敗しました。もう一度やり直してください");
            }
        }while(!result);
        return true;
    }

    private static boolean countWord(String countWord){
        return countWord.matches("[a-zA-Z0-9-_]{1,33}");
    }

    private static String inputNameOrPassword(String variableName){
        Scanner sr = new Scanner(System.in);
        String inputNameOrPassword;
        boolean checkWordNum;

        do {
            System.out.print("1文字以上32文字以下,使える文字は「a-z,A-Z,0-1,-,_」で" + variableName + "入力してください\n>");
            inputNameOrPassword = sr.nextLine();
            checkWordNum = countWord(inputNameOrPassword);
        }while(!checkWordNum);
        return inputNameOrPassword;
    }
}