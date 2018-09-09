package event;

public class MessageEventFactory {
    public static MessageEvent createMessageEvent(String msg) {
        if (msg.startsWith("/")) {
            String[] messages = msg.split(" ");
            if (messages.length > 1) {
                return new MessageEvent(messages[0], msg.replaceFirst(messages[0], ""));
            }
            return new MessageEvent(messages[0], "");
        }
        else {
            return new MessageEvent("/send", msg);
        }
    }
}
