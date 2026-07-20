package bfothello;
import java.io.*;
import java.net.*;

import javafx.application.Application;


public class Main {
    public static void main(String[] args) {

        if (false) {
            Board b = new Board();
            CheckForLegalMoves checker = new CheckForLegalMoves();
            try {
                b.constructBoardFromStateHash("01101010101010101001010101010101011001010110011010011001010110010101011001010110010001001000010000010010001000010100100000000000");
                System.out.println(checker.checkIfLegalMoveExists(Tile.State.BLACK, b));
            } catch (BadHashException e) {
                throw new RuntimeException(e);
            }

            } else {
            String delimiter = ";";
            Othello othello = new Othello();
            // Spawn bots, currently done in this body for testing purposes.
            // TODO: Decouple
            Thread.ofPlatform().start(new bfothello.bots.Runbot(){});
            Thread.ofPlatform().start(new bfothello.bots.Runbot(){});

            Thread.ofPlatform().start(new LobbyServer().run(65500, othello, delimiter));

        }

    }
}