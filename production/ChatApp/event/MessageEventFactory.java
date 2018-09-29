package event;

import server.Client;

public class MessageEventFactory {
    public static MessageEvent createMessageEvent(Client creator, String msg) {
        // TODO: 内緒話を特殊ケースから一般ケースにする(正規表現わかりにくいし条件文ゴミ過ぎるからどうにかしろ
        if (msg.split(" ")[0].matches("/>[\\w]+")) {
            return new MessageEvent(creator, Command.SECRET_MESSAGE, msg.replace("/>", ""));
        }
        else if (msg.startsWith("/")) {
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
