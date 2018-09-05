import javax.swing.text.Utilities;
import java.sql.*;

public class LoginUtils {
    static boolean check(int id,String ps){
        boolean che = DbUtils.migrateData(id,ps);
        if(che){
            return true;
        }
        return false;
    }
}