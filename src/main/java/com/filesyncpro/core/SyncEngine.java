package main.java.com.filesyncpro.core;

import main.java.com.filesyncpro.config.Settings;
import main.java.com.filesyncpro.model.FileMetadata;
import main.java.com.filesyncpro.util.LogUtil;

import java.nio.file.*;
import java.util.Map;

public class SyncEngine {
    
    private final Settings settings;
    private final FileScanner scanner = new FileScanner();

    public SyncEngine(Settings settings){
        this.settings = settings;
    }

    public void runIncremental(){
        System.out.println("Running incremental sync...");
        sync(false);
    }

    public void runFullSync(){
        System.out.println("Running full sync...");
    }

    public void sync(boolean fullSync){
        Map<String, FileMetadata> src = scanner.scan(settings.getSource());
        Map<String, FileMetadata> dst = scanner.scan(settings.getDestination());

        src.forEach((path, meta) -> {
            String relative = path.substring(settings.getSource().length());
            Path destPath = Paths.get(settings.getDestination() + relative);
            try {
                if(!dst.containsKey(destPath.toString())){
                    Files.createDirectories(destPath.getParent());
                    Files.copy(meta.getPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
                    LogUtil.log("Copied new file: " + relative);
                }else if(!meta.getHash().equals(dst.get(destPath.toString()).getHash())){
                    Files.copy(meta.getPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
                    LogUtil.log("Updated modified file: " + relative);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        if(fullSync) {
            dst.forEach((destPath, meta) -> {
                String relative = destPath.substring(settings.getDestination().length());
                Path srcPath = Paths.get(settings.getSource() + relative);

                if(!src.containsKey(srcPath.toString())){
                    try {
                        Files.deleteIfExists(meta.getPath());
                        LogUtil.log("Deleted missing file: " + relative);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        LogUtil.log("Sync complete.");
    }
}
