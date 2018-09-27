package event;

import server.Client;

import java.io.Serializable;

public class MessageEvent implements Serializable {
    private Client creator;
    private Command command;
    private String body;

    MessageEvent(Command command, String body) {
        this.command = command;
        this.body = body;
    }

    public Client getCreator() {
        return creator;
    }

    public void setCreator(Client client) {
        this.creator = client;
    }

    public Command getCommand() {
        return command;
    }

    public String getBody() {
        return body;
    }
}
