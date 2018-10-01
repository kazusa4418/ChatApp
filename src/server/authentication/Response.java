package server.authentication;

public class Response {
    private Status status;
    private String userName;

    Response(Status status, String userName) {
        this.status = status;
        this.userName = userName;
    }

    public Status getStatus() {
        return status;
    }

    public String getUserName() {
        return userName;
    }
}
