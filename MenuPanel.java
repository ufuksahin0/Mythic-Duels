package com.cardgame.view;

import com.cardgame.view.ui.StyledButton;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    private MainFrame mainFrame;

    public MenuPanel(MainFrame frame) {
        this.mainFrame = frame;
        this.setLayout(new GridBagLayout()); 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 0, 10, 0); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Game Logo
        JLabel logo = new JLabel("⚔️", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        this.add(logo, gbc);

        // Game Title
        JLabel title = new JLabel("MYTHIC DUELS", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 64));
        title.setForeground(new Color(255, 215, 0)); // Gold color
        // title.setBorder(BorderFactory.createLineBorder(Color.RED)); // Debug border
        
        // Game Subtitle
        JLabel subtitle = new JLabel("Strategy Card Game", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        subtitle.setForeground(Color.LIGHT_GRAY);
        
        gbc.insets = new Insets(0, 0, 5, 0); 
        this.add(title, gbc);
        gbc.insets = new Insets(0, 0, 40, 0);
        this.add(subtitle, gbc);
        
        // Menu Buttons
        StyledButton newGameButton = new StyledButton("New Game");
        newGameButton.setPreferredSize(new Dimension(250, 50));
        newGameButton.addActionListener(e -> mainFrame.showGame());
        
        StyledButton loadGameButton = new StyledButton("Load Game");
        loadGameButton.setPreferredSize(new Dimension(250, 50));
        loadGameButton.addActionListener(e -> mainFrame.loadGame());
        
        StyledButton rulesButton = new StyledButton("Rules");
        rulesButton.setPreferredSize(new Dimension(250, 50));
        rulesButton.addActionListener(e -> showRules());

        StyledButton highScoresButton = new StyledButton("High Scores");
        highScoresButton.setPreferredSize(new Dimension(250, 50));
        highScoresButton.addActionListener(e -> mainFrame.showHighScores());

        StyledButton settingsButton = new StyledButton("Settings");
        settingsButton.setPreferredSize(new Dimension(250, 50));
        settingsButton.addActionListener(e -> mainFrame.showOptions());

        StyledButton exitButton = new StyledButton("Exit");
        exitButton.setPreferredSize(new Dimension(250, 50));
        exitButton.addActionListener(e -> System.exit(0));

        gbc.insets = new Insets(10, 0, 10, 0); 
        this.add(newGameButton, gbc);
        this.add(loadGameButton, gbc);
        this.add(rulesButton, gbc);
        this.add(highScoresButton, gbc);
        this.add(settingsButton, gbc);
        this.add(exitButton, gbc);
        
        // Footer info
        JLabel footer = new JLabel("Java Term Project Spring 2026", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.setForeground(Color.GRAY);
        gbc.insets = new Insets(40, 0, 0, 0);
        this.add(footer, gbc);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Background gradient
        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();
        Color c1 = new Color(20, 30, 48);
        Color c2 = new Color(36, 59, 85);
        GradientPaint gp = new GradientPaint(0, 0, c1, w, h, c2);
        g2.setPaint(gp);
        g2.fillRect(0, 0, w, h);
    }
    
    private void showRules() {
        String rules = "Game Rules:\n\n" +
                "1. Each player starts with 30 Health and 1 Mana.\n" +
                "2. Mana increases by 1 each round (max 10).\n" +
                "3. Play cards from your hand if you have enough Mana.\n" +
                "4. Higher Attack damages the opponent. Defense reduces damage.\n" +
                "5. Reduce opponent's Health to 0 to win.";
        JOptionPane.showMessageDialog(this, rules, "Rules", JOptionPane.INFORMATION_MESSAGE);
    }
}
