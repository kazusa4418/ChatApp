public class RegisterUser {

    public static void registerInfo(){

        System.out.println("会員登録を行います\n下記に必要事項を入力してください");
        System.out.println("名前を入力してください ");
        String name = new java.util.Scanner(System.in).nextLine();
        System.out.println("パスワードを入力してください");
        String ps = new java.util.Scanner(System.in).nextLine();

        boolean result= DbUtils.register(name,ps);

        if(result){
            System.out.println("正常に終了しました");
        }else {
            System.out.println("失敗しました");
        }
    }
}
