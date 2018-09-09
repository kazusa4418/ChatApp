

public class ChatAppMain {
    public static void main(String []args) {
        LoginServer loginInterface = new LoginServer();
        loginInterface.run();
    }

    /*public static void main(String []args){
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
                login.RegisterUser.registerInfo();
            }
            else if(check == 3){
                System.out.println("チャットプログラムを終了します\nありがとうございました");
            }
        }
        catch (NumberFormatException e) {
            System.out.println("正しく入力してください。\n打てる文字は数字のみです。");
        }
    }while(check > 3||check < 1);

}*/

    //
    /*private static int isEarnedNum(int checkNumberOrString){
        if (checkNumberOrString == 1) {
            System.out.println("ログイン画面に遷移します");
            return 1;
        }
        if (checkNumberOrString == 2) {
            System.out.println("新規登録画面に遷移します");
            return 2;
        }
        else {
            System.out.println("正しい数値を再度、入力してください");
            return 4;
        }
    }
    */
}