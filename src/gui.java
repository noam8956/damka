import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Stack;

public class gui {
    public static Client player = new Client();
    public static boardSquare[][] board = new boardSquare[8][8];
    public static JPanel boardPanel = new JPanel();

    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {


        // Creating instance of JFrame
        JFrame mainFrame = new JFrame();
        mainFrame.setTitle("you are player " + (player.playerNumber + 1) + " color is " + player.playerColor);
        mainFrame.setSize(500, 600);

        // Creating boardPanel for the board layout
        GridLayout boardLayout = new GridLayout(8, 8);
        boardPanel.setLayout(boardLayout);

        // Creating the main layout
        BoxLayout mainLayout = new BoxLayout(mainFrame.getContentPane(), BoxLayout.Y_AXIS);
        mainFrame.setLayout(mainLayout);

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
                    String[][] newBoard = null;
                    try {
                        while(newBoard==null)
                            newBoard = player.makeMove(boardSquare.fromRow, boardSquare.fromColumn, boardSquare.toRow, boardSquare.toColumn);
                        boardSquare.numberOfSelectedSquares = 0;

                        updateBoard(newBoard);
                        updateBoard(player.readBoard());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                       ex.printStackTrace();
                    }
                }
            }
        });
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);

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

}


class boardSquare extends JComponent {
    private int row; //x position of the rectangle measured from top left corner
    private int column; //y position of the rectangle measured from top left corner

    private boolean isBlack = false;
    private boolean isRed = false;

    private boolean isSelected = false;

    public static int numberOfSelectedSquares = 0, fromRow = 0, fromColumn = 0, toRow = 0, toColumn = 0;
    //static Stack<Piece> selection = new Stack<Piece>();

    public boardSquare(int row, int column, String type) {
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

                // Handle the click event
                if (isSelected) {
                    numberOfSelectedSquares--;
                    isSelected = false;
                } else if (!isSelected) {
                    if (numberOfSelectedSquares == 0) {
                        isSelected = true;
                        fromRow = row;
                        fromColumn = column;
                        numberOfSelectedSquares++;
                    } else if (numberOfSelectedSquares == 1) {
                        isSelected = true;
                        toColumn = column;
                        toRow = row;
                        numberOfSelectedSquares = 2;
                    }
                }


                repaint();
                System.out.println("Clicked on square: Row = " + row + ", Column = " + column);
                // You can add any logic here to handle the click, such as selecting or moving pieces
            }
        });

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

class Piece {
    int row;
    int column;

    public Piece(int row, int column) {
        this.row = row;
        this.column = column;
    }
}