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
        ArrayList<Tuple<Integer, Integer>> walks = doWalks(x, y, this.turn);
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
            if (checkIfLegalMoveExists(Tile.State.WHITE))
                this.turn = Tile.State.WHITE;
            if (!checkIfLegalMoveExists(Tile.State.BLACK))
                // Game ends here, because no legal moves left
                this.turn = Tile.State.EMPTY;
        } else {
            if (checkIfLegalMoveExists(Tile.State.BLACK))
                this.turn = Tile.State.BLACK;
            if (!checkIfLegalMoveExists(Tile.State.WHITE))
                // Game ends here, because no legal moves left
                this.turn = Tile.State.EMPTY;
        }
    }

    private Boolean checkIfLegalMoveExists(Tile.State state) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getTile(i, j).getState() == Tile.State.EMPTY && !doWalks(i, j, state).isEmpty() ) {
                    return true;
                }
            }
        }

        return false;
    }

    private ArrayList<Tuple<Integer, Integer>> doWalks(Integer x, Integer y, Tile.State turn) {
       ArrayList<Tuple<Integer, Integer>> up = walkUp(x , y, turn);
       ArrayList<Tuple<Integer, Integer>> down = walkDown(x , y, turn);
       ArrayList<Tuple<Integer, Integer>> left = walkLeft(x , y, turn);
       ArrayList<Tuple<Integer, Integer>> right = walkRight(x , y, turn);
       ArrayList<Tuple<Integer, Integer>> upleft = walkUpLeft(x , y, turn);
       ArrayList<Tuple<Integer, Integer>> upright = walkUpRight(x , y, turn);
       ArrayList<Tuple<Integer, Integer>> downleft = walkDownLeft(x , y, turn);
       ArrayList<Tuple<Integer, Integer>> downright = walkDownRight(x , y, turn);

       ArrayList<Tuple<Integer, Integer>> merged = new ArrayList<>();
       merged.addAll(up);
       merged.addAll(down);
       merged.addAll(left);
       merged.addAll(right);
       merged.addAll(upleft);
       merged.addAll(upright);
       merged.addAll(downleft);
       merged.addAll(downright);

       return merged;
    }

    // FIXME: Turn to recursive. Very hacky
    // FIXME: Make this its own class
    private ArrayList<Tuple<Integer, Integer>> walkUp(Integer x, Integer y, Tile.State turn) {
        ArrayList<Tuple<Integer, Integer>> ups = new ArrayList<>();
        if (turn == Tile.State.BLACK) {
            for (int i = y + 1; i < 8; i++) {
                if (this.board.getTile(x, i).getState() == Tile.State.EMPTY || this.board.getTile(x, i).getState() == Tile.State.BLACK) {
                    if (this.board.getTile(x, i).getState() == Tile.State.EMPTY) {
                        ups.clear();
                    }
                    return ups;
                }
                ups.add(new Tuple<>(x, i));
            }
        } else if  (turn == Tile.State.WHITE) {
            for (int i = y + 1; i < 8; i++) {
                if (this.board.getTile(x, i).getState() == Tile.State.EMPTY || this.board.getTile(x, i).getState() == Tile.State.WHITE) {
                    if (this.board.getTile(x, i).getState() == Tile.State.EMPTY) {
                        ups.clear();
                    }
                    return ups;
                }
                ups.add(new Tuple<>(x, i));
            }
        }
        return ups;
    }

    private ArrayList<Tuple<Integer, Integer>> walkDown(Integer x, Integer y, Tile.State turn) {
        ArrayList<Tuple<Integer, Integer>> downs = new ArrayList<>();
        if (turn == Tile.State.BLACK) {
            for (int i = y - 1; i > 0; i--) {
                if (this.board.getTile(x, i).getState() == Tile.State.EMPTY || this.board.getTile(x, i).getState() == Tile.State.BLACK) {
                    if (this.board.getTile(x, i).getState() == Tile.State.EMPTY) {
                        downs.clear();
                    }
                    return downs;
                }
                downs.add(new Tuple<>(x, i));
            }
        } else if  (turn == Tile.State.WHITE) {
            for (int i = y - 1; i > 0; i--) {
                if (this.board.getTile(x, i).getState() == Tile.State.EMPTY || this.board.getTile(x, i).getState() == Tile.State.WHITE) {
                    if (this.board.getTile(x, i).getState() == Tile.State.EMPTY) {
                        downs.clear();
                    }
                    return downs;
                }
                downs.add(new Tuple<>(x, i));
            }
        }
        return downs;
    }
    private ArrayList<Tuple<Integer, Integer>> walkLeft(Integer x, Integer y, Tile.State turn) {
        ArrayList<Tuple<Integer, Integer>> lefts = new ArrayList<>();
        if (turn == Tile.State.BLACK) {
            for (int i = x + 1; i < 8; i++) {
                if (this.board.getTile(i, y).getState() == Tile.State.EMPTY || this.board.getTile(i, y).getState() == Tile.State.BLACK) {
                    if (this.board.getTile(i, y).getState() == Tile.State.EMPTY) {
                        lefts.clear();
                    }
                    return lefts;
                }
                lefts.add(new Tuple<>(i, y));
            }
        } else if  (turn == Tile.State.WHITE) {
            for (int i = x + 1; i < 8; i++) {
                if (this.board.getTile(i, y).getState() == Tile.State.EMPTY || this.board.getTile(i, y).getState() == Tile.State.WHITE) {
                    if (this.board.getTile(i, y).getState() == Tile.State.EMPTY) {
                        lefts.clear();
                    }
                    return lefts;
                }
                lefts.add(new Tuple<>(i, y));
            }
        }
        return lefts;

    }

    private ArrayList<Tuple<Integer, Integer>> walkRight(Integer x, Integer y, Tile.State turn) {
        ArrayList<Tuple<Integer, Integer>> rights = new ArrayList<>();
        if (turn == Tile.State.BLACK) {
            for (int i = x - 1; i > 8; i--) {
                if (this.board.getTile(i, y).getState() == Tile.State.EMPTY || this.board.getTile(i, y).getState() == Tile.State.BLACK) {
                    if (this.board.getTile(i, y).getState() == Tile.State.EMPTY) {
                        rights.clear();
                    }
                    return rights;
                }
                rights.add(new Tuple<>(i, y));
            }
        } else if  (turn == Tile.State.WHITE) {
            for (int i = x - 1; i > 8; i--) {
                if (this.board.getTile(i, y).getState() == Tile.State.EMPTY || this.board.getTile(i, y).getState() == Tile.State.WHITE) {
                    if (this.board.getTile(i, y).getState() == Tile.State.EMPTY) {
                        rights.clear();
                    }
                    return rights;
                }
                rights.add(new Tuple<>(i, y));
            }
        }
        return rights;
    }

    private ArrayList<Tuple<Integer, Integer>> walkUpLeft(Integer x, Integer y, Tile.State turn) {
        ArrayList<Tuple<Integer, Integer>> uplefts = new ArrayList<>();
        if (turn == Tile.State.BLACK) {
            int i = y - 1;
            int j = x - 1;
            while (i > 0 && j > 0) {
                if (this.board.getTile(j, i).getState() == Tile.State.EMPTY || this.board.getTile(j, i).getState() == Tile.State.BLACK) {
                    if (this.board.getTile(j, i).getState() == Tile.State.EMPTY) {
                        uplefts.clear();
                    }
                    return uplefts;
                }
                uplefts.add(new Tuple<>(j, i));
                j--;
                i--;
            }
        } else if  (turn == Tile.State.WHITE) {
            int i = y - 1;
            int j = x - 1;
            while (i > 0 && j > 0) {
                if (this.board.getTile(j, i).getState() == Tile.State.EMPTY || this.board.getTile(j, i).getState() == Tile.State.WHITE) {
                    if (this.board.getTile(j, i).getState() == Tile.State.EMPTY) {
                        uplefts.clear();
                    }
                    return uplefts;
                }
                uplefts.add(new Tuple<>(j, i));
                j--;
                i--;
            }
        }
        return uplefts;
    }

    private ArrayList<Tuple<Integer, Integer>> walkUpRight(Integer x, Integer y, Tile.State turn) {
        ArrayList<Tuple<Integer, Integer>> uprights = new ArrayList<>();
        if (turn == Tile.State.BLACK) {
            int i = y - 1;
            int j = x + 1;
            while (i > 0 && j < 8) {
                if (this.board.getTile(j, i).getState() == Tile.State.EMPTY || this.board.getTile(j, i).getState() == Tile.State.BLACK) {
                    if (this.board.getTile(j, i).getState() == Tile.State.EMPTY) {
                        uprights.clear();
                    }
                    return uprights;
                }
                uprights.add(new Tuple<>(j, i));
                j++;
                i--;
            }
        } else if  (turn == Tile.State.WHITE) {
            int i = y - 1;
            int j = x + 1;
            while (i > 0 && j < 8) {
                if (this.board.getTile(j, i).getState() == Tile.State.EMPTY || this.board.getTile(j, i).getState() == Tile.State.WHITE) {
                    if (this.board.getTile(j, i).getState() == Tile.State.EMPTY) {
                        uprights.clear();
                    }
                    return uprights;
                }
                uprights.add(new Tuple<>(j, i));
                j++;
                i--;
            }
        }
        return uprights;
    }

    private ArrayList<Tuple<Integer, Integer>> walkDownLeft(Integer x, Integer y, Tile.State turn) {
        ArrayList<Tuple<Integer, Integer>> downlefts = new ArrayList<>();
        if (turn == Tile.State.BLACK) {
            int i = y + 1;
            int j = x - 1;
            while (i < 8 && j > 0) {
                if (this.board.getTile(j, i).getState() == Tile.State.EMPTY || this.board.getTile(j, i).getState() == Tile.State.BLACK) {
                    if (this.board.getTile(j, i).getState() == Tile.State.EMPTY) {
                        downlefts.clear();
                    }
                    return downlefts;
                }
                downlefts.add(new Tuple<>(j, i));
                j--;
                i++;
            }
        } else if  (turn == Tile.State.WHITE) {
            int i = y + 1;
            int j = x - 1;
            while (i < 8 && j > 0) {
                if (this.board.getTile(j, i).getState() == Tile.State.EMPTY || this.board.getTile(j, i).getState() == Tile.State.WHITE) {
                    if (this.board.getTile(j, i).getState() == Tile.State.EMPTY) {
                        downlefts.clear();
                    }
                    return downlefts;
                }
                downlefts.add(new Tuple<>(j, i));
                j--;
                i++;
            }
        }
        return downlefts;
    }
    private ArrayList<Tuple<Integer, Integer>> walkDownRight(Integer x, Integer y, Tile.State turn) {
        ArrayList<Tuple<Integer, Integer>> downrights = new ArrayList<>();
        if (turn == Tile.State.BLACK) {
            int i = y + 1;
            int j = x + 1;
            while (i < 8 && j < 8) {
                if (this.board.getTile(j, i).getState() == Tile.State.EMPTY || this.board.getTile(j, i).getState() == Tile.State.BLACK) {
                    if (this.board.getTile(j, i).getState() == Tile.State.EMPTY) {
                        downrights.clear();
                    }
                    return downrights;
                }

                downrights.add(new Tuple<>(j, i));
                j++;
                i++;
            }
        } else if  (turn == Tile.State.WHITE) {
            int i = y + 1;
            int j = x + 1;
            while (i < 8 && j < 8) {
                if (this.board.getTile(j, i).getState() == Tile.State.EMPTY || this.board.getTile(j, i).getState() == Tile.State.WHITE) {
                    if (this.board.getTile(j, i).getState() == Tile.State.EMPTY) {
                        downrights.clear();
                    }
                    return downrights;
                }
                downrights.add(new Tuple<>(j, i));
                j++;
                i++;
            }
        }
        return downrights;
    }


}
