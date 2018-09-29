package event;

import server.Client;

import java.io.Serializable;

public class MessageEvent {
    private Client creator;
    private Command command;
    private String body;

    MessageEvent(Client creator, Command command, String body) {
        this.creator = creator;
        this.command = command;
        this.body = body;
    }

    public Client getCreator() {
        return creator;
    }

    public Command getCommand() {
        return command;
    }

    public String getBody() {
        return body;
    }
}
