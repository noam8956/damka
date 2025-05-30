import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

class CheckersGui {
    public static Client player = new Client();
    public static boardSquare2[][] board = new boardSquare2[8][8];
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
                        board[row][column] = new boardSquare2(row, column, "Red");
                    else if (row >= 5)
                        board[row][column] = new boardSquare2(row, column, "Black");
                    else
                        board[row][column] = new boardSquare2(row, column, "Blank");
                } else
                    board[row][column] = new boardSquare2(row, column, "Blank");
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
                if (boardSquare2.numberOfSelectedSquares == 2) {
                    try {

                            player.makeMove(boardSquare2.fromRow, boardSquare2.fromColumn, boardSquare2.toRow, boardSquare2.toColumn);

                        for(int i =0; i<board.length;i++)
                            for(int j = 0; j<board[i].length;j++)
                                board[i][j].setSelected(false);

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
        String playerColor;
         int turn = 0;
         int playerNumber = 0;
         static boolean isMyTurn = false;



        public Client() {
            try {
                this.socket = new Socket("localhost", 5000);
                System.out.println("Connected to Chess Server");
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                Scanner scanner = new Scanner(System.in);

                String playerDeclaration = in.readLine();
                if (playerDeclaration.equals("first")) {
                    isMyTurn = true;
                    playerNumber = 0;
                    this.playerColor = "Red";
                    System.out.println("You are the first player");
                    System.out.println("waiting for second player");
                } else if (playerDeclaration.equals("second")) {
                    playerNumber = 1;
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

        public void updateBoard(String[][] newBoard) {
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
                            board[row][column] = new boardSquare2(row, column, color);
                        else if (row >= 5)
                            board[row][column] = new boardSquare2(row, column, color);
                        else
                            board[row][column] = new boardSquare2(row, column, color);
                    } else
                        board[row][column] = new boardSquare2(row, column, color);
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
                        isMyTurn=true;
                        System.out.println("my turn starts");
                        String resp = null;
                        while (resp == null || !resp.equals("t")) {
                            resp = in.readLine();  // Receive response from the server
                            System.out.println("Received response: " + resp);
                        }

                        // Perform move logic
                        updateBoard(readBoard());  // Update the board after the move

                        // Increment turn after making the move

                        System.out.println("my turn end");
                        isMyTurn = false;
                    } else {
                        // Wait until the turn changes
                        updateBoard(readBoard());
                    }
                    turn++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        }


    static class boardSquare2 extends JComponent {
        private int row; //x position of the rectangle measured from top left corner
        private int column; //y position of the rectangle measured from top left corner

        private boolean isBlack = false;
        private boolean isRed = false;

        private boolean isSelected = false;

        public static int numberOfSelectedSquares = 0, fromRow = 0, fromColumn = 0, toRow = 0, toColumn = 0;
        //static Stack<Piece> selection = new Stack<Piece>();

        public boardSquare2(int row, int column, String type) {
            this.row = row;
            this.column = column;
            if (type.equals("Black")) {
                isBlack = true;
                isRed = false;
            } else if (type.equals("Red")) {
                isRed = true;
                isBlack = false;
            } else if (type.equals("Blank")) {
                isBlack = false;
                isRed = false;
            }

            if ((row + column) % 2 == 0)
                this.setBackground(Color.white);
            else
                this.setBackground(Color.BLACK);

            // Set preferred size for the square
            setPreferredSize(new Dimension(50, 50));

            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    System.out.println("isMyTurn: " + player.isMyTurn);
                    // Handle the click event
                    if(player.isMyTurn)
                    {
                        if (isSelected) {
                            setSelected(false);
                        } else if (!isSelected) {
                            setSelected(true);
                        }
                    }


                    repaint();
                    System.out.println("Clicked on square: Row = " + row + ", Column = " + column);
                    // You can add any logic here to handle the click, such as selecting or moving pieces
                }
            });

        }

        public void setSelected(boolean status) {
            if (status != isSelected){
                if(status) {
                    isSelected = true;
                    if (numberOfSelectedSquares == 0) {
                        fromRow = row;
                        fromColumn = column;
                        numberOfSelectedSquares++;
                    } else if (numberOfSelectedSquares == 1) {
                        toColumn = column;
                        toRow = row;
                        numberOfSelectedSquares++;
                    }
                }
                else{
                    isSelected = false;
                    numberOfSelectedSquares--;
                }
                repaint();
            }


        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            // Draw the background color of the square
            if ((row + column) % 2 == 0) {
                g2.setColor(Color.WHITE);
            } else {
                g2.setColor(Color.BLACK);
            }
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Draw the piece if there is one (Red or Black)
            if (isBlack) {
                g2.setColor(Color.BLACK);
                g2.fillOval(5, 5, 40, 40);
            } else if (isRed) {
                g2.setColor(Color.RED);
                g2.fillOval(5, 5, 40, 40);
            }

            // Draw border if selected
            if (this.isSelected) {
                g2.setColor(Color.BLUE);
                g2.setStroke(new BasicStroke(3)); // Set bold border
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1); // Draw border with 1 pixel margin
            } else {
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(1)); // Set normal border
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1); // Draw normal border
            }
        }

        public void changeType(String newType) {
            if (newType.equals("Red")) {
                isBlack = false;
                isRed = true;
            } else if (newType.equals("Black")) {
                isBlack = true;
                isRed = false;
            } else if (newType.equals("Blank")) {
                isBlack = false;
                isRed = false;
            }

            repaint();

        }

        public String getType() {
            if (isBlack)
                return "Black";
            else if (isRed)
                return "Red";
            else
                return "Blank";
        }
    }



    }



class Piece2 {
    int row;
    int column;

    public Piece2(int row, int column) {
        this.row = row;
        this.column = column;
    }
}




class Main{
    public static void main(String[] args){
        CheckersGui game = new CheckersGui();
    }
}