package bfothello.bots.strategies;

import bfothello.*;
import bfothello.bots.Strategy;

import java.util.ArrayList;

public class NaiveBigImpact extends Strategy {

    @Override
    public String about() {
        return "NaiveBigImpact\nLists all available move and counts which move flips the most tiles. Selects a random move if multiple moves are available.";
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

        ArrayList<Triple<Integer, Integer, Integer>> Moves = aux.findAllLegalMovesWithFlipCount(b, yourRole);
        ArrayList<Tuple<Integer, Integer>> ListOfMoves = new ArrayList<>();
        int largestImpact = 0;

        for (Triple<Integer, Integer, Integer> m : Moves) {
              if (m.getC() == largestImpact)
                  ListOfMoves.add(new Tuple<>(m.getA(), m.getB()));
              else if (m.getC() > largestImpact) {
                  largestImpact = m.getC();
                  ListOfMoves.clear();
                  ListOfMoves.add(new Tuple<>(m.getA(), m.getB()));
              }
        }

        if (ListOfMoves.size() == 1)
            return ListOfMoves.getFirst();

        return aux.selectRandomMoveFromArray(ListOfMoves);
    }
}
