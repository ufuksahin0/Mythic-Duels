package com.cardgame.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck implements Serializable {
    private List<Card> cards;

    public enum DeckType { BALANCED, AGGRESSIVE, DEFENSIVE }

    public Deck() {
        this(DeckType.BALANCED);
    }

    public Deck(DeckType type) {
        this.cards = new ArrayList<>();
        initializeDeck(type);
    }

    private void initializeDeck(DeckType type) {
        cards.clear();
        switch (type) {
            case AGGRESSIVE:
                addCards("Assassin", 5, 1, 3);
                addCards("Berserker", 6, 0, 4);
                addCards("Dragon", 8, 2, 5);
                addCards("Soldier", 3, 1, 2);
                break;
            case DEFENSIVE:
                addCards("Guardian", 1, 6, 3);
                addCards("Golem", 2, 8, 5);
                addCards("Healer", 1, 4, 2);
                addCards("Knight", 3, 5, 4);
                break;
            case BALANCED:
            default:
                addCards("Soldier", 3, 3, 2);
                addCards("Archer", 4, 2, 3);
                addCards("Knight", 5, 5, 5);
                addCards("Wizard", 6, 3, 5);
                break;
        }
        // Fill rest with randoms to reach ~20 cards
        while(cards.size() < 20) {
            addRandomCard();
        }
    }
    
    private void addCards(String name, int atk, int def, int cost) {
        for(int i=0; i<3; i++) cards.add(new Card(name, atk, def, cost, "Unit: " + name));
    }

    private void addRandomCard() {
        String[] names = {"Mercenary", "Squire", "Beast", "Spirit"};
        String name = names[(int)(Math.random()*names.length)];
        int cost = (int)(Math.random() * 5) + 1; 
        int attack = cost * 2 + (int)(Math.random() * 2) - 1;
        int defense = cost * 2 + (int)(Math.random() * 2) - 1;
        if(attack < 0) attack = 0;
        if(defense < 0) defense = 0;
        cards.add(new Card(name, attack, defense, cost, "Random Unit"));
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card draw() {
        if (cards.isEmpty()) return null;
        return cards.remove(0);
    }

    public int size() {
        return cards.size();
    }

    public void reset(DeckType type) {
        initializeDeck(type);
        shuffle();
    }
}
