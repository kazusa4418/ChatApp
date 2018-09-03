public enum Command {
    EXIT,
    SEND_MESSAGE,
    ADD_ROOM,
    MAKE_ROOM,
    SHOW_ROOM,
    REMOVE_ROOM,
    KICK_USER,
    SHOW_MEMBER;

    static boolean isCommand(String msg) {
        String[] commands = { "/exit", "/show", "/add", "/remove" };

        for (String command : commands) {
            if (msg.startsWith(command)) {
                return true;
            }
        }
        return false;
    }
}