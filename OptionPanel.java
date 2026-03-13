package com.cardgame.view;

import com.cardgame.util.GameConfig;

import javax.swing.*;
import java.awt.*;

public class OptionPanel extends JPanel {
    private MainFrame mainFrame;

    public OptionPanel(MainFrame frame) {
        this.mainFrame = frame;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel title = new JLabel("Settings");
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Difficulty
        JPanel diffPanel = new JPanel();
        diffPanel.add(new JLabel("Difficulty: "));
        JComboBox<GameConfig.Difficulty> diffCombo = new JComboBox<>(GameConfig.Difficulty.values());
        diffCombo.setSelectedItem(GameConfig.getDifficulty());
        diffCombo.addActionListener(e -> GameConfig.setDifficulty((GameConfig.Difficulty) diffCombo.getSelectedItem()));
        diffPanel.add(diffCombo);
        diffPanel.setMaximumSize(new Dimension(300, 50));

        // Animations
        JCheckBox animCheck = new JCheckBox("Enable Animations");
        animCheck.setSelected(GameConfig.isAnimationsEnabled());
        animCheck.setAlignmentX(Component.CENTER_ALIGNMENT);
        animCheck.addActionListener(e -> GameConfig.setAnimationsEnabled(animCheck.isSelected()));

        JButton btnBack = new JButton("Back to Menu");
        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBack.addActionListener(e -> mainFrame.showMenu());

        this.add(title);
        this.add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(diffPanel);
        this.add(Box.createRigidArea(new Dimension(0, 20)));
        this.add(animCheck);
        this.add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(btnBack);
    }
}
