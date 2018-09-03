import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class DbUtils {
     boolean Register(String name, String ps) {
        Connection con = null;
        int result=0;


         try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "");
            System.out.println("MySQLに接続できました。");
        } catch (SQLException e) {
            System.out.println("MySQLに接続できませんでした。");
        }
        try {
            Statement stm = con.createStatement();
            String sql = "INSERT INTO user VALUES('" + name + "','" + ps + "')";
            result = stm.executeUpdate(sql);
        }catch (SQLException err){
            System.out.println(err);
        }
         return result == 1;
    }
}
