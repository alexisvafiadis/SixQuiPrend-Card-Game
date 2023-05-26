package com.isep.sixquiprend.Core;

import java.util.List;

public class Row {
    private final int MAX_NUMBER_OF_CARDS = 5;
    private List<Card> cards;

    public boolean canAddCard(Card card) {
        // If the row is already full, the card cannot be added
        if (isFull()) {
            return false;
        }

        // If the row is empty, the card can always be added
        if (cards.size() == 0) {
            return true;
        }

        // Otherwise, check if the card can be added to the row according to the game rules
        int lastCardValue = getLastCardValue();
        int cardNumber = card.getValue();

        // If the card has the same or higher number than the last card in the row, it can be added
        return (lastCardValue <= cardNumber);

    }


    public void addCard(Card card) {
        cards.add(card);
    }

    public boolean isFull() { return (cards.size() >= MAX_NUMBER_OF_CARDS) ;}

    public List<Card> getCards() {
        return cards;
    }

    public int getLastCardValue() {
        return cards.get(cards.size() - 1).getValue();
    }
}
