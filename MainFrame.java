package com.cardgame.view;

import com.cardgame.controller.GameController;
import com.cardgame.model.Deck.DeckType;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    private HighScorePanel highScorePanel;
    private OptionPanel optionPanel;
    private GameController gameController;

    public MainFrame() {
        setTitle("Java Card Game - Extra Term Project");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        menuPanel = new MenuPanel(this);
        gamePanel = new GamePanel(this);
        highScorePanel = new HighScorePanel(this);
        optionPanel = new OptionPanel(this);
        
        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(gamePanel, "GAME");
        mainPanel.add(highScorePanel, "SCORES");
        mainPanel.add(optionPanel, "OPTIONS");
        
        this.add(mainPanel);
    }
    
    public void showMenu() {
        cardLayout.show(mainPanel, "MENU");
    }
    
    public void showGame() {
        // Initialize new game
        String playerName = JOptionPane.showInputDialog(this, "Enter Player Name:", "Player");
        if (playerName == null || playerName.trim().isEmpty()) playerName = "Player 1";
        
        // Select Deck
        DeckType[] types = DeckType.values();
        DeckType playerDeck = (DeckType) JOptionPane.showInputDialog(this, 
            "Select Deck:", "Deck Selection", 
            JOptionPane.QUESTION_MESSAGE, null, types, types[0]);
            
        if (playerDeck == null) return; // Cancelled
        
        // Random computer deck
        DeckType computerDeck = types[(int)(Math.random() * types.length)];
        
        gameController = new GameController(gamePanel);
        gamePanel.setController(gameController);
        gameController.startGame(playerName, playerDeck, computerDeck);
        
        cardLayout.show(mainPanel, "GAME");
    }
    
    public void loadGame() {
        gameController = new GameController(gamePanel);
        gamePanel.setController(gameController);
        if (gameController.loadGame()) {
            cardLayout.show(mainPanel, "GAME");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to load game.");
        }
    }
    
    public void showOptions() {
        cardLayout.show(mainPanel, "OPTIONS");
    }

    
    public void showHighScores() {
        highScorePanel.refresh();
        cardLayout.show(mainPanel, "SCORES");
    }
    
    public void showGameOver(String winner) {
        int option = JOptionPane.showConfirmDialog(this, 
                "Winner: " + winner + "\nPlay Again?", 
                "Game Over", 
                JOptionPane.YES_NO_OPTION);
                
        if (option == JOptionPane.YES_OPTION) {
            showGame();
        } else {
            showMenu();
        }
    }
}
