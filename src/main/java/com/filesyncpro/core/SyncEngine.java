package main.java.com.filesyncpro.core;

import main.java.com.filesyncpro.config.Settings;
import main.java.com.filesyncpro.model.FileMetadata;
import main.java.com.filesyncpro.util.LogUtil;

import java.nio.file.*;
import java.util.Comparator;
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
        sync(true);
    }

    public void sync(boolean fullSync){
        Map<String, FileMetadata> src = scanner.scan(settings.getSource());
        Map<String, FileMetadata> dst = scanner.scan(settings.getDestination());

        src.forEach((path, meta) -> {
            String relative = path.substring(settings.getSource().length());
            Path destPath = Paths.get(settings.getDestination() + relative);
            try {
                if(!dst.containsKey(destPath.toString())){
                    if (!meta.isDirectory()) {                        
                        Files.createDirectories(destPath.getParent());
                        Files.copy(meta.getPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
                        LogUtil.log("Copied new file: " + relative);
                    }else{
                        Files.createDirectories(destPath);
                        LogUtil.log("Created new folder: " + relative);
                    }
                }else if(!meta.isDirectory() && !dst.get(destPath.toString()).isDirectory()){
                    String srcHash = meta.getHash();                    
                    String dstHash = dst.get(destPath.toString()).getHash();
                    if (srcHash != null && dstHash != null && !srcHash.equals(dstHash)) {                        
                        Files.copy(meta.getPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
                        LogUtil.log("Updated modified file: " + relative);
                    }                    
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
                        if (!meta.isDirectory()) {                            
                            Files.deleteIfExists(meta.getPath());
                            LogUtil.log("Deleted missing file: " + relative);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            try {
                Files.walk(Paths.get(settings.getDestination()))
                    .sorted(Comparator.reverseOrder())
                    .filter(Files::isDirectory)
                    .forEach(dstDir -> {
                        String relative = dstDir.toString().substring(settings.getDestination().length());
                        Path srcDir = Paths.get(settings.getSource() + relative);

                        if (!Files.exists(srcDir)) {
                            try {
                                Files.walk(dstDir)
                                .sorted(Comparator.reverseOrder())
                                .forEach(path -> {
                                    try {
                                        Files.deleteIfExists(path);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LogUtil.log("Sync complete.");
    }
}
