package util;

import java.io.IOException;

public class Console {
    /* =============== SINGLETON =============== */
    private static final Console instance = new Console();

    public static Console getInstance() {
        return instance;
    }
    /* ========================================= */

    private static final java.io.Console console = System.console();

    private Console() {
        try {
            new ProcessBuilder("chcp", "65001").inheritIO().start().waitFor();
        }
        catch (IOException | InterruptedException ignore) {
            // TODO: いつか実装する・・・。
        }
    }

    public String readLine() {
        return console.readLine();
    }

    public String readLine(String msg) {
        return console.readLine(msg);
    }

    public String readPassword() {
        return String.valueOf(console.readPassword());
    }

    public String readPassword(String msg) {
        return String.valueOf(console.readPassword(msg));
    }

    public void print(Object obj) {
        console.writer().print(obj);
    }

    public void println(Object obj) {
        console.writer().println(obj);
    }

    public void pleaseEnter() {
        console.readPassword();
    }

    public void pleaseEnter(String msg) {
        console.readPassword(msg + "");
    }

    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException ignore) {
            // TODO: 後で実装する
        }
    }
    public void clear() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }
        catch (IOException | InterruptedException ignore) {
            // TODO: 後で実装する
        }
    }
}
