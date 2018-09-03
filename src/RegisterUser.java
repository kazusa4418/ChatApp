public class RegisterUser {
    private DbUtils dbMove = new DbUtils();

    public void registerInfo(){
        System.out.println("名前を入力してください ");
        String name = new java.util.Scanner(System.in).nextLine();
        System.out.println("パスワードを入力してください");
        String ps = new java.util.Scanner(System.in).nextLine();

        boolean result= dbMove.Register(name,ps);

        if(result){
            System.out.println("正常に終了しました");
        }else{
            System.out.println("失敗しました");
        }

    }

}
