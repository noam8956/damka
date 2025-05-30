package Itay.model;

import java.io.Serializable;

public class Piece implements Serializable {
    private int row, col;
    private boolean isKing;
    private Player owner;

    public Piece(int row, int col, Player owner) {
        this.row = row;
        this.col = col;
        this.owner = owner;
        this.isKing = false;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public boolean isKing() { return isKing; }
    public Player getOwner() { return owner; }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public void makeKing() {
        this.isKing = true;
    }
}

