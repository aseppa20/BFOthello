package bfothello.bots;
import java.io.*;
import java.net.*;

import bfothello.*;
import bfothello.bots.strategies.FirstLegalMove;

public class Runbot implements Runnable {
    String delimiter = ";";
    String role = "";
    Tile.State rolenum = Tile.State.EMPTY;
    Strategy strategy = new FirstLegalMove();


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
             DataInputStream receive = new DataInputStream(socket.getInputStream()))
        {
            System.out.println("Hello! Bot here!");

            while (role.isBlank()) {
                try {
                    send.writeUTF("Hello");
                    role = receive.readUTF();
                    if (role.equals("Black")) {
                        rolenum = Tile.State.BLACK;
                        System.out.println("Got role Black");
                    }
                    if (role.equals("White")) {
                        rolenum = Tile.State.WHITE;
                        System.out.println("Got role White");
                    }

                    if (role.contains("Game full"))
                        System.exit(0);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Thread.sleep(1000);

            boolean game = true;
            while(game) {
                String state = receive.readUTF();
                System.out.println(state);

                if (state.equals("OK")) {
                    continue;
                }

                if (state.contains("Game over")) {
                    break;
                }

                if (state.contains("Error")) {
                    if (state.contains("Game full")) {
                        System.exit(0);
                    }
                    continue;
                }

                String[] splitstate = state.split(delimiter);

                if (splitstate.length <= 1) {
                    continue;
                }

                if (splitstate[1].equals(rolenum.toString())) {
                    Tuple<Integer, Integer> newMove = strategy.decideMove(rolenum, splitstate[0]);
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