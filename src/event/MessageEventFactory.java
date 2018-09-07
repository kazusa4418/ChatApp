package event;

public class MessageEventFactory {
     public static MessageEvent createMessageEvent(String msg) {
        // TODO: ここなんかダサい気がする。
        String firstMsg = msg.split(" ")[0];
        switch(firstMsg) {
            case "/show":
                return createShowEvent(msg);
            case "/join":
                return createJoinEvent(msg);
            case "/leave":
                return createLeaveEvent(msg);
            case "/make":
                return createMakeEvent(msg);
            case "/kick":
                return createKickEvent(msg);
            case "/member":
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
        if (msgs.length > 2) {
            return null;
        }

        return new MessageEvent(Command.JOIN_ROOM, msgs[1]);
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
        if (msgs.length > 2) {
            return null;
        }

        return new MessageEvent(Command.MAKE_ROOM, msgs[1]);
    }

    private static MessageEvent createKickEvent(String msg) {
        String[] msgs = msg.split(" ");
        if (msgs.length > 2) {
            return null;
        }

        return new MessageEvent(Command.KICK_USER, msgs[1]);
    }

    private static MessageEvent createMemberEvent(String msg) {
        String[] msgs = msg.split(" ");
        if (msgs.length > 1) {
            return null;
        }

        return new MessageEvent(Command.SHOW_MEMBER, "");
    }

    private static MessageEvent createSendMessageEvent(String msg) {
        return new MessageEvent(Command.SEND_MESSAGE, msg);
    }
}
