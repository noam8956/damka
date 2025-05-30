package Itay.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GameFrame {
    public BoardPanel boardPanel;

    public GameFrame(){

    }

    public void setupFrame(){
        // Creating instance of JFrame
        JFrame mainFrame = new JFrame();
        mainFrame.setTitle( "Noam Checkers");
        mainFrame.setSize(500, 600);

        // Creating boardPanel for the board layout
        GridLayout boardLayout = new GridLayout(8, 8);

        // Creating the main layout
        BoxLayout mainLayout = new BoxLayout(mainFrame.getContentPane(), BoxLayout.Y_AXIS);
        mainFrame.setLayout(mainLayout);



        JLabel playerLabel = new JLabel("you are player " );
        playerLabel.setForeground(Color.BLUE);
        playerLabel.setFont(new Font("Arial",Font.PLAIN,24));
        JLabel playerColorLabel = new JLabel( " color is " );
        playerColorLabel.setForeground( Color.RED);
        playerColorLabel.setFont(new Font("Arial",Font.PLAIN,24));

        mainFrame.add(playerLabel);
        mainFrame.add(playerColorLabel);

        // Creating and adding board to mainFrame
        mainFrame.add(boardPanel);


        JPanel buttonsPanel = new JPanel();
        JButton moveBtn = new JButton("Move");
        buttonsPanel.add(moveBtn);
        mainFrame.add(buttonsPanel);


        moveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    public void startServer(){

    }
}
