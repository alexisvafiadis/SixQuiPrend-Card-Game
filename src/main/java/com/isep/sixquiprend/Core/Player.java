package com.isep.sixquiprend.Core;

import java.util.ArrayList;
import java.util.List;

public class Player {
    protected Game game;
    protected String name;
    protected int ID;
    protected List<Card> hand;
    protected List<Card> cards;
    protected int beefHeadCount;
    protected int cardChoice;

    public Player(Game game, String name) {
        this.name = name;
        cards = new ArrayList<>();
        cardChoice = 0;
        beefHeadCount = 0;
    }

    public void setAndOrderHand(List<Card> hand) {
        // order the player's cards in ascending order
        // useful for the bots to pick the best card, and to display the hand in the right order for the main player
        for (int i = 0; i < hand.size() - 1; i++) {
            for (int j = i + 1; j < hand.size(); j++) {
                Card card1 = hand.get(i);
                Card card2 = hand.get(j);

                // If the value of the property for the first object is greater than the value of the property
                // for the second object, swap them in the list
                if (card1.getValue() > card2.getValue()) {
                    hand.set(i, card2);
                    hand.set(j, card1);
                }
            }
        }
        this.hand = hand;
    }
    public void playCard() {

    }

    public Row chooseRow() {
        return null;
    }

    public void pickUpRow(Row row) {
        for (Card card : row.getCards()) {
            cards.add(card);
            beefHeadCount += card.getBeefHead();
        }
    }
    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    public List<Card> getHand() {
        return hand;
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getBeefHeadCount() {
        return beefHeadCount;
    }

    public void setCardChoice(int cardChoice) {
        this.cardChoice = cardChoice;
    }
    public Card getCardChoice() {
        return cards.get(cardChoice);
    }
    public boolean hasChosenCard() {
        return cardChoice != 0;
    }

    public int getCardChoiceNumber() {
        return cardChoice;
    }
}
