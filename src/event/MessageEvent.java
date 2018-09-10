package event;

import server.Client;

import java.io.Serializable;

public class MessageEvent implements Serializable {
    private Client creator;
    private String command;
    private String body;

    MessageEvent(String command, String body) {
        this.command = command;
        this.body = body;
    }

    public Client getCreator() {
        return creator;
    }

    public void setCreator(Client client) {
        this.creator = client;
    }

    public String getCommand() {
        return command;
    }

    public String getBody() {
        return body;
    }
}
