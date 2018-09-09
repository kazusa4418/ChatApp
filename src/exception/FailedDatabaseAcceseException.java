package exception;

public class FailedDatabaseAcceseException extends Throwable {
    private int errorId;
    private String errorMsg;

    public FailedDatabaseAcceseException(int errorId){
        this.errorId = errorId;

    }

    public int getErrorId(){
        return errorId;
    }

    public String getErrorMsg(int errorId){
        if(errorId == 0){
            errorMsg = "接続に失敗しました";
        }
        if(errorId == 1){
            errorMsg = "重大な問題が発生しました。プログラムを終了します";
        }
        return errorMsg;
    }
}
