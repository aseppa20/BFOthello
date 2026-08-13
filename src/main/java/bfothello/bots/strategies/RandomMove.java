package bfothello.bots.strategies;

import bfothello.*;
import bfothello.bots.Strategy;

import java.util.ArrayList;
import java.util.Random;

public class RandomMove extends Strategy {
    public RandomMove() {
    }


    @Override
    public Tuple<Integer, Integer> decideMove(Tile.State yourRole, String statehash) {
        CheckForLegalMoves CFLM = new CheckForLegalMoves();
        Board b = new Board();
        Random r = new Random();

        try {
            b.constructBoardFromStateHash(statehash);
        } catch (BadHashException e) {
            System.out.println("Got a bad hash");
            return new Tuple<>(9, 9);
        }

        ArrayList<Tuple<Integer, Integer>> listOfLegalMoves = new ArrayList<>();

        //Construct all legal moves
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (b.getTile(x, y).getState() == Tile.State.EMPTY && !CFLM.doWalks(x, y, yourRole, b).isEmpty()) {
                    listOfLegalMoves.add(new Tuple<>(x, y));
                }
            }
        }

        return listOfLegalMoves.get(r.nextInt(listOfLegalMoves.size()));
    }
}
