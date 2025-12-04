package main.java.com.filesyncpro.util;

import java.nio.file.*;
import java.time.LocalDateTime;

public class LogUtil {
    
    private static final Path LOG_FILE = Paths.get("filesync.log");

    public static void log(String message){
        String logEntry = "[" + LocalDateTime.now() + "] " + message + "\n";

        try {
            Files.write(LOG_FILE, logEntry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);            
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(message);
    }
}
