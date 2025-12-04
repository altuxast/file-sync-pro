package main.java.com.filesyncpro;

import main.java.com.filesyncpro.cli.CLI;

public class Main {
    public static void main(String[] args) throws Exception {
        CLI cli = new CLI();
        cli.start();

        System.out.println("Running...");
    }
}
