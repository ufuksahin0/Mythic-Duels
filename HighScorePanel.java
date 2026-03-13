package com.cardgame.view;

import com.cardgame.util.HighScoreManager;
import com.cardgame.util.HighScoreManager.HighScore;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HighScorePanel extends JPanel {
    private MainFrame mainFrame;
    private JTextArea scoreArea;
    private HighScoreManager manager;

    public HighScorePanel(MainFrame frame) {
        this.mainFrame = frame;
        this.manager = new HighScoreManager(); // Reloads from file
        this.setLayout(new BorderLayout());
        
        JLabel title = new JLabel("High Scores", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        this.add(title, BorderLayout.NORTH);
        
        scoreArea = new JTextArea();
        scoreArea.setEditable(false);
        scoreArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        this.add(new JScrollPane(scoreArea), BorderLayout.CENTER);
        
        JButton btnBack = new JButton("Back to Menu");
        btnBack.addActionListener(e -> mainFrame.showMenu());
        this.add(btnBack, BorderLayout.SOUTH);
    }
    
    public void refresh() {
        manager = new HighScoreManager(); // Refresh data
        List<HighScore> scores = manager.getScores();
        StringBuilder sb = new StringBuilder();
        int rank = 1;
        for (HighScore s : scores) {
            sb.append(rank++).append(". ").append(s.toString()).append("\n");
        }
        if (scores.isEmpty()) {
            sb.append("No high scores yet!");
        }
        scoreArea.setText(sb.toString());
    }
}
