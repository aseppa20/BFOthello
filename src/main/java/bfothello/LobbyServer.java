package bfothello;

import java.io.IOException;
import java.net.ServerSocket;

public class LobbyServer extends Thread {
    public Runnable run(int port, Othello othello, String delimiter) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (othello.getTurn() != Tile.State.EMPTY) {
                new GameServer(serverSocket.accept(), othello, delimiter).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }
}
