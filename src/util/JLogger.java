package util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class JLogger {
    private static Logger logger = Logger.getLogger("chat_application");
    private static Logger error = Logger.getLogger("chat_application.error");

    static {
        initialize();
    }

    public static void log(Level level, String logMsg, Throwable throwable) {
        if (level == Level.SEVERE) {
            error.log(level, logMsg, throwable);
        }
        else {
            logger.log(level, logMsg, throwable);
        }
    }

    public static void finest(String logMsg) {
        logger.finest(logMsg);
    }

    public static void finer(String logMsg) {
        logger.finer(logMsg);
    }

    public static void fine(String logMsg) {
        logger.fine(logMsg);
    }

    public static void config(String logMsg) {
        logger.config(logMsg);
    }

    public static void info(String logMsg) {
        logger.info(logMsg);
    }

    public static void warning(String logMsg) {
        logger.warning(logMsg);
    }

    public static void severe(String logMsg) {
        error.severe(logMsg);
    }

    private static void initialize() {
        try {
            Handler handler = new FileHandler("./chat_application.log");
            Formatter formatter = new SimpleFormatter();
            handler.setFormatter(formatter);
            logger.addHandler(handler);

            handler = new FileHandler("./chat_application.error.log");
            formatter = new SimpleFormatter();
            handler.setFormatter(formatter);
            error.addHandler(handler);
        }
        catch (IOException err) {
            err.printStackTrace();
            System.exit(1);
        }
    }


}
