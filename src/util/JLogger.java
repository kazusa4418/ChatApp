package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@SuppressWarnings("unused")
public class JLogger {
    private static final LoggerList loggers = new LoggerList();

    private static Logger getLogger() {
        String className = ThreadUtils.getClassNameCalledThis(2);

        if (!loggers.existsLogger(className)) {
            loggers.createNewLogger(className);
        }
        return loggers.getLogger(className);
    }

    private static Logger getErrorLogger() {
        String className = ThreadUtils.getClassNameCalledThis(2);

        if (!loggers.existsErrorLogger(className)) {
            loggers.createNewErrorLogger(className);
        }
        return loggers.getErrorLogger(className);
    }

    public static void log(Level level, String logMsg, Throwable throwable) {
        if (level == Level.SEVERE) {
            getErrorLogger().log(level, logMsg, throwable);
        }
        else {
            getLogger().log(level, logMsg, throwable);
        }
    }

    public static void finest(String logMsg) {
        getLogger().finest(logMsg);
    }

    public static void finer(String logMsg) {
        getLogger().finer(logMsg);
    }

    public static void fine(String logMsg) {
        getLogger().fine(logMsg);
    }

    public static void config(String logMsg) {
        getLogger().config(logMsg);
    }

    public static void info(String logMsg) {
        getLogger().info(logMsg);
    }

    public static void warning(String logMsg) {
        getLogger().warning(logMsg);
    }

    public static void severe(String logMsg) {
        getErrorLogger().severe(logMsg);
    }
}

class LoggerList {
    private final List<Logger> loggers = new ArrayList<>();
    private final List<Logger> errLoggers = new ArrayList<>();

    Logger getLogger(String loggerName) {
        for (Logger logger : loggers) {
            if (exists(loggerName, loggers)) {
                return logger;
            }
        }
        return null;
    }

    Logger getErrorLogger(String loggerName) {
        for (Logger logger : errLoggers) {
            if (exists(loggerName, errLoggers)) {
                return logger;
            }
        }
        return null;
    }

    boolean existsLogger(String loggerName) {
        return exists(loggerName, loggers);
    }

    boolean existsErrorLogger(String loggerName) {
        return exists(loggerName, errLoggers);
    }

    private boolean exists(String loggerName, List<Logger> loggers) {
        for (Logger logger : loggers) {
            if (logger.getName().equals(loggerName)) {
                return true;
            }
        }
        return false;
    }

    void createNewLogger(String loggerName) {
        Logger logger = Logger.getLogger(loggerName);

        Handler handler = null;
        try {
            handler = new FileHandler("./log/" + loggerName + ".log");
        }
        catch (IOException err) {
            err.printStackTrace();
            System.exit(1);
        }
        Formatter formatter = new SimpleFormatter();
        handler.setFormatter(formatter);
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
        loggers.add(logger);
    }

    void createNewErrorLogger(String loggerName) {
        Logger logger = Logger.getLogger(loggerName);

        Handler handler = null;
        try {
            handler = new FileHandler("./log/" + loggerName + ".err.log");
        }
        catch (IOException err) {
            err.printStackTrace();
            System.exit(1);
        }

        Formatter formatter = new SimpleFormatter();
        handler.setFormatter(formatter);
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
        errLoggers.add(logger);
    }
}
