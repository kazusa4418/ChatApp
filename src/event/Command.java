package event;

import util.JLogger;
import util.PropertyReader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public enum Command {
    LOGIN,
    LOGOUT,
    COMMAND_HELP,
    SEND_MESSAGE,
    JOIN_ROOM,
    LEAVE_ROOM,
    MAKE_ROOM,
    CLOSE_ROOM,
    SHOW_ROOMS,
    KICK_MEMBER,
    SHOW_MEMBERS,
    CHANGE_ADMIN,
    CHANGE_NAME,
    NOT_FOUND;

    public static Command get(String msg) {
        switch(msg) {
            case "/login":
                return LOGIN;
            case "/logout":
                return LOGOUT;
            case "/help":
                return COMMAND_HELP;
            case "/send":
                return SEND_MESSAGE;
            case "/join":
                return JOIN_ROOM;
            case "/leave":
                return LEAVE_ROOM;
            case "/make":
                return MAKE_ROOM;
            case "/close":
                return CLOSE_ROOM;
            case "/show-rooms":
                return SHOW_ROOMS;
            case "/kick":
                return KICK_MEMBER;
            case "/show-members":
                return SHOW_MEMBERS;
            case "/change-admin":
                return CHANGE_ADMIN;
            case "/name":
                return CHANGE_NAME;
            default:
                return NOT_FOUND;
        }
    }

    public static String usage(Command command) {
        return HelpDictionary.getUsageMessage(command);
    }

    public static String help(Command command) {
        return HelpDictionary.getHelpMessage(command);
    }

    public String getArgumentRegex() {
        switch (this) {
            case LOGOUT:
                return "";
            case COMMAND_HELP:
                return "[a-zA-Z0-9]+";
            case SEND_MESSAGE:
                return ".*";
            case JOIN_ROOM:
                return "[a-zA-Z0-9]+";
            case LEAVE_ROOM:
                return "";
            case MAKE_ROOM:
                return "[a-zA-Z0-9]+";
            case CLOSE_ROOM:
                return "";
            case SHOW_ROOMS:
                return "";
            case KICK_MEMBER:
                return "[a-zA-Z0-9-_]+";
            case CHANGE_ADMIN:
                return "[a-zA-Z0-9-_]+";
            case SHOW_MEMBERS:
                return ".{0}|[a-zA-Z0-9]+";
            case CHANGE_NAME:
                return "[a-zA-Z0-9-_]+";
            default:
                return "";
        }
    }
}

class HelpDictionary {
    private static final Map<Command, String> USAGE_DICTIONARY = new HashMap<>();
    private static final Map<Command, String> HELP_DICTIONARY = new HashMap<>();

    static {
        loadCommandUsageMap();
        loadCommandHelpMap();
    }

    private static void loadCommandUsageMap() {
        try (PropertyReader reader = new PropertyReader(new File("./help.properties"))) {
            reader.load();

            USAGE_DICTIONARY.put(Command.LOGOUT, reader.getProperty("logout.usage"));
            USAGE_DICTIONARY.put(Command.COMMAND_HELP, reader.getProperty("help.usage"));
            USAGE_DICTIONARY.put(Command.SEND_MESSAGE, reader.getProperty("send.usage"));
            USAGE_DICTIONARY.put(Command.JOIN_ROOM, reader.getProperty("join.usage"));
            USAGE_DICTIONARY.put(Command.LEAVE_ROOM, reader.getProperty("leave.usage"));
            USAGE_DICTIONARY.put(Command.MAKE_ROOM, reader.getProperty("make.usage"));
            USAGE_DICTIONARY.put(Command.CLOSE_ROOM, reader.getProperty("close.usage"));
            USAGE_DICTIONARY.put(Command.SHOW_ROOMS, reader.getProperty("show-rooms.usage"));
            USAGE_DICTIONARY.put(Command.KICK_MEMBER, reader.getProperty("kick.usage"));
            USAGE_DICTIONARY.put(Command.CHANGE_ADMIN, reader.getProperty("change-admin.usage"));
            USAGE_DICTIONARY.put(Command.SHOW_MEMBERS, reader.getProperty("show-members.usage"));

        }
        catch (IOException err) {
            JLogger.log(Level.SEVERE, "can not read help.properties", err);

            // プロパティファイルが読み取れなかったらへリプメッセージには読み取れなかったことを書いておく
            for (Command command : Command.values()) {
                USAGE_DICTIONARY.put(command, "## fatal: can not read help message.");
            }
        }
    }

    private static void loadCommandHelpMap() {
        try (PropertyReader reader = new PropertyReader(new File("./help.properties"))) {
            reader.load();

            HELP_DICTIONARY.put(Command.LOGOUT, reader.getProperty("logout.help"));
            HELP_DICTIONARY.put(Command.COMMAND_HELP, reader.getProperty("help.help"));
            HELP_DICTIONARY.put(Command.SEND_MESSAGE, reader.getProperty("send.help"));
            HELP_DICTIONARY.put(Command.JOIN_ROOM, reader.getProperty("join.help"));
            HELP_DICTIONARY.put(Command.LEAVE_ROOM, reader.getProperty("leave.help"));
            HELP_DICTIONARY.put(Command.MAKE_ROOM, reader.getProperty("make.help"));
            HELP_DICTIONARY.put(Command.CLOSE_ROOM, reader.getProperty("close.help"));
            HELP_DICTIONARY.put(Command.SHOW_ROOMS, reader.getProperty("show-rooms.help"));
            HELP_DICTIONARY.put(Command.KICK_MEMBER, reader.getProperty("kick.help"));
            HELP_DICTIONARY.put(Command.CHANGE_ADMIN, reader.getProperty("change-admin.help"));
            HELP_DICTIONARY.put(Command.SHOW_MEMBERS, reader.getProperty("show-members.help"));
        }
        catch (IOException err) {
            JLogger.log(Level.SEVERE, "can not read help.properties", err);

            // プロパティファイルが読み取れなかったらヘルプメッセージには読み取れなかったことを書いておく
            for (Command command : Command.values()) {
                HELP_DICTIONARY.put(command, "## fatal: can not read help message.");
            }
        }
    }

    static String getUsageMessage(Command command) {
        return USAGE_DICTIONARY.get(command);
    }

    static String getHelpMessage(Command command) {
        return HELP_DICTIONARY.get(command);
    }
}