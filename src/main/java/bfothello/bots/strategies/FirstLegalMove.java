package bfothello.bots.strategies;

import bfothello.*;
import bfothello.bots.Strategy;

public class FirstLegalMove extends Strategy {
    public FirstLegalMove() {
    }

    private Tuple<Integer, Integer> findFirstLegalMove(String hash, Tile.State role) {
        Board board = new Board();
        try {
            board.constructBoardFromStateHash(hash);
            CheckForLegalMoves c = new CheckForLegalMoves();
            for (Integer x = 0; x < 8; x++) {
                for (Integer y = 0; y < 8; y++) {
                    if (board.getTile(x, y).getState() != Tile.State.EMPTY || c.doWalks(x, y, role, board).isEmpty())
                        continue;
                    return new Tuple<>(x, y);
                }
            }
        } catch (BadHashException e) {
            System.out.println("Bad hash :( Retrying");
        }
        return new Tuple<>(9, 9);
    }

    @Override
    public String about() {
        return "FirstLegalMove\nCalculates the first legal move.";
    }

    @Override
    public Tuple<Integer, Integer> decideMove(Tile.State yourRole, String statehash) {
        return findFirstLegalMove(statehash, yourRole);
    }

    @Override
    public String getName() {
        return "FirstLegalMove";
    }
}
