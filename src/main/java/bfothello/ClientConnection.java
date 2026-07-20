package bfothello;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class ClientConnection implements Runnable {
    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;
    private final Othello othello;
    private final String delimiter;
    private final Tile.State role;

    public ClientConnection(Socket socket, Othello othello, String delimiter, Tile.State role) throws IOException {
        this.socket = socket;
        this.othello = othello;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        this.delimiter = delimiter;
        this.role = role;
    }

    @Override
    public void run() {
        String gamestate = "";
        // Game not started. Assign player roles.
        try {
            String line = input.readUTF();

            if (line.equals("Hello")) {
                if (role == Tile.State.BLACK)
                    output.writeUTF("Black");
                else if (role == Tile.State.WHITE)
                    output.writeUTF("White");
            } else {
                output.writeUTF("Expected Hello");
            }
        } catch (RuntimeException | IOException e) {
            throw new RuntimeException(e);
        }

        for(;;) {
            try {
                if (othello.getTurn() == Tile.State.EMPTY) {
                    output.writeUTF("Game over, bye!");
                    socket.close();
                    System.exit(0);
                }

                if (!gamestate.equals(othello.getBoard().getBoardStateHash())) {
                    output.writeUTF((othello.getBoard().getBoardStateHash() + delimiter + othello.getTurn()));
                    gamestate = othello.getBoard().getBoardStateHash();
                    System.out.println("Sent stuff");
                    continue;
                }


                System.out.println(role + " Waiting for stuff");
                String line = input.readUTF();

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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}