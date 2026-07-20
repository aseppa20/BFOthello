package bfothello;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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