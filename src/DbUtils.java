import java.sql.*;

class DbUtils {
    private static Connection con = null;
    //新規会員登録
    static boolean register(String name, String ps)throws FailedDatabaseAcceseException {
        int result = 0;

        try {
            con = DriverManager.getConnection("jdbc:mysql://118.27.28.109/chat_app", "chatapp", "chatapp_admin");
        }
        catch (SQLException e) {
            System.err.println(e.toString());
            throw new FailedDatabaseAcceseException(0);
        }
        //noinspection SqlDialectInspection,SqlNoDataSourceInspection
        String sql = "INSERT INTO user VALUES("+ name +","+ ps +");";
        try {
            Statement stm = con.createStatement();
            result = stm.executeUpdate(sql);
        }
        catch (SQLException err){
            System.err.println(err.toString());
            throw new FailedDatabaseAcceseException(1);
        }
        return result == 1;
    }

    //ログインのためのDBからのでーた取得
    static boolean migrateData(String id,String ps) throws FailedDatabaseAcceseException {
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:mysql://118.27.28.109/chat_app", "chatapp", "chatapp_admin");
        }
        catch (SQLException e) {
            System.err.println(e.toString());
            throw new FailedDatabaseAcceseException(0);
        }

        try {
            Statement stm = con.createStatement();
            //noinspection SqlDialectInspection,SqlNoDataSourceInspection
            String sql = "SELECT * FROM user WHERE user_id = " + id + "AND user_ps =" + ps + ";";
            rs = stm.executeQuery(sql);
            return rs.next();
        }
        catch(SQLException e) {
            System.err.println(e.toString());
            throw new FailedDatabaseAcceseException(1);
        }
    }

    //データベース内にある名前との比較
    static boolean sameNameCheck(String name) throws FailedDatabaseAcceseException {
        ResultSet rs = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://118.27.28.109/chat_app", "chatapp", "chatapp_admin");
        }
        catch (SQLException e) {
            System.err.println(e.toString());
            throw new FailedDatabaseAcceseException(0);
        }
        try {
            Statement stm = con.createStatement();
            //noinspection SqlDialectInspection,SqlNoDataSourceInspection
            String sql = "SELECT * FROM user WHERE user_name = " + name + ";";
            rs = stm.executeQuery(sql);
            return rs.next();
        }
        catch(SQLException e){
            System.err.println(e.toString());
            throw new FailedDatabaseAcceseException(1);
        }
    }
}
