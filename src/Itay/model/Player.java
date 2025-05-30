package Itay.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    private String name;
    private boolean isLocal;
    private List<Piece> pieces;

    public Player(String name, boolean isLocal) {
        this.name = name;
        this.isLocal = isLocal;
        this.pieces = new ArrayList<>();
    }

    public String getName() { return name; }
    public boolean isLocal() { return isLocal; }
    public List<Piece> getPieces() { return pieces; }

    public void addPiece(Piece p) {
        pieces.add(p);
    }

    public void removePiece(Piece p) {
        pieces.remove(p);
    }
}
