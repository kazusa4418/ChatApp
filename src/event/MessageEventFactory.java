package event;

public class MessageEventFactory {
    public static MessageEvent createMessageEvent(String msg) {
        if (msg.startsWith("/")) {
            String[] messages = msg.split(" ", 1);
            if (messages.length > 1) {
                return new MessageEvent(messages[0], messages[1]);
            }
            //TODO: この辺全般的に空白だと要素数0になる仕様をどうにかしたい
            return new MessageEvent(messages[0], " ");
        }
        else {
            return new MessageEvent("/send", msg);
        }
    }
}
