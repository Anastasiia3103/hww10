package hw10;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLogger {
    private FileLoggerConfiguration configuration;
    public FileLogger(FileLoggerConfiguration configuration){
        this.configuration = configuration;
    }
    public void debug(String message) throws FileMaxSizeReachedException{
        if (configuration.getLoggingLevel () != LoggingLevel.DEBUG){
            return;
        }
        log("DEBUG Message: " + message);
    }
    public void info(String message)throws FileMaxSizeReachedException{
        if (configuration.getLoggingLevel () != LoggingLevel.INFO){
            return;
        }
        log("INFO Message: " + message);
    }

    private void log (String logMessage) throws FileMaxSizeReachedException {
        File logFile = configuration.getLogFile ( );
        // Check if the log file exceeds the maximum file size
        if (logFile.length ( ) >= configuration.getMaxFileSize ( )) {
            throw new FileMaxSizeReachedException ("Maximum file size reached. Max size: " + configuration.getMaxFileSize ( )
                    + " bytes. Current size: " + logFile.length ( ) + " bytes. File path: " + logFile.getAbsolutePath ( ));
        }
        try (FileWriter writer = new FileWriter (logFile, true)){
            writer.write (getFormattedLogMessage(logMessage));
            writer.write (System.lineSeparator ());
        }
        catch (IOException e) {
            e.printStackTrace ();
        }
    }
    private void createNewLogFile() throws FileMaxSizeReachedException{
        File logFile = configuration.getLogFile ();
        String logFileName = logFile.getName ();
        String logDirectory = logFile.getParent ();
        LocalDateTime currentTime = LocalDateTime.now ();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm");
        String timestamp = currentTime.format(formatter);
        int dotIndex = logFileName.lastIndexOf ('.');
        String baseName = (dotIndex != -1) ? logFileName.substring (0, dotIndex) : logFileName;
        String extension = (dotIndex != -1) ? logFileName.substring (dotIndex) : " ";
        String newLogFileName = baseName + " " + timestamp + extension;
        File newLogFile = new File (logDirectory, newLogFileName);
        if (!newLogFile.exists ()){
            try {
                newLogFile.createNewFile ();
            } catch (IOException e){
                throw new FileMaxSizeReachedException ("Failed to create new log file: " +
                        newLogFile.getAbsolutePath());
            }
        }
        configuration = new FileLoggerConfiguration (newLogFile, configuration.getLoggingLevel(),
                configuration.getMaxFileSize (), configuration.getLogFormat());
    }
    private String getFormattedLogMessage(String message){
        LocalDateTime currentTime = LocalDateTime.now ();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern (configuration.getLogFormat ());
        String formattedTime = currentTime.format (formatter);
        return "[" + formattedTime + "]" + message;
    }
}

enum LoggingLevel {
    INFO,
    DEBUG
}
class FileMaxSizeReachedException extends Exception{
    public FileMaxSizeReachedException(String message) {
        super(message);
    }
}


