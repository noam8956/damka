package Itay;

import java.io.*;
import java.net.*;

public class CheckersServer {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started, waiting for players...");

            // Accept connections from two clients (2 players)
            Socket player1 = serverSocket.accept();
            System.out.println("Player 1 connected.");
            Socket player2 = serverSocket.accept();
            System.out.println("Player 2 connected.");

            // Create threads to handle communication with both players
            new PlayerHandler(player1, 1).start();
            new PlayerHandler(player2, 2).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // This class will handle communication with each player.
    static class PlayerHandler extends Thread {
        private Socket socket;
        private int playerNumber;

        public PlayerHandler(Socket socket, int playerNumber) {
            this.socket = socket;
            this.playerNumber = playerNumber;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                // Game loop: Handle communication with the client
                String message;
                while ((message = in.readLine()) != null) {
                    // Process the move and send the game state update back to both players
                    System.out.println("Received from Player " + playerNumber + ": " + message);
                    // Handle the move here and update game state
                    out.println("Move received: " + message);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
