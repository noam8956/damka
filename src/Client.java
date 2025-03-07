import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.*;

public class Client {
    private Socket socket;
    //private ChessGame game;
    private PrintWriter out;
    private BufferedReader in;

    public Client(/*, ChessGame game*/) {
        //this.game = game;
        try {
            this.socket = new Socket("localhost", 5000);
            System.out.println("Connected to Chess Server");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            String playerDeclaration = in.readLine();
            int playerNumber = 0;
            if (playerDeclaration.equals("first")) {
                playerNumber = 0;
                System.out.println("You are the first player");
                System.out.println("waiting for second player");
            } else if (playerDeclaration.equals("second")) {
                playerNumber = 1;
                System.out.println("You are second player");
            }

            int turn = 0;
            boolean continueGame = true;
            /*while (continueGame) { //game loop
                StringBuilder board = new StringBuilder();
                String res;
                while ((res = in.readLine()) != null && !res.isEmpty()) { // Read until an empty line
                    board.append(res).append("\n");
                }
                System.out.println(board.toString());

                while (true) {//turn loop
                    if (playerNumber == turn % 2) {
                        System.out.println("Enter a move:");
                        String command = scanner.nextLine();
                        out.println(command);
                    }
                    String status = in.readLine();
                    if (status.equals("t")) {
                        turn++;
                        break;
                    } else if (status.equals("f")) {

                    }
                    else if (status.equals("1") || status.equals("2")){
                        System.out.println("Player " + status + " Won"); //print winner
                        continueGame = false; //break game loop
                        break;//break turn loop
                    }
                }
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[][] makeMove(int row,int column, int destRow,int destColumn ) throws IOException, ClassNotFoundException {
        String[][] check = {{"a","b","c"},{"a","c","b"},{"c","b","a"}};
        byte[] bytes = ByteArraysHandler.convert2DArrayToByteArray(check);
        System.out.println(Arrays.deepToString(ByteArraysHandler.convertByteArrayTo2DArray(bytes)));

        out.println("move " +row +""+column+" " + destRow+""+destColumn);
        String status = in.readLine();
        if(status.equals("t")){
            return readBoard();

        }
        else return null;

    }

    public String[][] readBoard() throws IOException, ClassNotFoundException {
        StringBuilder board = new StringBuilder();
        String res;
        while ((res = in.readLine()) != null && !res.isEmpty()) { // Read until an empty line
            board.append(res).append("\n");
        }
        return ByteArraysHandler.convertByteArrayTo2DArray(res.getBytes());
    }

    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        Client client = new Client();
    }


}
