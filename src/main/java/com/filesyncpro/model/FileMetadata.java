package main.java.com.filesyncpro.model;

import java.nio.file.Path;

public class FileMetadata {
    
    private Path path;
    private long lastModified;
    private String hash;

    public FileMetadata(Path path, long lastModified, String hash){
        this.path = path;
        this.lastModified = lastModified;
        this.hash = hash;
    }

    public Path getPath(){
        return path;
    }

    public long getLastModified(){
        return lastModified;
    }

    public String getHash(){
        return hash;
    }
}
