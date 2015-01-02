package agilemods.bot.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogHandler {

    private static Logger log = LoggerFactory.getLogger("AgileBot");

    public static void info(String msg) {
        log.info(msg);
    }

    public static void warn(String msg) {
        log.warn(msg);
    }
}
