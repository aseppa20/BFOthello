package bfothello.bots;
import java.io.*;
import java.net.*;

public class Runbot implements Runnable {
    @Override
    public void run() {
        System.out.println("Hello! Runbot here!");
        String role = "";
        try (Socket socket = new Socket("localhost", 65500);
             PrintWriter send = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
                while (role.isEmpty()) {
                    send.println("Hello");
                    role = receive.readLine();
                    System.out.println(role);
                }
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}