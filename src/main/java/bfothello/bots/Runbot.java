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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try (Socket socket = new Socket("localhost", 65500);
             DataOutputStream send = new DataOutputStream(socket.getOutputStream());
             DataInputStream receive = new DataInputStream(socket.getInputStream());)
        {
            System.out.println("Hello! Bot here!");

            while (role.isBlank()) {
                try {
                    send.writeUTF("Hello");
                    Thread.sleep(200);
                    role = receive.readUTF();
                    if (role.equals("Black")) {
                        rolenum = Tile.State.BLACK;
                        System.out.println("Got role Black");
                    }
                    if (role.equals("White")) {
                        rolenum = Tile.State.WHITE;
                        System.out.println("Got role White");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    continue;
                }
            }
            Thread.sleep(1000);

                boolean game = true;
                while(game) {
                    send.writeUTF("State");
                    Thread.sleep(100);
                    String state = receive.readUTF();
                    System.out.println(state);
                    String[] splitstate = state.split(delimiter);
                    if (splitstate.length <= 1) {
                        Thread.sleep(500);
                        continue;
                    }
                    if (splitstate[1].equals(rolenum.toString())) {
                        Tuple<Integer, Integer> newMove = findFirstLegalMove(splitstate[0]);
                        send.writeUTF(("Move" + delimiter + role + delimiter + newMove.getA().toString() + delimiter + newMove.getB().toString()));
                    } else if (splitstate[1].equals("0")) {
                        game = false;
                    }

                }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}