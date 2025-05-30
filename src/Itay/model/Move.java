package Itay.model;

import java.io.Serializable;

public class Move implements Serializable {
    private int startRow, startCol;
    private int endRow, endCol;
    private boolean isCapture;

    public Move(int startRow, int startCol, int endRow, int endCol, boolean isCapture) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
        this.isCapture = isCapture;
    }

    public int getStartRow() { return startRow; }
    public int getStartCol() { return startCol; }
    public int getEndRow() { return endRow; }
    public int getEndCol() { return endCol; }
    public boolean isCapture() { return isCapture; }
}

