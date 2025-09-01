package bfothello;

import java.util.InputMismatchException;

public class Board {
    private final Tile[][] board = new Tile[8][8];

    public Board() {
        for (int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                this.board[i][j] = new Tile();
            }
        }

        this.board[3][3].setState(Tile.State.WHITE);
        this.board[4][4].setState(Tile.State.WHITE);
        this.board[3][4].setState(Tile.State.BLACK);
        this.board[4][3].setState(Tile.State.BLACK);
    }

    public void updateTile(Integer x, Integer y, Tile.State state) {
        this.board[x][y].setState(state);
    }

    public Tile getTile(Integer x, Integer y) {
        return this.board[x][y];
    }

    public String getBoardStateHash() {
        /* The entire board can be represented in 16 bytes, 2 bytes per row.
        *  00 represents an empty tile
        *  01 represents a white tile
        *  10 represents a black tile
        *  This hash returns a binary hash string
        * */
        StringBuilder binaryHash = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                binaryHash.append(this.board[i][j].getTileBinaryHash());
            }
        }
        return binaryHash.toString();
    }

    public void constructBoardFromStateHash(String stateHash) throws BadHashException {
        if (stateHash == null || stateHash.length() != 128) {
            throw new BadHashException("Hash was not 128 bits long.");
        }
        int k = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String tile = stateHash.substring(k, k + 2);
                switch (tile) {
                    case "00" -> this.board[i][j].setState(Tile.State.EMPTY);
                    case "01" -> this.board[i][j].setState(Tile.State.WHITE);
                    case "10" -> this.board[i][j].setState(Tile.State.BLACK);
                    default -> throw new BadHashException("Invalid tile hash.");
                }
                k = k + 2;
            }
        }

    }

    @Override
    public String toString() {
        String hash = this.getBoardStateHash();
        StringBuilder boardString = new StringBuilder();
        int i = 0;
        int j = 0;
        for (char c : hash.toCharArray()) {
            boardString.append(c);
            i++;
            if (i % 2 == 0) {
                boardString.append(" ");
            }
            j++;
            if (j % 16 == 0) {
                boardString.append("\n");
            }
        }

        return boardString.toString();
    }
}
