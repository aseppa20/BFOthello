package bfothello.bots.strategies.reusable;

import bfothello.Board;
import bfothello.CheckForLegalMoves;
import bfothello.Tile;
import bfothello.Tuple;

import java.util.ArrayList;
import java.util.Random;

public class Auxiliaries {
    public Tuple<Integer, Integer> selectRandomMoveFromArray(ArrayList<Tuple<Integer, Integer>> listOfMoves) {
        Random r = new Random();
        return listOfMoves.get(r.nextInt(listOfMoves.size()));
    }

    public ArrayList<Tuple<Integer, Integer>> findAllLegalMoves(Board board, Tile.State yourRole) {
        ArrayList<Tuple<Integer, Integer>> listOfLegalMoves = new ArrayList<>();
        CheckForLegalMoves CFLM = new CheckForLegalMoves();

        //Construct all legal moves
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board.getTile(x, y).getState() == Tile.State.EMPTY && !CFLM.doWalks(x, y, yourRole, board).isEmpty()) {
                    listOfLegalMoves.add(new Tuple<>(x, y));
                }
            }
        }
        return listOfLegalMoves;
    }
}
