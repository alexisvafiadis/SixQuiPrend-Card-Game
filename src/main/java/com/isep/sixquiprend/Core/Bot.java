package com.isep.sixquiprend.Core;

import java.util.Random;

public class Bot extends Player{
    private int difficulty;

    public Bot(Game game, String name, int difficulty) {
        super(game, name);
        this.difficulty = difficulty;
    }

    public Integer decideCardIndex() {
        if (difficulty == 0) {
            Random random = new Random();
            System.out.println("hand size : " + hand.size());
            int randomCardIndex = random.nextInt(hand.size());
            return randomCardIndex;
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

    public Row chooseRow() {
        //Choose the row with the least beef heads
        int minBeefHeadCount = 100;
        Row chosenRow = null;
        for (Row row : game.getRows()) {
            if (row == null) continue;
            int rowBeefHeadCount = row.getBeefHeadCount();
            if (rowBeefHeadCount < minBeefHeadCount) {
                minBeefHeadCount = rowBeefHeadCount;
                chosenRow = row;
            }
        }
        return chosenRow;
    }
}
