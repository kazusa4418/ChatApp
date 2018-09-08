package event;

import util.PropertyReader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public enum Command {
    EXIT,
    SEND_MESSAGE,
    JOIN_ROOM,
    LEAVE_ROOM,
    MAKE_ROOM,
    SHOW_ROOM,
    KICK_MEMBER,
    SHOW_MEMBER;

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

            USAGE_DICTIONARY.put(Command.EXIT, reader.getProperty("exit.usage"));
            USAGE_DICTIONARY.put(Command.SEND_MESSAGE, reader.getProperty("sendMessage.usage"));
            USAGE_DICTIONARY.put(Command.JOIN_ROOM, reader.getProperty("joinRoom.usage"));
            USAGE_DICTIONARY.put(Command.LEAVE_ROOM, reader.getProperty("leaveRoom.usage"));
            USAGE_DICTIONARY.put(Command.MAKE_ROOM, reader.getProperty("makeRoom.usage"));
            USAGE_DICTIONARY.put(Command.SHOW_ROOM, reader.getProperty("showRoom.usage"));
            USAGE_DICTIONARY.put(Command.KICK_MEMBER, reader.getProperty("kickMember.usage"));
            USAGE_DICTIONARY.put(Command.SHOW_MEMBER, reader.getProperty("showMember.usage"));

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

            HELP_DICTIONARY.put(Command.EXIT, reader.getProperty("exit.help"));
            HELP_DICTIONARY.put(Command.SEND_MESSAGE, reader.getProperty("sendMessage.help"));
            HELP_DICTIONARY.put(Command.JOIN_ROOM, reader.getProperty("joinRoom.help"));
            HELP_DICTIONARY.put(Command.LEAVE_ROOM, reader.getProperty("leaveRoom.help"));
            HELP_DICTIONARY.put(Command.MAKE_ROOM, reader.getProperty("makeRoom.help"));
            HELP_DICTIONARY.put(Command.SHOW_ROOM, reader.getProperty("showRoom.help"));
            HELP_DICTIONARY.put(Command.KICK_MEMBER, reader.getProperty("kickMember.help"));
            HELP_DICTIONARY.put(Command.SHOW_MEMBER, reader.getProperty("showMember.help"));
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