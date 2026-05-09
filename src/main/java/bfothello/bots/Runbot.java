package bfothello.bots;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import bfothello.*;

public class Runbot implements Runnable {
    String delimiter = ";";
    String role = "";
    Tile.State rolenum = Tile.State.EMPTY;

    private Tuple<Integer, Integer> findFirstLegalMove(String hash) {
        Board board = new Board();
        try {
            board.constructBoardFromStateHash(hash);
            ArrayList<Tuple<Integer, Integer>> legalMoves = new ArrayList<>();
            CheckForLegalMoves c = new CheckForLegalMoves();
            for (Integer x = 0; x < 8; x++) {
                for (Integer y = 0; y < 8; y++) {
                    if (! c.doWalks(x, y, rolenum, board).isEmpty())
                        return new Tuple<>(x, y);
                }
            }
        } catch (BadHashException e) {
            System.out.println("Bad hash :( Retrying");
        }
        return new Tuple<>(9, 9);
    }

    @Override
    public void run() {
        System.out.println("Hello! Runbot here!");

        try (Socket socket = new Socket("localhost", 65500);
             PrintWriter send = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
                while (role.isEmpty()) {
                    send.println("Hello");
                    role = receive.readLine();
                    System.out.println(role);
                    if (role.equals("Black"))
                        rolenum = Tile.State.BLACK;
                    if (role.equals("White"))
                        rolenum = Tile.State.WHITE;
                }
                Thread.sleep(100);

                boolean game = true;
                while(game) {
                    send.println("State");
                    String state =  receive.readLine();
                    if (state == null)
                        continue;
                    String[] splitstate = state.split(delimiter);
                    if (splitstate[1].equals(rolenum.toString())) {
                        Tuple<Integer, Integer> newMove = findFirstLegalMove(splitstate[0]);
                        send.println(("Move" + delimiter + role + delimiter + newMove.getA().toString() + delimiter + newMove.getB().toString()));
                    } else if (splitstate[1].equals("0")) {
                        game = false;
                    }
                    receive.readLine();

                Thread.sleep(100);
                }


                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}