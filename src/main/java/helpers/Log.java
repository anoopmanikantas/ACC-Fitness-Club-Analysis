package helpers;

public class Log {

    public static void info(String message) {
        java.util.logging.Logger.getLogger(Log.class.getName()).info(message);
    }

    public static void error(String error) {
        java.util.logging.Logger.getLogger(Log.class.getName()).severe(error);
    }
}
