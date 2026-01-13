package main.java.com.filesyncpro.core;

import main.java.com.filesyncpro.model.FileMetadata;

import java.io.InputStream;
import java.util.Map;

public interface FileProvider {
    Map<String, FileMetadata> scan(String root) throws Exception;

    boolean exists(String path) throws Exception;

    InputStream read(String path) throws Exception;

    void write(String path, InputStream data) throws Exception;

    void createDirectory(String path) throws Exception;

    void delete(String path) throws Exception;

    long getLastModified(String path) throws Exception;

    boolean isDirectory(String path) throws Exception;
}
