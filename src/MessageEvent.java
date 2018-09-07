import java.io.Serializable;

class MessageEvent implements Serializable {
    private Client creator;
    private Command command;
    private String body;

    MessageEvent(Command command, String body) {
        this.command = command;
        this.body = body;
    }

    Client getCreator() {
        return creator;
    }

    void setCreator(Client client) {
        this.creator = client;
    }

    Command getCommand() {
        return command;
    }

    String getBody() {
        return body;
    }
}
