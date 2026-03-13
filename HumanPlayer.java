package com.cardgame.model;

public class HumanPlayer extends Player {
    public HumanPlayer(String name, Deck.DeckType deckType) {
        super(name, deckType);
    }

    @Override
    public Card makeMove() {
        // Human moves are driven by UI events, this might return null or be unused 
        // depending on controller implementation.
        return null; 
    }
}
