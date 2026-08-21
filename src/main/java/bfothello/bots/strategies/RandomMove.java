package bfothello.bots.strategies;

import bfothello.*;
import bfothello.bots.Strategy;

import java.util.ArrayList;

public class RandomMove extends Strategy {
    public RandomMove() {
    }


    @Override
    public Tuple<Integer, Integer> decideMove(Tile.State yourRole, String statehash) {
        Board b = new Board();

        try {
            b.constructBoardFromStateHash(statehash);
        } catch (BadHashException e) {
            System.out.println("Got a bad hash");
            return new Tuple<>(9, 9);
        }

        ArrayList<Tuple<Integer, Integer>> listOfLegalMoves = aux.findAllLegalMoves(b, yourRole);

        return aux.selectRandomMoveFromArray(listOfLegalMoves);
    }
}
