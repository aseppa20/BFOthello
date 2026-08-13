package bfothello.bots;

import bfothello.Tile;
import bfothello.Tuple;

public abstract class Strategy {
    public abstract Tuple<Integer, Integer> decideMove(Tile.State yourRole, String statehash);
}
