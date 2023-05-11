package hw10;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileLoggerConfigurationLoader {
    public static FileLoggerConfiguration load (File configFile) throws IOException {
        BufferedReader reader = new BufferedReader (new FileReader (configFile));
        File file = null;
        LoggingLevel level = null;
        long maxSize = 0;
        String format = null;
        String line;
        while ((line = reader.readLine ()) != null){
            if (line.startsWith ("FILE: ")){
                String filePath = line.substring ("FILE: ".length ()).trim ();
                file = new File (filePath);
            }
            else if (line.startsWith ("LEVEL:")) {
                String levelStr = line.substring ("LEVEL:".length ()).trim ();
                level = LoggingLevel.valueOf (levelStr);
            }
            else if (line.startsWith ("MAX-SIZE:")) {
                String maxSizeStr = line.substring ("MAX-SIZE:".length ()).trim ();
                maxSize = Long.parseLong (maxSizeStr);
            }
            else if (line.startsWith ("FORMAT:")) {
                format = line.substring ("FORMAT:".length ()).trim ();

            }
        }
        reader.close ();
        if (file == null || level == null || format == null) {
            throw new IllegalArgumentException ( "Invalid configuration file format: " + configFile.getPath ());
        }
        return new FileLoggerConfiguration (file, level, maxSize, format);
    }
}
