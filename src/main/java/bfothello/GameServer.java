package bfothello;
import java.io.IOException;
import java.net.*;

public class GameServer extends Thread {
    private Socket socket = null;
    private final Othello othello;
    private final String delimiter;

    public GameServer(Socket socket, Othello othello, String delimiter) {
        super("GameServer");
        this.socket = socket;
        this.othello = othello;
        this.delimiter = delimiter;
    }


    public void run() {
        if (!othello.isTherePlayerBlack() || !othello.isTherePlayerWhite()) {
            Tile.State role = Tile.State.EMPTY;

            if (!othello.isTherePlayerBlack()) {
                role = Tile.State.BLACK;
                othello.setPlayerBlack(true);
            }
            else if (!othello.isTherePlayerWhite()) {
                role = Tile.State.WHITE;
                othello.setPlayerWhite(true);
            }

            Runnable client = null;
            try {
                client = new ClientConnection(socket, othello, delimiter, role);
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
