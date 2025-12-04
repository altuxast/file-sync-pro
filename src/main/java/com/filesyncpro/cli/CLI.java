package main.java.com.filesyncpro.cli;

import java.util.Scanner;

import main.java.com.filesyncpro.config.Settings;
import main.java.com.filesyncpro.core.Scheduler;
import main.java.com.filesyncpro.core.SyncEngine;

public class CLI {

   private final Settings settings = new Settings();
   private final Scanner scanner = new Scanner(System.in);

   public void start() {
      while (true) {
         System.out.println("\n== FileSync Pro ===");
         System.out.println("1. Set Source Directory");
         System.out.println("2. Set Destination Directory");
         System.out.println("3. Run Incremental Sync");
         System.out.println("4. Run Full Sync");
         System.out.println("5. Run Scheduled Sync");
         System.out.println("6. Exit");

         System.out.println("Choose option: ");
         int choice = Integer.parseInt(scanner.nextLine());

         switch (choice) {
            case 1:
               setSource();
               break;
            case 2:
               setDestination();
               break;
            case 3:
               incrementalSync();
               break;
            case 4:
               fullSync();
               break;
            case 5:
               scheduledSync();
               break;
            case 6:
               System.exit(0);
               break;
            default:
               System.out.println("Invalid choice.");
               ;
         }
      }
   }

   private void scheduledSync() {
      System.out.print("Enter interval in minutes: ");
      int minutes = Integer.parseInt(scanner.nextLine());
      new Scheduler(settings).schedule(minutes);
   }

   private void fullSync() {
      new SyncEngine(settings).runFullSync();
   }

   private void incrementalSync() {
      new SyncEngine(settings).runIncremental();
   }

   private void setDestination() {
      System.out.print("Enter destination directory: ");
      settings.setSource(scanner.nextLine());
   }

   private void setSource() {
      System.out.print("Enter source directory: ");
      settings.setSource(scanner.nextLine());
   }
}
