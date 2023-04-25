package com.isep.sixquiprend.Core;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int ID;
    private List<Card> hand;
    private List<Card> cards;
    private int beefHeadCount;

    private int cardChoice;

    public Player(String name) {
        this.name = name;
        cards = new ArrayList<>();
        cardChoice = 0;
        beefHeadCount = 0;
    }

    public void setHand(List<Card> hand) {
        hand = hand;
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

    public int getCardChoiceNumber() {
        return cardChoice;
    }
}
