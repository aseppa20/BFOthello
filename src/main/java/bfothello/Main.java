package bfothello;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javafx.application.Application;
// Setup application and works as the server
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
class ClientConnection implements Runnable {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private Othello othello;
    private String delimiter;

    public ClientConnection(Socket socket, Othello othello, String delimiter) throws IOException {
        this.socket = socket;
        this.othello = othello;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        this.delimiter = delimiter;
    }

    @Override
    public void run() {
        for(;;) {
        try {
                String line = input.readUTF();
                if (line.isEmpty())
                    continue;
                System.out.println(line);

                if (!othello.isTherePlayerBlack() || !othello.isTherePlayerWhite()) {

                    // Game not started. Assign player roles.
                    if (line.equals("Hello")) {
                        // Here is a race condition. Commit for research purposes!
                        if (!othello.isTherePlayerBlack()) {
                            othello.setPlayerBlack(true);
                            output.writeUTF("Black");
                        } else if (!othello.isTherePlayerWhite()) {
                            othello.setPlayerWhite(true);
                            output.writeUTF("White");
                        }
                    } else {
                        output.writeUTF("Wait");
                    }
                // Game is ON!
                } else if (othello.getTurn() != Tile.State.EMPTY) {
                    if (line.equals("Hello"))
                        output.writeUTF(("Error" + delimiter + "Game Full"));

                    else if (line.equals("State"))
                        output.writeUTF((othello.getBoard().getBoardStateHash() + delimiter + othello.getTurn()));

                    else if (line.contains("Move")) {
                        if (line.contains("Black") && line.contains("White"))
                            output.writeUTF(("Error" + delimiter + "No Multi Role"));
                        else if (line.contains("Black") && othello.getTurn() == Tile.State.WHITE ||
                                line.contains("White") && othello.getTurn() == Tile.State.BLACK)
                            output.writeUTF(("Error" + delimiter + "Not Your Turn"));
                        else if (line.contains("Black") && othello.getTurn() == Tile.State.BLACK ||
                                line.contains("White") && othello.getTurn() == Tile.State.WHITE) {
                            // Move;Turn;X;Y
                            String[] splitted = line.split(delimiter);
                            try {
                                othello.makeMove(splitted[2], splitted[3]);
                                output.writeUTF("OK");
                                System.out.println(othello.getBoard().getBoardStateHash());
                            } catch (IllegalMoveException e) {
                                output.writeUTF(("Error" + delimiter + "Illegal Move"));
                            }
                        }
                    }

                    else {
                        output.writeUTF("Error");
                    }
                }

                else {
                    output.writeUTF("Game over, bye!");
                    socket.close();
                    break;
                }

            } catch (IOException e) {
            e.printStackTrace();
            }
        }
    }
}

class OthelloServerThread extends Thread {
    private Socket socket = null;
    private Othello othello;
    private String delimiter;

    public OthelloServerThread(Socket socket, Othello othello, String delimiter) {
        super("OthelloServerThread");
        this.socket = socket;
        this.othello = othello;
        this.delimiter = delimiter;
    }

    public void run() {
        //Game has not been started. Try offer roles so game starts
        if (!othello.isTherePlayerBlack() || !othello.isTherePlayerWhite()) {
            Runnable client = null;
            try {
                client = new ClientConnection(socket, othello, delimiter);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            new Thread(client).start();
            System.out.println("Connection got!");

        } else {
            try {
                socket.getOutputStream().write(("Error" + delimiter + "Game Full").getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


public class Main {
    public static void main(String[] args) {
        String delimiter = ";";
        Othello othello = new Othello();
        // Spawn bots, currently done in this body for testing purposes.
        // TODO: Decouple
        Thread.ofPlatform().start(new bfothello.bots.Runbot(){});
        Thread.ofPlatform().start(new bfothello.bots.Runbot(){});

        try (ServerSocket serverSocket = new ServerSocket(65500)) {
            while (othello.getTurn() != Tile.State.EMPTY) {
                new OthelloServerThread(serverSocket.accept(), othello, delimiter).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}