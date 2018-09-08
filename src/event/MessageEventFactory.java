package event;

public class MessageEventFactory {
     public static MessageEvent createMessageEvent(String msg) {
        // TODO: ここなんかダサい気がする。
        String firstMsg = msg.split(" ")[0];
        switch(firstMsg) {
            case "/show-rooms":
                return createShowEvent(msg);
            case "/join":
                return createJoinEvent(msg);
            case "/leave":
                return createLeaveEvent(msg);
            case "/make":
                return createMakeEvent(msg);
            case "/kick":
                return createKickEvent(msg);
            case "/show-members":
                return createMemberEvent(msg);
            default:
                return createSendMessageEvent(msg);
        }
    }
    // TODO: 全体的にコードがすっきりしていない気がする
    private static MessageEvent createExitEvent(String msg) {
        String[] msgs = msg.split(" ");
        if (msgs.length > 1) {
            return null;
        }

        return new MessageEvent(Command.EXIT, "");
    }

    private static MessageEvent createShowEvent(String msg) {
        String[] msgs = msg.split(" ");
        if (msgs.length > 1) {
            return null;
        }

        return new MessageEvent(Command.SHOW_ROOM, "");
    }

    private static MessageEvent createJoinEvent(String msg) {
        String[] msgs = msg.split(" ");
        if (msgs.length == 2) {
            return new MessageEvent(Command.JOIN_ROOM, msgs[1]);
        }
        else {
            return null;
        }
    }

    private static MessageEvent createLeaveEvent(String msg) {
         String[] msgs = msg.split(" ");
         if (msgs.length > 1) {
             return null;
         }

         return new MessageEvent(Command.LEAVE_ROOM, "");
    }

    private static MessageEvent createMakeEvent(String msg) {
        String[] msgs = msg.split(" ");
        if (msgs.length == 2) {
            return new MessageEvent(Command.MAKE_ROOM, msgs[1]);
        }
        else {
            return null;
        }
    }

    private static MessageEvent createKickEvent(String msg) {
        String[] msgs = msg.split(" ");
        if (msgs.length > 2) {
            return null;
        }

        return new MessageEvent(Command.KICK_MEMBER, msgs[1]);
    }

    private static MessageEvent createMemberEvent(String msg) {
        String[] msgs = msg.split(" ");

        // "/show-members"をリクエストした場合
        if (msgs.length == 1) {
            return new MessageEvent(Command.SHOW_MEMBER, "");
        }
        // "/show-members [room name]"をリクエストした場合
        else if (msgs.length == 2) {
            return new MessageEvent(Command.SHOW_MEMBER, msgs[1]);
        }
        else {
            return null;
        }
    }

    private static MessageEvent createSendMessageEvent(String msg) {
        return new MessageEvent(Command.SEND_MESSAGE, msg);
    }
}
