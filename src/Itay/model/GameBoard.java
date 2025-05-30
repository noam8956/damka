package Itay.model;

public class GameBoard {
    private Piece[][] board;

    public static final int SIZE = 8;

    public GameBoard() {
        board = new Piece[SIZE][SIZE];
    }

    public void initialize(Player player1, Player player2) {
        // Player 1 (top rows)
        for (int row = 0; row < 3; row++) {
            for (int col = (row + 1) % 2; col < SIZE; col += 2) {
                Piece p = new Piece(row, col, player1);
                board[row][col] = p;
                player1.addPiece(p);
            }
        }

        // Player 2 (bottom rows)
        for (int row = SIZE - 1; row >= SIZE - 3; row--) {
            for (int col = (row + 1) % 2; col < SIZE; col += 2) {
                Piece p = new Piece(row, col, player2);
                board[row][col] = p;
                player2.addPiece(p);
            }
        }
    }

    public Piece getPieceAt(int row, int col) {
        return board[row][col];
    }

    public void movePiece(Piece piece, int newRow, int newCol) {
        board[piece.getRow()][piece.getCol()] = null;
        piece.setPosition(newRow, newCol);
        board[newRow][newCol] = piece;

        // Check for kinging
        if (!piece.isKing() && (newRow == 0 || newRow == SIZE - 1)) {
            piece.makeKing();
        }
    }

    public void removePiece(Piece piece) {
        board[piece.getRow()][piece.getCol()] = null;
        piece.getOwner().removePiece(piece);
    }

    public boolean isInsideBoard(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }
}

