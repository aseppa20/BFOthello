package bfothello;

import java.util.ArrayList;

public class Othello {
    private final Board board;
    private Tile.State turn;
    private ArrayList<String> AllowedMovesWhite;
    private ArrayList<String> AllowedMovesBlack;

    public Othello() {
    // As for Othello rules, black starts
        this.board = new Board();
        this.turn = Tile.State.BLACK;
    }

    public Board getBoard() {
        return board;
    }

    private void updateAllowedMoves() {
        //No implementation yet
    }

    public void makeMove(Integer x, Integer y) throws IllegalMoveException {
        if (board.getTile(x, y).getState() != Tile.State.EMPTY) {
            throw new IllegalMoveException("Cannot place object to of other one.");
        }
        ArrayList<Tuple<Integer, Integer>> walks = doWalks(x, y);
        if (walks.isEmpty()) {
            throw new IllegalMoveException("That is not a legal move.");
        }
        board.updateTile(x, y, turn);
        for (Tuple<Integer, Integer> walk : walks) {
            board.updateTile(walk.getA(), walk.getB(), turn);
        }

        // TODO: Need to check if any legal moves are available
        if (turn == Tile.State.BLACK) {
            turn = Tile.State.WHITE;
        } else {
            turn = Tile.State.BLACK;
        }
    }

