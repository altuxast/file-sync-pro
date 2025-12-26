package main.java.com.filesyncpro.controllers;

import main.java.com.filesyncpro.config.Settings;
import main.java.com.filesyncpro.core.SyncEngine;
import main.java.com.filesyncpro.core.Scheduler;
import javafx.scene.control.Alert;

public class AppController {
    private final Settings settings;

    public AppController(Settings settings) {
        this.settings = settings;
    }

    public void setSource(String path) {
        settings.setSource(path);
        info("Source set to:\n" + path);
    }

    public void setDestination(String path){
        settings.setDestination(path);
        info("Destination set to:\n" + path);
    }

    public void incrementalSync(){
        new SyncEngine(settings).runFullSync();
        info("Full sync completed.");
    }

    public void fullSync(){
        new SyncEngine(settings).runFullSync();
        info("Full sync completed.");
    }

    // public void scheduledSync(int minutes){
    //     new Scheduler(settings).schedule(minutes);
    //     info("Scheduled sync every " + minutes + " minutes. ");
    // }

    private void info(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
