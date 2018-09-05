import java.sql.*;

class DbUtils {
    private static Connection con = null;
    //新規会員登録
     static boolean register(String name, String ps) {
        int result=0;

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "");
            //System.out.println("MySQLに接続できました。");
        }
        catch (SQLException e) {
            //System.err.println("MySQLに接続できませんでした。");
            System.err.println(e.toString());
        }
        //noinspection SqlDialectInspection,SqlNoDataSourceInspection
        String sql = "INSERT INTO user VALUES(name,ps)";
        try {
            Statement stm = con.createStatement();
            result = stm.executeUpdate(sql);
        }
        catch (SQLException err){
            System.err.println(err.toString());
        }
         return result == 1;
    }

    //ログインのためのDBからのでーた取得
    static boolean migrateData(int id,String ps){
         ResultSet rs = null;

         try {
             con = DriverManager.getConnection("jdbc:mysql://mysql133.phy.lolipop.lan:2222/LAA0956277-chatapp", "LAA0956277", "pet00004418");
             //System.out.println("MySQLに接続できました。");
         }
         catch (SQLException e) {
            //System.out.println("MySQLに接続できませんでした。");
            System.err.println(e.toString());
         }
         try {
             Statement stm = con.createStatement();
             //noinspection SqlDialectInspection,SqlNoDataSourceInspection
             String sql = "SELECT * FROM user WHERE user_id = " + id + ";";
             rs = stm.executeQuery(sql);
             if(!rs.next()){
                 return false;
             }
             //noinspection SqlDialectInspection,SqlNoDataSourceInspection
             sql = "SELECT * FROM user WHERE + user_pw = " + ps + ";";
             rs = stm.executeQuery(sql);
             return rs.next();
         }
         catch(SQLException e){
             System.err.println(e.toString());
         }
         return false;
    }
}

