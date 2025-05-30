package Itay.gui;

import Itay.controller.GameController;
import Itay.model.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class BoardPanel extends JComponent {
    public GameController controller;

    int tileSize;

    public BoardPanel(){

    }

    @Override
    public void paintComponent(Graphics g){}

    public void mousePressed(MouseEvent e){

    }

    public void highlightPossibleMoves(Piece p){

    }
}
