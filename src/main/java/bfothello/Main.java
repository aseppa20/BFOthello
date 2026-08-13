package bfothello;
import java.io.*;
import java.net.*;

import bfothello.bots.Runbot;
import bfothello.bots.strategies.*;
import javafx.application.Application;


public class Main {
    static void newgame() {
        String delimiter = ";";
        Othello othello = new Othello();
        Thread.ofPlatform().start(new bfothello.bots.Runbot(new FirstLegalMove()){});
        Thread.ofPlatform().start(new bfothello.bots.Runbot(new RandomMove()){});
        Thread.ofPlatform().start(new LobbyServer().run(65500, othello, delimiter));
    }

    public static void main(String[] args) {

        Console console = System.console();
        if (console == null)
            System.exit(-1);

        for (;;) {
            String input = console.readLine();

            switch (input) {
                case "e":
                    System.exit(0);
                    break;
                case "n":
                    newgame();
                    break;
                default:
                    System.out.println("I did not understand. Currently implemented: (e)xit, (n)ew game");
                    break;
            }

        }

    }
}