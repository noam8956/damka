package Itay.controller;

import Itay.model.*;
import Itay.network.Server;

public class GameController {

    public GameBoard board;

    public Player localPlayer;

    public Player remotePlayer;

    public Server server;

    public void handleLocalMove(int startRow, int startCol, int endRow, int endCol){}

    public void receiveRemoteMove(Move move){}

    public void checkEndGame(){}

}
