import java.net.*;
import java.io.*;

public class ChatClient_old{

    public static void main(String[]args){
        new ClientCore().mainProc("127.0.0.1",10000);
    }
}

class ClientCore{
    private Socket socket;
    private ClientStream streamConsoleToSocket;
    private ClientStream streamSocketToConsole;

    void mainProc(String host,int port){

        try {
            socket = new Socket(InetAddress.getByName(host),port);
            streamConsoleToSocket = new ClientStream(socket);
			streamSocketToConsole = new ClientStream(socket);
			streamConsoleToSocket.start();
			streamSocketToConsole.start();

        }
        catch (IOException err) {
            System.out.println(err.toString());
        } 
    }
}

class ClientStream extends Thread {

    private BufferedReader br;
    private BufferedWriter bw;

    ClientStream(Socket socket) {
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException err) {
            err.printStackTrace();
            throw new AssertionError(err);
        }
    }

    public void run() {
        try {
            while (br.ready()) {
                String buf = br.readLine();
                bw.write(buf);
                bw.newLine();
                bw.flush();
            }
        } catch (IOException err) {
            System.out.println(err.toString());
        }
    }
}


