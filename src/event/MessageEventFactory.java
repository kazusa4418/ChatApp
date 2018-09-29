package event;

import server.Client;

public class MessageEventFactory {
    public static MessageEvent createMessageEvent(Client creator, String msg) {
        if (msg.startsWith("/")) {
            String[] messages = msg.split(" ");
            if (messages.length > 1) {
                return new MessageEvent(creator, Command.get(messages[0]), msg.replaceFirst(messages[0], ""));
            }
            return new MessageEvent(creator, Command.get(messages[0]), "");
        }
        else {
            return new MessageEvent(creator, Command.SEND_MESSAGE, msg);
        }
    }
}
