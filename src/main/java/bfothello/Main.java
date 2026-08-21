package bfothello;
import java.io.*;
import java.net.*;

import bfothello.bots.Strategy;
import bfothello.bots.strategies.*;
import javafx.application.Application;


public class Main {

    enum strategies {
        FIRSTLEGAL,
        RANDOM,
        NAIVESMALL,
        NAIVEBIG
    }

    static Strategy getStrategy(strategies s) {
        switch (s) {
            case strategies.FIRSTLEGAL -> {
                return new FirstLegalMove(){};
            }
            case strategies.RANDOM -> {
                return new RandomMove(){};
            }
            case strategies.NAIVESMALL -> {
                return new NaiveSmallImpact(){};
            }
            case NAIVEBIG -> {
                return new NaiveBigImpact(){};
            }
        }

        return null;
    }

    static void newgame() {
        String delimiter = ";";
        Othello othello = new Othello();
        Thread.ofPlatform().start(new bfothello.bots.Runbot(new NaiveSmallImpact()){});
        Thread.ofPlatform().start(new bfothello.bots.Runbot(new NaiveBigImpact()){});
        Thread.ofPlatform().start(new LobbyServer().run(65500, othello, delimiter));
    }

    static void newgame(Strategy bot1, Strategy bot2, Integer port) {
        String delimiter = ";";
        Othello othello = new Othello();
        Thread.ofPlatform().start(new bfothello.bots.Runbot(bot1){});
        Thread.ofPlatform().start(new bfothello.bots.Runbot(bot2){});
        Thread.ofPlatform().start(new LobbyServer().run(port, othello, delimiter));
    }

    public static void main(String[] args) {

        Console console = System.console();
        if (console == null)
            System.exit(-1);

        for (;;) {
            System.out.println("Start game: n");
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