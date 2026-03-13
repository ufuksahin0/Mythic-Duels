package com.cardgame.controller;

import com.cardgame.model.*;
import com.cardgame.util.HighScoreManager;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameController {
    private HumanPlayer human;
    private ComputerPlayer computer;
    private GameViewListener view;
    private HighScoreManager highScoreManager;
    private boolean isPlayerTurn;
    private int round;

    public interface GameViewListener {
        void onGameStateUpdated();
        void onGameEnded(String winner);
        void onLogMessage(String message);
        void onCardPlayed(Card c, boolean isPlayer);
    }

    public GameController(GameViewListener view) {
        this.view = view;
        this.highScoreManager = new HighScoreManager();
    }

    public void startGame(String playerName, Deck.DeckType playerDeckType, Deck.DeckType computerDeckType) {
        human = new HumanPlayer(playerName, playerDeckType);
        computer = new ComputerPlayer("Computer", computerDeckType);
        round = 1;
        isPlayerTurn = true;
        
        view.onLogMessage("Game Started! Round " + round);
        view.onGameStateUpdated();
    }
    
    public void saveGame() {
        try (java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(new java.io.FileOutputStream("savedgame.dat"))) {
            oos.writeObject(new GameState(human, computer, round, isPlayerTurn));
            view.onLogMessage("Game Saved!");
        } catch (java.io.IOException e) {
            e.printStackTrace();
            view.onLogMessage("Error saving game.");
        }
    }
    
    public boolean loadGame() {
        try (java.io.ObjectInputStream ois = new java.io.ObjectInputStream(new java.io.FileInputStream("savedgame.dat"))) {
            GameState state = (GameState) ois.readObject();
            this.human = state.getHuman();
            this.computer = state.getComputer();
            this.round = state.getRound();
            this.isPlayerTurn = state.isPlayerTurn();
            
            view.onLogMessage("Game Loaded! Round " + round);
            view.onGameStateUpdated();
            return true;
        } catch (Exception e) {
            view.onLogMessage("Error loading game or no save file.");
            return false;
        }
    }

    public void playHumanCard(Card card) {
        if (!isPlayerTurn) return;

        // check if player has enough mana
        if (human.playCard(card)) {
            view.onCardPlayed(card, true);
            view.onLogMessage(human.getName() + " played " + card.getName());
            
            isPlayerTurn = false;
            view.onGameStateUpdated();
            
            // Wait a bit before computer plays
            Timer timer = new Timer(1500, e -> processComputerTurn(card));
            timer.setRepeats(false);
            timer.start();
        } else {
            view.onLogMessage("Not enough mana!");
        }
    }
    
    public void skipTurn() {
         if (!isPlayerTurn) return;
         view.onLogMessage(human.getName() + " skipped turn.");
         isPlayerTurn = false;
         view.onGameStateUpdated();
            
         // Wait a bit
         Timer timer = new Timer(1000, e -> processComputerTurn(null));
         timer.setRepeats(false);
         timer.start();
    }

    private void processComputerTurn(Card playerCard) {
        Card computerCard = computer.determineMove(playerCard);
        
        if (computerCard != null) {
            computer.playCard(computerCard);
            view.onCardPlayed(computerCard, false);
            view.onLogMessage("Computer played " + computerCard.getName());
            
            resolveCombat(playerCard, computerCard);
        } else {
             view.onLogMessage("Computer skipped turn.");
             if (playerCard != null) {
                 // Computer didn't block
                 view.onLogMessage("Computer took direct damage from " + playerCard.getName());
                 computer.takeDamage(playerCard.getAttack());
             }
        }

        checkWinCondition();
        startNextRound();
    }

    private void resolveCombat(Card playerCard, Card computerCard) {
        if (playerCard == null && computerCard == null) return;
        
        if (playerCard == null) {
             // Player skipped, Computer attacks directly
             human.takeDamage(computerCard.getAttack());
             view.onLogMessage("Computer attacks directly for " + computerCard.getAttack() + " damage!");
             return;
        }

        // Logic: Attack - Defense
        int pDmg = playerCard.getAttack() - computerCard.getDefense();
        int cDmg = computerCard.getAttack() - playerCard.getDefense();
        
        if (pDmg < 0) pDmg = 0;
        if (cDmg < 0) cDmg = 0;

        if (pDmg > 0) {
            computer.takeDamage(pDmg);
            view.onLogMessage("Computer took " + pDmg + " damage!");
        } else {
            view.onLogMessage("Computer blocked the attack!");
        }

        if (cDmg > 0) {
            human.takeDamage(cDmg);
            view.onLogMessage("Player took " + cDmg + " damage!");
        } else {
            view.onLogMessage("Player blocked the attack!");
        }
    }

    private void startNextRound() {
        if (!human.isAlive() || !computer.isAlive()) return;

        round++;
        human.startTurn();
        computer.startTurn();
        isPlayerTurn = true;
        
        view.onLogMessage("--- Round " + round + " ---");
        view.onGameStateUpdated();
    }

    private void checkWinCondition() {
        if (!human.isAlive()) {
            view.onGameEnded("Computer");
        } else if (!computer.isAlive()) {
            // score is health * 10
            highScoreManager.addScore(human.getName(), human.getHealth() * 10); 
            view.onGameEnded(human.getName());
        } else if (human.getDeckSize() == 0 && human.getHand().isEmpty()) {
             view.onGameEnded("Computer (Deck Empty)");
        }
    }

    public HumanPlayer getHuman() { return human; }
    public ComputerPlayer getComputer() { return computer; }
    public boolean isPlayerTurn() { return isPlayerTurn; }
    public HighScoreManager getHighScoreManager() { return highScoreManager; }
}
