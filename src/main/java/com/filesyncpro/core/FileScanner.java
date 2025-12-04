package main.java.com.filesyncpro.core;

import main.java.com.filesyncpro.model.FileMetadata;
import main.java.com.filesyncpro.util.HashUtil;

import java.nio.file.*;
import java.util.*;

public class FileScanner {
    
    public Map<String, FileMetadata> scan(String directory){
        Map<String, FileMetadata> files = new HashMap<>();

        try {
            Files.walk(Paths.get(directory)).filter(Files::isRegularFile).forEach(path -> {
                try {
                    long modified = Files.getLastModifiedTime(path).toMillis();
                    String hash = HashUtil.hashFile(path, "SHA-256");
                    files.put(path.toString(), new FileMetadata(path, modified, hash));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return files;
    }

}
