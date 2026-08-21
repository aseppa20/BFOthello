package bfothello.bots;

import bfothello.Tile;
import bfothello.Tuple;
import bfothello.bots.strategies.reusable.Auxiliaries;

public abstract class Strategy {
    protected Auxiliaries aux = new Auxiliaries();
    public abstract String about();
    public abstract Tuple<Integer, Integer> decideMove(Tile.State yourRole, String statehash);
    public abstract String getName();
}
