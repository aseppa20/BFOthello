package bfothello;

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

}
