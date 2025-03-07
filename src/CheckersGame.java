public class CheckersGame {

    private String[][] board = new String[8][8];

    public CheckersGame(){
        this.board = createBoard();
        System.out.println("printing board...");
        System.out.println(boardToString());

    }

    public char checkForWin(){
        int blackPieces = 0;
        int whitePieces = 0;
        for (int row = 0; row<board.length;row++){
            for(int column = 0; column<board[0].length;column++){
                if(board[row][column].charAt(0) == 'b')
                    blackPieces++;
                if(board[row][column].charAt(0) == 'w')
                    whitePieces++;
            }
        }
        if (blackPieces == 0)
            return 'w';
        if(whitePieces == 0)
            return 'b';
        return 'c';
    }

    public String[][] createBoard(){
        String[][] board = new String[8][8];
        for(int row = 0; row< board.length;row++ ){
            for (int column = 0; column<board[0].length;column++){
                board[row][column] = " ";
                if(row%2==column%2) {
                    if (row <= 2)
                        board[row][column] = "w";
                    if (row >= 5)
                        board[row][column] = "b";
                }
            }
        }
        return board;
    }

    public String boardToString() {
        String boardString = "";
        boardString = "  |0|1|2|3|4|5|6|7|\n";
        for (int row = 0; row < this.board.length; row++) {
            boardString += row + " |";
            for (int column = 0; column < this.board[0].length; column++) {
                boardString+=this.board[row][column] + "|";
            }
            boardString +='\n';
        }
        return boardString;
    }


    public boolean movePiece(int pieceRow, int pieceColumn, int targetRow, int targetColumn,char color) {
        // Check if the move is valid
        if (validMove(pieceRow, pieceColumn, targetRow, targetColumn) && this.board[pieceRow][pieceColumn].charAt(0) == color) {

            // Check if the move is a jump (capture)
            int rowDiff = Math.abs(targetRow - pieceRow);
            int colDiff = Math.abs(targetColumn - pieceColumn);

            // If it's a jump, remove the opponent's piece
            if (rowDiff == 2 && colDiff == 2) {
                int middleRow = (pieceRow + targetRow) / 2;
                int middleColumn = (pieceColumn + targetColumn) / 2;

                // Remove the opponent's piece from the board
                board[middleRow][middleColumn] = " ";

            }

            // Move the piece to the target position
            board[targetRow][targetColumn] = board[pieceRow][pieceColumn];
            board[pieceRow][pieceColumn] = " "; // Clear the original position

            // Handle piece promotion to queen
            String piece = board[targetRow][targetColumn];
            if (piece.equals("w") && targetRow == 0) {
                board[targetRow][targetColumn] = "wQ"; // White piece becomes a queen
            } else if (piece.equals("b") && targetRow == 7) {
                board[targetRow][targetColumn] = "bQ"; // Black piece becomes a queen
            }

            System.out.println(boardToString());
            return true;
        } else {
            return false;
        }
    }


    public boolean validMove(int pieceRow, int pieceColumn, int targetRow, int targetColumn) {
        // Ensure the target is within the bounds of the board
        if (targetRow < 0 || targetRow >= 8 || targetColumn < 0 || targetColumn >= 8 || this.board[pieceRow][pieceColumn].equals(" ")) {
            return false;
        }

        // Calculate the difference between the starting and target positions
        int rowDiff = Math.abs(targetRow - pieceRow);
        int colDiff = Math.abs(targetColumn - pieceColumn);

        // Ensure the move is diagonal
        if (rowDiff != colDiff) {
            return false;
        }

        // Check for a regular piece or a queen
        String piece = board[pieceRow][pieceColumn];
        boolean isWhite = piece.equals("w");
        boolean isBlack = piece.equals("b");

        // Check if the piece is a queen (using "wQ" or "bQ" for white or black queens)
        boolean isQueen = piece.equals("wQ") || piece.equals("bQ");

        // Regular move: Piece moves exactly 1 square diagonally
        if (!isQueen && rowDiff == 1) {
            // Ensure the target square is empty
            if (board[targetRow][targetColumn].equals(" ")) {
                return true;
            }
        }

        // Queen move: Piece can move multiple squares diagonally
        else if (isQueen) {
            // We need to check all squares between the start and target to ensure they are empty
            int rowDirection = targetRow > pieceRow ? 1 : -1;
            int colDirection = targetColumn > pieceColumn ? 1 : -1;

            // Check for all squares in between (must be empty)
            int currentRow = pieceRow + rowDirection;
            int currentCol = pieceColumn + colDirection;
            while (currentRow != targetRow && currentCol != targetColumn) {
                if (!board[currentRow][currentCol].equals(" ")) {
                    return false;  // A piece is blocking the path
                }
                currentRow += rowDirection;
                currentCol += colDirection;
            }

            // The target square must be empty
            if (board[targetRow][targetColumn].equals(" ")) {
                return true;
            }
        }

        // Jump move: The piece jumps over an opponent's piece (rowDiff = 2, colDiff = 2)
        else if (rowDiff == 2) {
            int middleRow = (pieceRow + targetRow) / 2;
            int middleColumn = (pieceColumn + targetColumn) / 2;

            // Ensure there is an opponent's piece in the middle
            if (!board[middleRow][middleColumn].equals(" ") &&
                    ((isWhite && board[middleRow][middleColumn].equals("b")) ||
                            (isBlack && board[middleRow][middleColumn].equals("w")))) {
                // Ensure the target square is empty
                if (board[targetRow][targetColumn].equals(" ")) {
                    return true;
                }
            }
        }

        return false;  // If no valid move conditions are met
    }

    public String[][] getBoard(){
        return board;
    }

}
