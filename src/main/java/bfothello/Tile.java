package bfothello;

public class Tile {
    // A tile on the board
    public enum State {
        EMPTY,WHITE,BLACK;
    }

    private State state;

    public Tile() {
        this.state = State.EMPTY;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getTileBinaryHash() {
        // A tile can be represented as a trinary state as shown in enum.
        if (this.state == State.EMPTY) {
            return "00";
        }
        if  (this.state == State.WHITE) {
            return "01";
        }
        // IF not empty or white, must be black
        return "10";

    }
}
