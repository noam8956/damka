import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

class CheckersGui {
    public static Client player = new Client();
    public static boardSquare[][] board = new boardSquare[8][8];
    public static JPanel boardPanel = new JPanel();

    public CheckersGui(){
        // Creating instance of JFrame
        JFrame mainFrame = new JFrame();
        mainFrame.setTitle( "Noam Checkers");
        mainFrame.setSize(500, 600);

        // Creating boardPanel for the board layout
        GridLayout boardLayout = new GridLayout(8, 8);
        boardPanel.setLayout(boardLayout);

        // Creating the main layout
        BoxLayout mainLayout = new BoxLayout(mainFrame.getContentPane(), BoxLayout.Y_AXIS);
        mainFrame.setLayout(mainLayout);



        JLabel playerLabel = new JLabel("you are player " + (player.playerNumber + 1));
        playerLabel.setForeground(Color.BLUE);
        playerLabel.setFont(new Font("Arial",Font.PLAIN,24));
        JLabel playerColorLabel = new JLabel( " color is " + player.playerColor);
        playerColorLabel.setForeground(player.playerColor.equals("Red") ? Color.RED : Color.BLACK);
        playerColorLabel.setFont(new Font("Arial",Font.PLAIN,24));

        mainFrame.add(playerLabel);
        mainFrame.add(playerColorLabel);

        // Creating and adding board to mainFrame
        mainFrame.add(boardPanel);

        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                if ((row + column) % 2 == 0) {
                    if (row <= 2)
                        board[row][column] = new boardSquare(row, column, "Red");
                    else if (row >= 5)
                        board[row][column] = new boardSquare(row, column, "Black");
                    else
                        board[row][column] = new boardSquare(row, column, "Blank");
                } else
                    board[row][column] = new boardSquare(row, column, "Blank");
                boardPanel.add(board[row][column]);
            }
        }

        JPanel buttonsPanel = new JPanel();
        JButton moveBtn = new JButton("Move");
        buttonsPanel.add(moveBtn);
        mainFrame.add(buttonsPanel);


        moveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (boardSquare.numberOfSelectedSquares == 2) {
                    try {

                            player.makeMove(boardSquare.fromRow, boardSquare.fromColumn, boardSquare.toRow, boardSquare.toColumn);
                        boardSquare.numberOfSelectedSquares = 0;

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        new Thread(player).start();

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }



    static class Client implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        int playerNumber = 0;
        String playerColor;
        int turn = 0;

        public Client() {
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

        public void makeMove(int row,int column, int destRow,int destColumn ) throws IOException {
            out.println("move " +row +""+column+" " + destRow+""+destColumn);

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
            System.out.println(Arrays.deepToString(boardArray));
            return boardArray;
        }

        public static void updateBoard(String[][] newBoard) {
            boardPanel.removeAll();
            for (int row = 0; row < 8; row++) {
                for (int column = 0; column < 8; column++) {
                    String color = "";
                    if (newBoard[row][column].equals("w"))
                        color = "Red";
                    else if (newBoard[row][column].equals("b"))
                        color = "Black";
                    else
                        color = "Blank";
                    if ((row + column) % 2 == 0) {
                        if (row <= 2)
                            board[row][column] = new boardSquare(row, column, color);
                        else if (row >= 5)
                            board[row][column] = new boardSquare(row, column, color);
                        else
                            board[row][column] = new boardSquare(row, column, color);
                    } else
                        board[row][column] = new boardSquare(row, column, color);
                    boardPanel.add(board[row][column]);

                }
            }
            boardPanel.revalidate();
            boardPanel.repaint();

        }



        @Override
        public void run() {
            try {
                updateBoard(readBoard());
                while (true) {
                    if (turn % 2 == playerNumber) {
                        String resp = null;
                        while (resp == null || !resp.equals("t"))
                            resp = in.readLine();

                        turn++;
                        updateBoard(readBoard());
                    }
                    else{
                        updateBoard(readBoard());
                    }

                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        }
    }






class Main{
    public static void main(String[] args){
        CheckersGui game = new CheckersGui();
    }
}