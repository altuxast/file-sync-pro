package main.java.com.filesyncpro.core;

import main.java.com.filesyncpro.model.FileMetadata;
import main.java.com.filesyncpro.util.HashUtil;

import java.io.InputStream;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class LocalFileProvider implements FileProvider {

    @Override
    public Map<String, FileMetadata> scan(String root) throws Exception {
        Map<String, FileMetadata> files = new HashMap<>();

        Files.walk(Paths.get(root)).forEach(path -> {
            try {
                boolean isDir = Files.isDirectory(path);
                long modified = Files.getLastModifiedTime(path).toMillis();
                String hash = isDir ? null : HashUtil.hashFile(path, "SHA-256");

                files.put(path.toString(), new FileMetadata(path, modified, hash, isDir));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return files;
    }

    @Override
    public boolean exists(String path) {
        return Files.exists(Paths.get(path));
    }

    @Override
    public InputStream read(String path) throws Exception {
        return Files.newInputStream(Paths.get(path));
    }

    @Override
    public void write(String path, InputStream data) throws Exception {
        Path p = Paths.get(path);
        if (p.getParent() != null) {
            Files.createDirectories(p.getParent());
        }
        Files.copy(data, p, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void createDirectory(String path) throws Exception {
        Files.createDirectories(Paths.get(path));
    }

    @Override
    public void delete(String path) throws Exception {
        Files.delete(Paths.get(path));
    }

    @Override
    public long getLastModified(String path) throws Exception {
        return Files.getLastModifiedTime(Paths.get(path)).toMillis();
    }

    @Override
    public boolean isDirectory(String path) {
        return Files.isDirectory(Paths.get(path));
    }
}