    private ArrayList<Tuple<Integer, Integer>> doWalks(Integer x, Integer y) {
       ArrayList<Tuple<Integer, Integer>> up = walkUp(x , y);
       ArrayList<Tuple<Integer, Integer>> down = walkDown(x , y);
       ArrayList<Tuple<Integer, Integer>> left = walkLeft(x , y);
       ArrayList<Tuple<Integer, Integer>> right = walkLeft(x , y);
       ArrayList<Tuple<Integer, Integer>> upleft = walkUpLeft(x , y);
       ArrayList<Tuple<Integer, Integer>> upright = walkUpRight(x , y);
       ArrayList<Tuple<Integer, Integer>> downleft = walkDownLeft(x , y);
       ArrayList<Tuple<Integer, Integer>> downright = walkDownRight(x , y);

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

    // FIXME: Is there a better way to check these?
    private ArrayList<Tuple<Integer, Integer>> walkUp(Integer x, Integer y) {
        ArrayList<Tuple<Integer, Integer>> ups = new ArrayList<>();
        if (turn == Tile.State.BLACK) {
            for (int i = y + 1; i < 8; i++) {
                if (this.board.getTile(x, i).getState() == Tile.State.EMPTY || this.board.getTile(x, i).getState() == Tile.State.BLACK) {
                    return ups;
                }
                ups.add(new Tuple<>(x, i));
            }
        } else if  (turn == Tile.State.WHITE) {
            for (int i = y + 1; i < 8; i++) {
                if (this.board.getTile(x, i).getState() == Tile.State.EMPTY || this.board.getTile(x, i).getState() == Tile.State.WHITE) {
                    return ups;
                }
                ups.add(new Tuple<>(x, i));
            }
        }
        return ups;
    }

    private ArrayList<Tuple<Integer, Integer>> walkDown(Integer x, Integer y) {
        ArrayList<Tuple<Integer, Integer>> downs = new ArrayList<>();
        if (turn == Tile.State.BLACK) {
            for (int i = y - 1; i > 0; i--) {
                if (this.board.getTile(x, i).getState() == Tile.State.EMPTY || this.board.getTile(x, i).getState() == Tile.State.BLACK) {
                    return downs;
                }
                downs.add(new Tuple<>(x, i));
            }
        } else if  (turn == Tile.State.WHITE) {
            for (int i = y - 1; i > 0; i--) {
                if (this.board.getTile(x, i).getState() == Tile.State.EMPTY || this.board.getTile(x, i).getState() == Tile.State.WHITE) {
                    return downs;
                }
                downs.add(new Tuple<>(x, i));
            }
        }
        return downs;
    }
    private ArrayList<Tuple<Integer, Integer>> walkLeft(Integer x, Integer y) {
        ArrayList<Tuple<Integer, Integer>> lefts = new ArrayList<>();
        if (turn == Tile.State.BLACK) {
            for (int i = x + 1; i < 8; i++) {
                if (this.board.getTile(i, y).getState() == Tile.State.EMPTY || this.board.getTile(i, y).getState() == Tile.State.BLACK) {
                    return lefts;
                }
                lefts.add(new Tuple<>(i, y));
            }
        } else if  (turn == Tile.State.WHITE) {
            for (int i = x + 1; i < 8; i++) {
                if (this.board.getTile(i, y).getState() == Tile.State.EMPTY || this.board.getTile(i, y).getState() == Tile.State.WHITE) {
                    return lefts;
                }
                lefts.add(new Tuple<>(i, y));
            }
        }
        return lefts;

    }

    private ArrayList<Tuple<Integer, Integer>> walkRight(Integer x, Integer y) {
        ArrayList<Tuple<Integer, Integer>> rights = new ArrayList<>();
        if (turn == Tile.State.BLACK) {
            for (int i = x - 1; i > 8; i--) {
                if (this.board.getTile(i, y).getState() == Tile.State.EMPTY || this.board.getTile(i, y).getState() == Tile.State.BLACK) {
                    return rights;
                }
                rights.add(new Tuple<>(i, y));
            }
        } else if  (turn == Tile.State.WHITE) {
            for (int i = x - 1; i > 8; i--) {
                if (this.board.getTile(i, y).getState() == Tile.State.EMPTY || this.board.getTile(i, y).getState() == Tile.State.WHITE) {
                    return rights;
                }
                rights.add(new Tuple<>(i, y));
            }
        }
        return rights;
    }

    private ArrayList<Tuple<Integer, Integer>> walkUpLeft(Integer x, Integer y) {
        ArrayList<Tuple<Integer, Integer>> uplefts = new ArrayList<>();
        if (turn == Tile.State.BLACK) {
            int i = y - 1;
            int j = x - 1;
            while (i > 0 && j > 0) {
                if (this.board.getTile(j, i).getState() == Tile.State.EMPTY || this.board.getTile(j, i).getState() == Tile.State.WHITE) {
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
                if (this.board.getTile(j, i).getState() == Tile.State.EMPTY || this.board.getTile(j, i).getState() == Tile.State.BLACK) {
                    return uplefts;
                }
                uplefts.add(new Tuple<>(j, i));
                j--;
                i--;
            }
        }
        return uplefts;
    }

    private ArrayList<Tuple<Integer, Integer>> walkUpRight(Integer x, Integer y) {
        ArrayList<Tuple<Integer, Integer>> uprights = new ArrayList<>();
        if (turn == Tile.State.BLACK) {
            int i = y - 1;
            int j = x + 1;
            while (i > 0 && j < 8) {
                if (this.board.getTile(j, i).getState() == Tile.State.EMPTY || this.board.getTile(j, i).getState() == Tile.State.WHITE) {
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
                if (this.board.getTile(j, i).getState() == Tile.State.EMPTY || this.board.getTile(j, i).getState() == Tile.State.BLACK) {
                    return uprights;
                }
                uprights.add(new Tuple<>(j, i));
                j++;
                i--;
            }
        }
        return uprights;
    }

    private ArrayList<Tuple<Integer, Integer>> walkDownLeft(Integer x, Integer y) {
        ArrayList<Tuple<Integer, Integer>> uprights = new ArrayList<>();
        if (turn == Tile.State.BLACK) {
            int i = y + 1;
            int j = x - 1;
            while (i < 8 && j > 0) {
                if (this.board.getTile(j, i).getState() == Tile.State.EMPTY || this.board.getTile(j, i).getState() == Tile.State.WHITE) {
                    return uprights;
                }
                uprights.add(new Tuple<>(j, i));
                j--;
                i++;
            }
        } else if  (turn == Tile.State.WHITE) {
            int i = y + 1;
            int j = x - 1;
            while (i < 8 && j > 0) {
                if (this.board.getTile(j, i).getState() == Tile.State.EMPTY || this.board.getTile(j, i).getState() == Tile.State.BLACK) {
                    return uprights;
                }
                uprights.add(new Tuple<>(j, i));
                j--;
                i++;
            }
        }
        return uprights;
    }
    private ArrayList<Tuple<Integer, Integer>> walkDownRight(Integer x, Integer y) {
        ArrayList<Tuple<Integer, Integer>> downrights = new ArrayList<>();
        if (turn == Tile.State.BLACK) {
            int i = y + 1;
            int j = x + 1;
            while (i < 8 && j < 8) {
                if (this.board.getTile(j, i).getState() == Tile.State.EMPTY || this.board.getTile(j, i).getState() == Tile.State.WHITE) {
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
                if (this.board.getTile(j, i).getState() == Tile.State.EMPTY || this.board.getTile(j, i).getState() == Tile.State.BLACK) {
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
