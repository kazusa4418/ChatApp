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
            name = inputNameOrPassword("name");
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
                System.out.println("すでに同一の名前が存在します。もう一度やり直してください");
            }
        } while(result);

        do {
            ps = inputNameOrPassword("password");
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
        return countWord.length() <= 32;
    }

    private static String inputNameOrPassword(String variableName){
        Scanner sr = new Scanner(System.in);
        String inputNameOrPassword;
        boolean checkWordNum;

        do {
            System.out.println("32文字以下で" + variableName + "入力してください");
            inputNameOrPassword = sr.nextLine();
            checkWordNum = countWord(inputNameOrPassword);
            if(!checkWordNum){
                System.out.println("もう一度入力してください");
            }
        }while(!checkWordNum);
        return inputNameOrPassword;
    }
}