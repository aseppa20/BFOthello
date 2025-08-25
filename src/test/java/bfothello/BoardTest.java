package bfothello;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @org.junit.jupiter.api.Test
    void constructBoardFromStateHash() {
        Board board = new Board();
        String boardstate = "00000000000000000000000000000000000001000000000000001001100000000000001001000000000000000000000000000000000000000000000000000000";
        try {
            board.constructBoardFromStateHash(boardstate);
        } catch (BadHashException e) {
            fail(e.getMessage());
        }
        assertEquals(boardstate, board.getBoardStateHash());

    }
}