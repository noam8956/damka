import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.*;

public class Client {
    private Socket socket;
    //private ChessGame game;
    private PrintWriter out;
    private BufferedReader in;
    int playerNumber = 0;
    String playerColor = "Black";
    int turn = 0;

    public Client(/*, ChessGame game*/) {
        //this.game = game;
        try {
            this.socket = new Socket("localhost", 5000);
            System.out.println("Connected to Chess Server");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            String playerDeclaration = in.readLine();
            if (playerDeclaration.equals("first")) {
                this.playerNumber = 0;
                this.playerColor = "Red";
                System.out.println("You are the first player");
                System.out.println("waiting for second player");
            } else if (playerDeclaration.equals("second")) {
                this.playerNumber = 1;
                this.playerColor = "Black";
                System.out.println("You are second player");
            }
            boolean continueGame = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[][] makeMove(int row,int column, int destRow,int destColumn ) throws IOException, ClassNotFoundException {
        out.println("move " +row +""+column+" " + destRow+""+destColumn);
        String status = in.readLine();
        System.out.println(status);
        if(status.equals("t")){
            String[][] newBoard = readBoard();
            System.out.println("printing new board....");
            System.out.println(Arrays.deepToString(newBoard));
            turn++;
            return newBoard;

        }
        else return null;

    }

    public String[][] readBoard() throws IOException, ClassNotFoundException {
        StringBuilder board = new StringBuilder();
        String res;
        while ((res = in.readLine()) != null && !res.isEmpty()) { // Read until an empty line
            board.append(res).append("\n");
        }
        String[] rows = board.toString().split("\n");
        String[][] boardArray = new String[rows.length-1][];;
        for (int i = 1; i< rows.length;i++){
            boardArray[i-1] = rows[i].substring(3).split("\\|");
        }
        System.out.println("printing game board...");
        System.out.println(boardArray);
        return boardArray;
    }

    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        Client client = new Client();
    }


}
