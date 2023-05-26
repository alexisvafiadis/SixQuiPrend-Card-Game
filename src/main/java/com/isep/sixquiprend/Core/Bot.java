package com.isep.sixquiprend.Core;

import java.util.Random;

public class Bot extends Player{
    private int difficulty;

    public Bot(Game game, String name, int difficulty) {
        super(game, name);
        this.difficulty = difficulty;
    }

    public Card pickCard() {
        if (difficulty == 0) {
            Random random = new Random();
            int randomCardIndex = random.nextInt(hand.size());
            Card card = hand.get(randomCardIndex);
            hand.remove(randomCardIndex);
            return card;
        }
        else {
            int minCardValue = 104;
            for (Card card : hand) {
                if (card.getValue() < minCardValue) {
                    minCardValue = card.getValue();
                }
            }
            int minRowLastCardValue = 104;
            for (Row row : game.getRows()) {
                if (row.getLastCardValue() < minRowLastCardValue) {
                    minRowLastCardValue = row.getLastCardValue();
                }
            }
            if (minCardValue > minRowLastCardValue) {

            }
            return null;
        }
    }
}
