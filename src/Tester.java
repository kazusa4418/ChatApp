import java.io.IOException;

public class Tester {
    public static void main(String[] args) throws IOException {
        Runtime.getRuntime().exec(new String[] { "cmd.exe","/C","start","ping","-n","100" ,"192.168.1.1"});
    }
}
