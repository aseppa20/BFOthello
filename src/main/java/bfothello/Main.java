package bfothello;
import java.io.*;
import java.net.*;

import javafx.application.Application;
// Setup application and works as the server
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {


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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}