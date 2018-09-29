package client;

public class FailedConnectToServerException extends Exception {
    private final String errMsg;
    private final String logMsg;
    private final Exception cause;

    FailedConnectToServerException(String errMsg, String logMsg, Exception cause) {
        this.errMsg = errMsg;
        this.logMsg = logMsg;
        this.cause = cause;
    }

    String getErrMsg() {
        return errMsg;
    }

    String getLogMsg() {
        return logMsg;
    }

    // getCauseって命名しようと思ったらThrowableで既に定義されてた・・・。
    Exception getCauseException() {
        return cause;
    }
}
