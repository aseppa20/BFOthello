package bfothello;

import java.util.ArrayList;

public class Othello {
    private Board board;
    public enum Turn {
        BLACK,
        WHITE;
    }
    private Turn turn;
    public Othello(Board board) {
    // As for Othello rules, black starts
        this.board = board;
        this.turn = Turn.BLACK;
    }

    public Board getBoard() {
        return board;
    }

    private ArrayList<String> AllowedMovesWhite;
    private ArrayList<String> AllowedMovesBlack;
}
