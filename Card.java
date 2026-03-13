package com.cardgame.model;

import java.io.Serializable;

public class Card implements Serializable {
    private String name;
    private int attack;
    private int defense;
    private int cost;
    private String description;
    // We will use a color or simpler representation if images are not available
    // but the requirement says "Card images", we can use paths or generated BufferedImages in View.
    
    public Card(String name, int attack, int defense, int cost, String description) {
        this.name = name;
        this.attack = attack;
        this.defense = defense;
        this.cost = cost;
        this.description = description;
    }

    public String getName() { return name; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getCost() { return cost; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return name + " [A:" + attack + " D:" + defense + " C:" + cost + "]";
    }
}
