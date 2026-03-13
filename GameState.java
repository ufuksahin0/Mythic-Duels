package com.cardgame.model;

import java.io.Serializable;

public class GameState implements Serializable {
    private HumanPlayer human;
    private ComputerPlayer computer;
    private int round;
    private boolean isPlayerTurn;

    public GameState(HumanPlayer human, ComputerPlayer computer, int round, boolean isPlayerTurn) {
        this.human = human;
        this.computer = computer;
        this.round = round;
        this.isPlayerTurn = isPlayerTurn;
    }

    public HumanPlayer getHuman() { return human; }
    public ComputerPlayer getComputer() { return computer; }
    public int getRound() { return round; }
    public boolean isPlayerTurn() { return isPlayerTurn; }
}
