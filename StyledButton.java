package com.cardgame.view.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StyledButton extends JButton {
    private Color normalColor = new Color(70, 130, 180); // Steel Blue
    private Color hoverColor = new Color(100, 149, 237); // Cornflower Blue
    private Color pressedColor = new Color(25, 25, 112); // Midnight Blue
    private boolean isHovering = false;
    private boolean isPressed = false;

    public StyledButton(String text) {
        super(text);
        super.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setOpaque(false);
        this.setForeground(Color.WHITE);
        this.setFont(new Font("Segoe UI", Font.BOLD, 16));
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add padding
        this.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovering = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovering = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Choose color based on state
        if (isPressed) {
            g2.setColor(pressedColor);
        } else if (isHovering) {
            g2.setColor(hoverColor);
        } else {
            g2.setColor(normalColor);
        }

        // Draw rounded rectangle background
        g2.fillRoundRect(0, 0, w, h, 20, 20);

        // Draw Text
        FontMetrics fm = g2.getFontMetrics();
        int x = (w - fm.stringWidth(getText())) / 2;
        int y = (h - fm.getHeight()) / 2 + fm.getAscent();

        g2.setColor(getForeground());
        g2.drawString(getText(), x, y);

        g2.dispose();
    }
}
