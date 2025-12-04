package main.java.com.filesyncpro.core;

import main.java.com.filesyncpro.config.Settings;

import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {
    
    private final Settings settings;

    public Scheduler(Settings settings){
        this.settings = settings;
    }

    public void schedule(int minutes){
        Timer timer = new Timer(true);  // Daemon thread for repeating maintainence activities
        SyncEngine engine = new SyncEngine(settings);

        timer.schedule(new TimerTask() {
            @Override
            public void run(){
                engine.runIncremental();
            }
        }, 0, minutes * 60 * 1000);

        System.out.println("Scheduled sync every " + minutes + " minutes.");
    }
}
