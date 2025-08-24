package bfothello;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Othello othello = new Othello();
        System.out.println(othello.getBoard().getBoardStateHash());
        try {
            othello.makeMove(3, 2);
            othello.makeMove(2, 2);
        }  catch (IllegalMoveException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(othello.getBoard().getBoardStateHash());
    }
}