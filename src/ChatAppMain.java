public class ChatAppMain {

    public static void main(String []args){
        int checkNumberOrString = 0;
        String loginOrRegister;
        boolean isEarned;
        int check = 0;

        System.out.println("ログインなら1\n新規登録なら2を入力してください");
        do {
            loginOrRegister = new java.util.Scanner(System.in).nextLine();
            try {
                checkNumberOrString = Integer.parseInt(loginOrRegister);
                check = isEarnedNum(checkNumberOrString);
                if(check == 1){
                    ChatClient client = new ChatClient();
                    client.start();
                }
                else if(check == 2){
                    RegisterUser.registerInfo();
                }
            }
            catch (NumberFormatException e) {
                System.out.println("正しく入力してください。\n打てる文字は数字のみです。");
            }
        }while(check == 3);
    }

    //
    private static int isEarnedNum(int checkNumberOrString){
        if (checkNumberOrString == 1) {
            System.out.println("ログイン画面に遷移します");
            return 1;
            //ChatClient client = new ChatClient();
            //client.start();
        }
        if (checkNumberOrString == 2) {
            System.out.println("新規登録画面に遷移します");
            //RegisterUser.registerInfo();
            return 2;
        } else {
            System.out.println("正しい数値を再度、入力してください");
            return 3;
        }
    }
}


