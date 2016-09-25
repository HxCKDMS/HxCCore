package HxCKDMS.HxCCore.api.Utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

@SuppressWarnings({"unused", "WeakerAccess"})
public class LogHelper {
    public static void log(Level logLevel, Object object, String modName){
        LogManager.getLogger(modName).log(logLevel, object);
    }

    public static void all(Object object, String modName){
        log(Level.ALL, object, modName);
    }

    public static void debug(Object object, String modName){
        log(Level.DEBUG, object, modName);
    }

    public static void error(Object object, String modName){
        log(Level.ERROR, object, modName);
    }

    public static void fatal(Object object, String modName){
        log(Level.FATAL, object, modName);
    }

    public static void info(Object object, String modName){
        log(Level.INFO, object, modName);
    }

    public static void off(Object object, String modName){
        log(Level.OFF, object, modName);
    }

    public static void trace(Object object, String modName){
        log(Level.TRACE, object, modName);
    }

    public static void warn(Object object, String modName){
        log(Level.WARN, object, modName);
    }
}
