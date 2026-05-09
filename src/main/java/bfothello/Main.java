package bfothello;
import java.io.*;
import java.net.*;

import javafx.application.Application;
// Setup application and works as the server
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
    String delimiter = ";";

        try (ServerSocket serverSocket = new ServerSocket(65500)) {
            Othello othello = new Othello();
            System.out.println(othello.getBoard());

            // Spawn bots
            Thread t1 = Thread.ofPlatform().start(new bfothello.bots.Runbot(){});
            Thread t2 = Thread.ofPlatform().start(new bfothello.bots.Runbot(){});

            boolean playerblack = false;
            boolean playerwhite = false;

            while (!playerblack || !playerwhite) {
                //give bot black or white
                try (Socket socket = serverSocket.accept()) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    if (in.readLine().equals("Hello")) {
                        if (!playerblack) {
                            socket.getOutputStream().write("Black".getBytes());
                            playerblack = true;
                            System.out.println("Black given");
                        } else {
                            socket.getOutputStream().write("White".getBytes());
                            playerwhite = true;
                            System.out.println("White given");
                        }
                    } else {
                        socket.getOutputStream().write("Wait".getBytes());
                    }
                    socket.getOutputStream().flush();
                }
            }

            while(othello.getTurn() != Tile.State.EMPTY) {
                try (Socket socket = serverSocket.accept()) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String line = in.readLine();
                    if (line.equals("Hello")) {
                        socket.getOutputStream().write("Game Full".getBytes());
                    }
                    else if (line.equals("State")) {
                        String state = othello.getBoard() + delimiter + othello.getTurn();
                        socket.getOutputStream().write(state.getBytes());

                    } else if (line.contains("Move")) {
                        if (line.contains("Black") && line.contains("White"))
                            socket.getOutputStream().write("No Multiroles".getBytes());

                        else if (line.contains("Black") && othello.getTurn() == Tile.State.WHITE ||
                                 line.contains("White") && othello.getTurn() == Tile.State.BLACK)
                            socket.getOutputStream().write("Not Your Turn".getBytes());

                        else if (line.contains("Black") && othello.getTurn() == Tile.State.BLACK ||
                                line.contains("White") && othello.getTurn() == Tile.State.WHITE) {
                            // Move;Turn;X;Y
                            String[] splitted = line.split(delimiter);
                            try {
                                othello.makeMove(splitted[2], splitted[3]);
                            } catch (IllegalMoveException e) {
                                socket.getOutputStream().write("Illegal Move".getBytes());
                            }
                        }
                    }

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}