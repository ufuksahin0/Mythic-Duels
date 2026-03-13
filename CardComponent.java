package com.cardgame.view;

import com.cardgame.model.Card;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CardComponent extends JPanel {
    private Card card;
    private boolean isHidden;
    private ActionListener onClickListener;
    private BufferedImage cardImage;

    public CardComponent(Card card, ActionListener onClickListener) {
        this.card = card;
        this.onClickListener = onClickListener;
        Dimension size = new Dimension(120, 170);
        this.setPreferredSize(size);
        this.setMinimumSize(size); // Force minimum size
        this.setMaximumSize(size);
        this.setOpaque(false);
        
        loadCardImage();
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onClickListener != null && !isHidden) {
                    onClickListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "CARD_CLICKED"));
                }
            }
        });
    }

    private void loadCardImage() {
        try {
            // Check for png or jpg
            String[] extensions = {".png", ".jpg", ".jpeg"};
            for (String ext : extensions) {
                File imgFile = new File("resources/images/" + card.getName() + ext);
                if (imgFile.exists()) {
                    System.out.println("Loading image from: " + imgFile.getAbsolutePath());
                    cardImage = ImageIO.read(imgFile);
                    return;
                }
            }
            System.out.println("No image found for " + card.getName() + ", using default art.");
        } catch (IOException e) {
            System.err.println("Error loading image for " + card.getName());
            e.printStackTrace();
        }
    }
    
    public CardComponent(Card card) {
        this(card, null);
    }
    
    public void setHidden(boolean hidden) {
        this.isHidden = hidden;
        this.repaint();
    }
    
    public Card getCard() {
        return card;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    
            int w = getWidth();
            int h = getHeight();
            int arc = 15;
    
            if (isHidden) {
                drawCardBack(g2, w, h, arc);
                return;
            }
    
                    // 1. Card Background
                    Color topColor = new Color(255, 250, 240); // Default Cream
                    Color bottomColor = new Color(230, 220, 210); // Default Beige
            
                    String name = card.getName();
                    if (name.contains("Dragon") || name.contains("Fire") || name.contains("Berserker")) {
                        topColor = new Color(255, 230, 230); // Light Red
                        bottomColor = new Color(220, 180, 180);
                    } else if (name.contains("Knight") || name.contains("Soldier") || name.contains("Guardian")) {
                        topColor = new Color(240, 248, 255); // Alice Blue
                        bottomColor = new Color(190, 210, 230); // Steel Blueish
                    } else if (name.contains("Wizard") || name.contains("Magic") || name.contains("Spirit")) {
                        topColor = new Color(245, 230, 255); // Lavender
                        bottomColor = new Color(200, 180, 220); // Purpleish
                    } else if (name.contains("Archer") || name.contains("Squire") || name.contains("Mercenary")) {
                        topColor = new Color(255, 248, 220); // Cornsilk
                        bottomColor = new Color(210, 190, 150); // Brownish
                    } else if (name.contains("Healer") || name.contains("Beast") || name.contains("Golem")) {
                        topColor = new Color(230, 255, 230); // HoneyDew
                        bottomColor = new Color(180, 210, 180); // Greenish
                    } else if (name.contains("Assassin")) {
                        topColor = new Color(220, 220, 220); // Light Grey
                        bottomColor = new Color(100, 100, 100); // Dark Grey
                    }
            
                    GradientPaint gp = new GradientPaint(0, 0, topColor, w, h, bottomColor);
                    g2.setPaint(gp);
                    g2.fillRoundRect(2, 2, w-4, h-4, arc, arc);            
            // Border
            g2.setColor(new Color(50, 50, 50));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(2, 2, w-4, h-4, arc, arc);
    
            // 2. Cost Bubble
            g2.setColor(new Color(30, 144, 255));
            g2.fillOval(8, 8, 24, 24);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            drawCenteredString(g2, String.valueOf(card.getCost()), new Rectangle(8, 8, 24, 24));
    
            // 3. Card Name
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            FontMetrics fm = g2.getFontMetrics();
            int nameWidth = fm.stringWidth(card.getName());
            g2.drawString(card.getName(), (w - nameWidth) / 2, 25);
    
            // 4. Art Panel
            int artX = 10;
            int artY = 40;
            int artW = w - 20;
            int artH = 70;
            
            if (cardImage != null) {
                // Simplified drawing to avoid clipping issues
                g2.drawImage(cardImage, artX, artY, artW, artH, null);
                g2.setColor(new Color(50, 50, 50));
                g2.drawRect(artX, artY, artW, artH); // Simple rect border
            } else {
                // Fallback to Procedural Art
                g2.setColor(new Color(245, 245, 245));
                g2.fillRoundRect(artX, artY, artW, artH, 10, 10);
                g2.setColor(new Color(200, 200, 200));
                g2.drawRoundRect(artX, artY, artW, artH, 10, 10);
                drawCardIcon(g2, card.getName(), new Rectangle(artX, artY, artW, artH));
            }
    
            // 5. Stats
            int statSize = 30;
            int statY = h - 40;
            
            // Attack
            g2.setColor(new Color(220, 20, 60));
            g2.fillOval(15, statY, statSize, statSize);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            drawCenteredString(g2, String.valueOf(card.getAttack()), new Rectangle(15, statY, statSize, statSize));
            
            // Defense
            g2.setColor(new Color(34, 139, 34));
            g2.fillOval(w - 15 - statSize, statY, statSize, statSize);
            g2.setColor(Color.WHITE);
            drawCenteredString(g2, String.valueOf(card.getDefense()), new Rectangle(w - 15 - statSize, statY, statSize, statSize));
            
            // Labels
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("Arial", Font.PLAIN, 9));
            g2.drawString("ATK", 18, statY - 2);
            g2.drawString("DEF", w - 15 - statSize + 3, statY - 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void drawCardBack(Graphics2D g2, int w, int h, int arc) {
        // Patterned back
        GradientPaint gp = new GradientPaint(0, 0, new Color(44, 62, 80), w, h, new Color(20, 30, 40));
        g2.setPaint(gp);
        g2.fillRoundRect(2, 2, w-4, h-4, arc, arc);
        
        g2.setColor(new Color(236, 240, 241));
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(8, 8, w-16, h-16, arc, arc);
        
        g2.setFont(new Font("Segoe UI", Font.BOLD, 40));
        drawCenteredString(g2, "⚔", new Rectangle(0, 0, w, h));
    }

    private void drawCardIcon(Graphics2D g2, String name, Rectangle area) {
        String symbol = "❓"; // Default
        Color color = Color.BLACK;

        if (name.contains("Dragon") || name.contains("Fire")) { symbol = "🐉"; }
        else if (name.contains("Knight")) { symbol = "🛡️"; }
        else if (name.contains("Soldier")) { symbol = "⚔️"; }
        else if (name.contains("Wizard") || name.contains("Magic")) { symbol = "🔮"; }
        else if (name.contains("Archer")) { symbol = "🏹"; }
        else if (name.contains("Healer")) { symbol = "💖"; }
        else if (name.contains("King")) { symbol = "👑"; }
        else if (name.contains("Queen")) { symbol = "👸"; }
        else if (name.contains("Assassin")) { symbol = "🗡️"; }
        else if (name.contains("Golem")) { symbol = "🗿"; }
        else if (name.contains("Berserker")) { symbol = "🪓"; }
        else if (name.contains("Guardian")) { symbol = "🏰"; }
        else if (name.contains("Mercenary")) { symbol = "💰"; }
        else if (name.contains("Beast")) { symbol = "🐺"; }
        else if (name.contains("Spirit")) { symbol = "👻"; }

        // Draw Shadow
        g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        
        // Check if font can display, else fallback to standard sans
        if (!g2.getFont().canDisplay(symbol.codePointAt(0))) {
             g2.setFont(new Font("SansSerif", Font.PLAIN, 50));
        }

        drawCenteredString(g2, symbol, area);
    }

    private void drawCenteredString(Graphics2D g2, String text, Rectangle rect) {
        FontMetrics fm = g2.getFontMetrics();
        int x = rect.x + (rect.width - fm.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(text, x, y);
    }
}