import java.sql.*;

public class LoginUtils {
    static boolean check(int id,String ps) {
        Connection con = null;
        //ResultSet rs = null;
        int [] DbUserId = {1,2,3};
        String [] DbUserPs = {"123","234","345"};

        /*try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "");
            System.out.println("MySQLに接続できました。");
        } catch (SQLException e) {
            System.out.println("MySQLに接続できませんでした。");
        }

        try {
            Statement stm = con.createStatement();
            String sql = "select * from user";
            rs = stm.executeQuery(sql);
        }
        catch(SQLException e){
            System.out.println(e);
        }
        try {
            while (rs.next()) {

                DbUserId = rs.getInt("user_id");
                DbUserPs = rs.getString("user_ps");
            }
        }
        catch(SQLException err){

            System.out.println(err);
        }
        */
        for(int i = 0; i < DbUserId.length ;i++) {
            if (id == DbUserId[i] && ps.equals(DbUserPs[i])) {
                return true;
            }
        }
        return false;
    }
}











