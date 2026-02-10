package bfothello;

import java.util.ArrayList;

public class Othello {
    private final Board board;
    private Tile.State turn;
    private Integer scoreWhite;
    private Integer scoreBlack;

    public Othello() {
    // As for Othello rules, black starts. Sets score to 2 for both, since there are 2 pieces on the board.
        this.board = new Board();
        this.turn = Tile.State.BLACK;
        this.scoreWhite = 2;
        this.scoreBlack = 2;
    }

    public Board getBoard() {
        return board;
    }

    public Integer getScoreWhite() {
        return scoreWhite;
    }

    public Integer getScoreBlack() {
        return scoreBlack;
    }

    private void increment_score() {
        // Increments black or white score. Does not do any decrements.
        if (this.turn == Tile.State.BLACK) {
            this.scoreBlack++;
        }
        else {
            this.scoreWhite++;
        }
    }

    private void update_score() {
        // Increments black or white score. Decrements the other.
        if (this.turn == Tile.State.BLACK) {
            this.scoreBlack++;
            this.scoreWhite--;
        }
        else {
            this.scoreBlack--;
            this.scoreWhite++;
        }
    }

    private void count_score() {
        // Count score from the board
        this.scoreWhite = 0;
        this.scoreBlack = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getTile(i, j).getState() == Tile.State.BLACK)
                    scoreBlack++;
                else if (board.getTile(i, j).getState() == Tile.State.WHITE)
                    scoreWhite++;
            }
        }
    }

    public void makeMove(Integer x, Integer y) throws IllegalMoveException {
        if (board.getTile(x, y).getState() != Tile.State.EMPTY) {
            throw new IllegalMoveException("Cannot place object to of other one.");
        }

        CheckForLegalMoves legalMoves = new CheckForLegalMoves();

        ArrayList<Tuple<Integer, Integer>> walks = legalMoves.doWalks(x, y, this.turn, board);
        if (walks.isEmpty()) {
            throw new IllegalMoveException("That is not a legal move.");
        }
        board.updateTile(x, y, this.turn);
        increment_score();
        for (Tuple<Integer, Integer> walk : walks) {
            board.updateTile(walk.getA(), walk.getB(), this.turn);
            update_score();
        }

        // Change turn, if a legal move is available
        if (this.turn == Tile.State.BLACK) {
            if (legalMoves.checkIfLegalMoveExists(Tile.State.WHITE, board))
                this.turn = Tile.State.WHITE;
            if (!legalMoves.checkIfLegalMoveExists(Tile.State.BLACK, board))
                // Game ends here, because no legal moves left
                this.turn = Tile.State.EMPTY;
        } else {
            if (legalMoves.checkIfLegalMoveExists(Tile.State.BLACK, board))
                this.turn = Tile.State.BLACK;
            if (!legalMoves.checkIfLegalMoveExists(Tile.State.WHITE, board))
                // Game ends here, because no legal moves left
                this.turn = Tile.State.EMPTY;
        }
    }



}
