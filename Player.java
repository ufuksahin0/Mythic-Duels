package com.cardgame.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Player implements Serializable {
    protected String name;
    protected int health;
    protected int mana;
    protected int maxMana;
    protected List<Card> hand;
    protected Deck deck;

    public Player(String name, Deck.DeckType deckType) {
        this.name = name;
        this.health = 30; // Starting health
        this.mana = 1;
        this.maxMana = 1;
        this.hand = new ArrayList<>();
        this.deck = new Deck(deckType);
        this.deck.shuffle();
        drawInitialHand();
    }

    private void drawInitialHand() {
        for (int i = 0; i < 5; i++) {
            drawCard();
        }
    }

    public void drawCard() {
        Card c = deck.draw();
        if (c != null) {
            hand.add(c);
        }
    }

    public void startTurn() {
        if (maxMana < 10) maxMana++;
        mana = maxMana;
        drawCard();
    }

    public boolean playCard(Card card) {
        if (hand.contains(card) && mana >= card.getCost()) {
            mana -= card.getCost();
            hand.remove(card);
            return true;
        }
        return false;
    }

    public void takeDamage(int dmg) {
        this.health -= dmg;
        if (this.health < 0) this.health = 0;
    }

    public abstract Card makeMove(); // Polymorphic method for AI/Human logic (Human triggers via UI)

    // Getters
    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getMana() { return mana; }
    public int getMaxMana() { return maxMana; }
    public List<Card> getHand() { return hand; }
    public int getDeckSize() { return deck.size(); }
    public boolean isAlive() { return health > 0; }
}
