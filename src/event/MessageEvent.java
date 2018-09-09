package event;

import client.ChatClient;
import server.Client;

import java.io.Serializable;

public class MessageEvent implements Serializable {
    private Command command;
    private String body;
    private ChatClient chatClient;

    MessageEvent(Command command, String body, ChatClient chatClient) {
        this.command = command;
        this.body = body;
        this.chatClient = chatClient;
    }

    public ChatClient getChatClient() {
        return chatClient;
    }
/*
    public void setCreator(Client client) {
        this.creator = client;
    }
*/
    public Command getCommand() {
        return command;
    }

    public String getBody() {
        return body;
    }
}
