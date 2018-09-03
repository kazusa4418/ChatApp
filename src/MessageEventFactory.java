class MessageEventFactory {
     static MessageEvent createMessageEvent(Client client, String msg) {
        // TODO: ここなんかダサい気がする。
        String firstMsg = msg.split(" ")[0];
        switch(firstMsg) {
            case "/exit":
                return createExitEvent(client, msg);
            case "/show":
                return createShowEvent(client, msg);
            case "/add":
                return createAddEvent(client, msg);
            case "/make":
                return createMakeEvent(client, msg);
            case "/remove":
                return createRemoveEvent(client, msg);
            case "/kick":
                return createKickEvent(client, msg);
            case "/member":
                return createMemberEvent(client, msg);
            default:
                return createSendMessageEvent(client, msg);
        }
    }
    // TODO: 全体的にコードがすっきりしていない気がする
    private static MessageEvent createExitEvent(Client client, String msg) {
        String[] msgs = msg.split(" ");
        if (msgs.length > 1) {
            System.err.println("unknown option `" + msgs[1] + "`");
            return null;
        }

        return new MessageEvent(client, Command.EXIT, "");
    }

    private static MessageEvent createShowEvent(Client client, String msg) {
        String[] msgs = msg.split(" ");
        if (msgs.length > 1) {
            System.err.println("unknown option `" + msgs[1] + "`");
            return null;
        }

        return new MessageEvent(client, Command.SHOW_ROOM, "");
    }

    private static MessageEvent createAddEvent(Client client, String msg) {
        String[] msgs = msg.split(" ");
        if (msgs.length > 2) {
            System.err.println("unknown option `" + msgs[2] + "`");
            return null;
        }

        return new MessageEvent(client, Command.ADD_ROOM, msgs[1]);
    }

    private static MessageEvent createMakeEvent(Client client, String msg) {
         String[] msgs = msg.split(" ");
         if (msgs.length > 2) {
             System.err.println("unknown option `" + msgs[2] + "`");
             return null;
         }

         return new MessageEvent(client, Command.MAKE_ROOM, msgs[1]);
    }

    private static MessageEvent createRemoveEvent(Client client, String msg) {
        String[] msgs = msg.split(" ");
        if (msgs.length > 1) {
            System.err.println("unknown option `" + msgs[1] + "`");
            return null;
        }

        return new MessageEvent(client, Command.REMOVE_ROOM, "");
    }

    private static MessageEvent createKickEvent(Client client, String msg) {
        String[] msgs = msg.split(" ");
        if (msgs.length > 2) {
            System.err.println("unknown option `" + msgs[2] + "`");
            return null;
        }

        return new MessageEvent(client, Command.KICK_USER, msgs[1]);
    }

    private static MessageEvent createMemberEvent(Client client, String msg) {
        String[] msgs = msg.split(" ");
        if (msgs.length > 1) {
            System.err.println("unknown option `" + msgs[1] + "`");
            return null;
        }

        return new MessageEvent(client, Command.SHOW_MEMBER, "");
    }

    private static MessageEvent createSendMessageEvent(Client client, String msg) {
        return new MessageEvent(client, Command.SEND_MESSAGE, msg);
    }
}
