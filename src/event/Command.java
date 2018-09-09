package event;

import util.PropertyReader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public enum Command {
    LOGOUT,
    COMMAND_HELP,
    SEND_MESSAGE,
    JOIN_ROOM,
    LEAVE_ROOM,
    MAKE_ROOM,
    SHOW_ROOM,
    KICK_MEMBER,
    SHOW_MEMBER;

    public static String usage(Command command) {
        return HelpDictionary.getUsageMessage(command);
    }

    public static String help(Command command) {
        return HelpDictionary.getHelpMessage(command);
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
            USAGE_DICTIONARY.put(Command.SHOW_ROOM, reader.getProperty("show-rooms.usage"));
            USAGE_DICTIONARY.put(Command.KICK_MEMBER, reader.getProperty("kick.usage"));
            USAGE_DICTIONARY.put(Command.SHOW_MEMBER, reader.getProperty("show-members.usage"));

        }
        catch (IOException err) {
            // TODO: エラーログに出力するようにする

            // プロパティファイルが読み取れなかったらへリプメッセージには読み取れなかったことを書いておく
            for (Command command : Command.values()) {
                USAGE_DICTIONARY.put(command, "can not read help.properties");
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
            HELP_DICTIONARY.put(Command.SHOW_ROOM, reader.getProperty("show-rooms.help"));
            HELP_DICTIONARY.put(Command.KICK_MEMBER, reader.getProperty("kick.help"));
            HELP_DICTIONARY.put(Command.SHOW_MEMBER, reader.getProperty("show-members.help"));
        }
        catch (IOException err) {
            // TODO: エラーログに出力するようにする

            // プロパティファイルが読み取れなかったらヘルプメッセージには読み取れなかったことを書いておく
            for (Command command : Command.values()) {
                HELP_DICTIONARY.put(command, "can not read help.properties");
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