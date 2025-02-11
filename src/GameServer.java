import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.*;

public class GameServer {
    private CheckersGame game;
    ExecutorService pool = Executors.newCachedThreadPool(); // Supports multiple players dynamically
    private ArrayList<Socket> sockets = new ArrayList<Socket>() ;
    public GameServer() {

           try (ServerSocket serverSocket = new ServerSocket(5000)) { // Auto-select available port
               System.out.println("Chess Server started on port: " + serverSocket.getLocalPort());
               System.out.println("Waiting for players...");

               while (sockets.size() < 2) {
                   Socket socket = serverSocket.accept();
                   this.sockets.add(socket);
                   System.out.println("new player connected");
                   //Thread clientThread = new Thread(new ClientHandler(socket));
                   //clientThread.start();
               }

               while(true) { //game loop
                   System.out.println("creating game board");
                   game = new CheckersGame();
                   Socket player1 = sockets.get(0);
                   Socket player2 = sockets.get(1);
                   System.out.println("starting game");

                   BufferedReader player1Reader = new BufferedReader(new InputStreamReader(player1.getInputStream()));
                   PrintWriter player1Printer = new PrintWriter(player1.getOutputStream(), true);
                   BufferedReader player2Reader = new BufferedReader(new InputStreamReader(player2.getInputStream()));
                   PrintWriter player2Printer = new PrintWriter(player2.getOutputStream(), true);

                   // Send initial board state
                   player1Printer.println(this.game.boardToString());
                   player2Printer.println(this.game.boardToString());

                   while (true) { // Game turn loop
                       System.out.println("player 1 turn");
                       String command = player1Reader.readLine();
                       System.out.println("received command from player 1: " + command);
                       boolean result = handleClient(command,1);
                       if (result) {
                           String boardState = this.game.boardToString();
                           player1Printer.println(boardState);
                           player2Printer.println(boardState);
                       }

                       System.out.println("player 2 turn");
                       String command2 = player2Reader.readLine();
                       System.out.println("received command from player 2: " + command2);
                       boolean result2 = handleClient(command2,2);
                       if (result2) {
                           String boardState = this.game.boardToString();
                           player1Printer.println(boardState);
                           player2Printer.println(boardState);
                       }
                   }
               }
           } catch (IOException e) {
               e.printStackTrace();
           }
       }

       public boolean handleClient(String message,int player){
        char color = player == 1 ? 'b' : 'w';
        String[] command = message.split(" ");
           System.out.println(command.toString());
        if(command.length != 3)
            return false;
        if(command[0].equals("move")){
            String[] pieceLocation = command[1].split("");
            int pieceRow = Integer.parseInt(pieceLocation[0]);
            int pieceColumn = Integer.parseInt(pieceLocation[1]);
            String[] targetLocation = command[2].split("");
            int targetRow = Integer.parseInt(targetLocation[0]);
            int targetColumn = Integer.parseInt(targetLocation[1]);

            System.out.println("making a move piece ("+pieceRow +","+pieceColumn+") to (" +targetRow +"," + pieceColumn+")");
            boolean result = this.game.movePiece(pieceRow,pieceColumn,targetRow,targetColumn,color);
            return result;
        }
        else
            return false;
       }
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        GameServer gameServer = new GameServer();
    }
}
