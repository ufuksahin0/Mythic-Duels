package com.cardgame.view;

import com.cardgame.controller.GameController;
import com.cardgame.model.Card;
import com.cardgame.model.Player;
import com.cardgame.view.ui.StyledButton;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GamePanel extends JPanel implements GameController.GameViewListener {
    private GameController controller;
    private MainFrame mainFrame;
    
    private JPanel computerPanel;
    private JPanel playerPanel;
    private JPanel boardPanel;
    private JPanel infoPanel;
    
    private JLabel computerStatsLabel;
    private JLabel playerStatsLabel;
    private JTextPane gameLog;
    private HTMLEditorKit htmlKit;
    private HTMLDocument htmlDoc;
    private StyledButton skipButton;
    private StyledButton saveButton;
    
    private JPanel computerHandPanel;
    private JPanel playerHandPanel;

    public GamePanel(MainFrame frame) {
        this.mainFrame = frame;
        this.setLayout(new BorderLayout());
        
        // Computer Area (Top)
        computerPanel = createTransparentPanel(new BorderLayout());
        computerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        computerStatsLabel = new JLabel("Health: 30 | Mana: 1");
        computerStatsLabel.setForeground(Color.WHITE);
        computerStatsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        computerPanel.add(computerStatsLabel, BorderLayout.NORTH);
        
        computerHandPanel = createTransparentPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        computerPanel.add(computerHandPanel, BorderLayout.CENTER);
        
        // Player Area (Bottom)
        playerPanel = createTransparentPanel(new BorderLayout());
        playerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        playerStatsLabel = new JLabel("Health: 30 | Mana: 1");
        playerStatsLabel.setForeground(Color.WHITE);
        playerStatsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        playerPanel.add(playerStatsLabel, BorderLayout.NORTH);
        
        playerHandPanel = createTransparentPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        playerPanel.add(playerHandPanel, BorderLayout.CENTER);
        
        // Board Area (Center)
        boardPanel = createTransparentPanel(new GridBagLayout()); 
        
        // Info/Log Area (Right)
        infoPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 180)); 
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        infoPanel.setOpaque(false);
        infoPanel.setPreferredSize(new Dimension(320, 0)); 
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel logTitle = new JLabel("Battle Log");
        logTitle.setForeground(Color.ORANGE);
        logTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        infoPanel.add(logTitle, BorderLayout.NORTH);

        gameLog = new JTextPane();
        gameLog.setEditable(false);
        gameLog.setOpaque(false);
        gameLog.setContentType("text/html");
        
        htmlKit = new HTMLEditorKit();
        htmlDoc = new HTMLDocument();
        gameLog.setEditorKit(htmlKit);
        gameLog.setDocument(htmlDoc);
        
        // Set up styles
        try {
            htmlKit.insertHTML(htmlDoc, htmlDoc.getLength(), 
                "<style>" +
                "body { font-family: 'Segoe UI', sans-serif; font-size: 11px; color: #E0E0E0; }" +
                ".p-act { color: #50FA7B; font-weight: bold; margin-top: 2px; }" + 
                ".c-act { color: #FF5555; font-weight: bold; margin-top: 2px; }" + 
                ".dmg { color: #FFB86C; font-weight: bold; margin-left: 10px; }" + 
                ".round { color: #F1FA8C; font-size: 13px; font-weight: bold; margin-top: 15px; margin-bottom: 5px; border-bottom: 1px solid #6272A4; }" + 
                ".info { color: #8BE9FD; font-style: italic; margin-left: 10px; }" + 
                "</style>", 0, 0, null);
        } catch (BadLocationException | IOException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(gameLog);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        
        // Buttons
        JPanel buttonPanel = createTransparentPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 5, 5, 0); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        skipButton = new StyledButton("Skip");
        skipButton.setFont(new Font("Segoe UI", Font.BOLD, 10)); 
        skipButton.setPreferredSize(new Dimension(60, 25)); 
        skipButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        skipButton.addActionListener(e -> controller.skipTurn());
        buttonPanel.add(skipButton, gbc);
        
        gbc.gridy = 1;
        saveButton = new StyledButton("Save");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 10)); 
        saveButton.setPreferredSize(new Dimension(60, 25)); 
        saveButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        saveButton.addActionListener(e -> controller.saveGame());
        buttonPanel.add(saveButton, gbc);
        
        gbc.gridy = 2;
        StyledButton menuButton = new StyledButton("Menu");
        menuButton.setFont(new Font("Segoe UI", Font.BOLD, 10)); 
        menuButton.setPreferredSize(new Dimension(60, 25)); 
        menuButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        menuButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Return to Main Menu? Unsaved progress will be lost.", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                mainFrame.showMenu();
            }
        });
        buttonPanel.add(menuButton, gbc);
        
        // Fill remaining space
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        buttonPanel.add(Box.createGlue(), gbc);
        
        JPanel contentPanel = createTransparentPanel(new BorderLayout());
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.EAST);
        
        infoPanel.add(contentPanel, BorderLayout.CENTER);
        
        this.add(computerPanel, BorderLayout.NORTH);
        this.add(boardPanel, BorderLayout.CENTER);
        this.add(playerPanel, BorderLayout.SOUTH);
        this.add(infoPanel, BorderLayout.EAST);
    }
    
    private JPanel createTransparentPanel(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setOpaque(false);
        return p;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Table Background
        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();
        
        Point center = new Point(w / 2, h / 2);
        float radius = Math.max(w, h);
        float[] dist = {0.0f, 1.0f};
        Color[] colors = {new Color(34, 139, 34), new Color(0, 100, 0)}; 
        RadialGradientPaint rgp = new RadialGradientPaint(center, radius, dist, colors);
        g2.setPaint(rgp);
        g2.fillRect(0, 0, w, h);
    }
    
    public void setController(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void onGameStateUpdated() {
        refreshStats();
        refreshHands();
    }

    @Override
    public void onGameEnded(String winner) {
        JOptionPane.showMessageDialog(this, "Game Over! Winner: " + winner);
        mainFrame.showGameOver(winner);
    }

    @Override
    public void onLogMessage(String message) {
        String cssClass = "";
        String prefix = "";

        if (message.contains("Player played")) {
            cssClass = "p-act";
            prefix = "🔵 "; 
        } else if (message.contains("Computer played")) {
            cssClass = "c-act";
            prefix = "🔴 "; 
        } else if (message.contains("damage")) {
            cssClass = "dmg";
            prefix = "💥 "; 
        } else if (message.contains("Round")) {
            cssClass = "round";
            message = message.replace("---", "").trim();
        } else if (message.contains("blocked")) {
            cssClass = "info";
            prefix = "🛡️ "; 
        } else if (message.contains("skipped")) {
            cssClass = "info";
            prefix = "⏭️ "; 
        }

        try {
            String html = "<div class='" + cssClass + "'>" + prefix + message + "</div>";
            htmlKit.insertHTML(htmlDoc, htmlDoc.getLength(), html, 0, 0, null);
            gameLog.setCaretPosition(htmlDoc.getLength());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCardPlayed(Card c, boolean isPlayer) {
        CardComponent cc = new CardComponent(c);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        
        JLabel ownerLabel = new JLabel(isPlayer ? "YOU" : "OPPONENT", SwingConstants.CENTER);
        ownerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        ownerLabel.setForeground(isPlayer ? new Color(100, 255, 100) : new Color(255, 100, 100));
        ownerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0)); 
        
        wrapper.add(ownerLabel, BorderLayout.NORTH);
        wrapper.add(cc, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0; 
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        
        if (isPlayer) {
             boardPanel.removeAll(); 
             gbc.gridx = 1; 
             gbc.insets = new Insets(0, 20, 0, 0); 
        } else {
             gbc.gridx = 0; 
             gbc.insets = new Insets(0, 0, 0, 20); 
        }
        
        boardPanel.add(wrapper, gbc);
        boardPanel.revalidate();
        boardPanel.repaint();
    }
    
    private void refreshStats() {
        if (controller == null) return;
        Player p = controller.getHuman();
        Player c = controller.getComputer();
        
        playerStatsLabel.setText(String.format("<html>PLAYER<br>Health: %d<br>Mana: %d/%d</html>", p.getHealth(), p.getMana(), p.getMaxMana()));
        computerStatsLabel.setText(String.format("<html>COMPUTER<br>Health: %d<br>Mana: %d/%d</html>", c.getHealth(), c.getMana(), c.getMaxMana()));
        
        skipButton.setEnabled(controller.isPlayerTurn());
    }
    
    private void refreshHands() {
        if (controller == null) return;
        
        // Player Hand
        playerHandPanel.removeAll();
        List<Card> pHand = controller.getHuman().getHand();
        for (Card c : pHand) {
            CardComponent cc = new CardComponent(c, e -> {
                if (controller.isPlayerTurn()) {
                    controller.playHumanCard(c);
                }
            });
            playerHandPanel.add(cc);
        }
        
        // Computer Hand
        computerHandPanel.removeAll();
        List<Card> cHand = controller.getComputer().getHand();
        for (Card c : cHand) {
            CardComponent cc = new CardComponent(c);
            cc.setHidden(true);
            computerHandPanel.add(cc);
        }
        
        playerHandPanel.revalidate();
        playerHandPanel.repaint();
        computerHandPanel.revalidate();
        computerHandPanel.repaint();
    }
    
    public void reset() {
        gameLog.setText("");
        boardPanel.removeAll();
        boardPanel.repaint();
    }
}
