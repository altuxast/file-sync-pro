package main.java.com.filesyncpro.model;

import java.nio.file.Path;

public class FileMetadata {
    
    private Path path;
    private long lastModified;
    private String hash;
    private final boolean isDirectory;

    public FileMetadata(Path path, long lastModified, String hash, boolean isDirectory){
        this.path = path;
        this.lastModified = lastModified;
        this.hash = hash;
        this.isDirectory = isDirectory;
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

    public boolean isDirectory(){
        return isDirectory;
    }
}
