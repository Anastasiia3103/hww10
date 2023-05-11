package hw10;

import java.io.File;

public class Main {
    public static void main (String[] args) {
        String configFilePath = "config.txt";
        File configFile = new File (configFilePath);
        File logFile = new File ("log.txt");
        LoggingLevel loggingLevel = LoggingLevel.DEBUG;
        long maxFileSize = 1000;
        String logFormat = "yyyy-MM-dd HH:mm:ss";
        FileLoggerConfiguration configuration = new FileLoggerConfiguration (logFile,
                loggingLevel, maxFileSize, logFormat);
        FileLogger fileLogger = new FileLogger (configuration);
        try {
            fileLogger.debug ("This is a debug message.");
            fileLogger.info ("This is a info message.");
        } catch (FileMaxSizeReachedException e) {
            e.printStackTrace ();
        }
    }
}
