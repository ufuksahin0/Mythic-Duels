package com.cardgame.model;

import com.cardgame.util.GameConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerPlayer extends Player {
    private Random random;

    public ComputerPlayer(String name, Deck.DeckType deckType) {
        super(name, deckType);
        this.random = new Random();
    }

    @Override
    public Card makeMove() {
        return determineMove(null);
    }
    
    public Card determineMove(Card opponentCard) {
        List<Card> playableCards = new ArrayList<>();
        
        // Find all cards we can afford
        for (Card c : hand) {
            if (c.getCost() <= mana) {
                playableCards.add(c);
            }
        }

        if (playableCards.isEmpty()) {
            return null; 
        }
        
        // Difficulty Check
        if (GameConfig.getDifficulty() == GameConfig.Difficulty.EASY) {
            // Easy: Just pick a random card
            return playableCards.get(random.nextInt(playableCards.size()));
        }

        // Normal/Hard Logic
        
        // 1. If opponent played a card, try to counter it
        if (opponentCard != null) {
            // Try to find a card with Attack > Opponent Defense
            for (Card c : playableCards) {
                if (c.getAttack() > opponentCard.getDefense()) {
                    return c; 
                }
            }
            // If can't kill, try to defend (Defense > Opponent Attack)
            for (Card c : playableCards) {
                if (c.getDefense() > opponentCard.getAttack()) {
                    return c; 
                }
            }
        }

        // 2. Otherwise (or if we can't counter), play the strongest card
        Card bestCard = playableCards.get(0);
        for (Card c : playableCards) {
            if (c.getAttack() > bestCard.getAttack()) {
                bestCard = c;
            }
        }
        
        // If health is low, maybe prioritize defense?
        if (health < 10) {
             for (Card c : playableCards) {
                if (c.getDefense() > bestCard.getDefense()) {
                    bestCard = c;
                }
            }
        }

        return bestCard;
    }
}
