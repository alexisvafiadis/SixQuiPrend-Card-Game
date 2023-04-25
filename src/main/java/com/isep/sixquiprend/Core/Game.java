package com.isep.sixquiprend.Core;

import com.isep.sixquiprend.Core.Card;
import com.isep.sixquiprend.Core.Player;
import com.isep.sixquiprend.Core.Row;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private final int MAX_BEEFHEAD_COUNT = 66;
    private final int PLAYER_CARDS_PER_ROUND = 10;
    private final int nbJoueursMax = 10;

    private final int minCardValue = 1;
    private final int maxCardValue = 104;
    private int ID;
    private List<Player> players;
    private Player currentPlayer;
    private List<Row> rows;
    private List<Integer> allCardValues;

    public void play() {
        while(!isGameEnded()) {
            giveDecks();
            while (!isRoundEnded()) {
                //TODO : wait for card choices
                orderPlayers();
                for (Player player : players) {
                    if (findRow(player.getCardChoice()) == null) {
                        currentPlayer.pickUpRow(currentPlayer.chooseRow());
                    }
                }
            }
        }
    }

    public boolean isGameEnded() {
        for (Player player : players) {
            if (player.getBeefHeadCount() >= MAX_BEEFHEAD_COUNT) {
                return true;
            }
        }
        return false;
    }

    public boolean isRoundEnded() {
        return !(players.get(0).getHand().size() == 0);
    }

    public Row findRow(Card card) {
        for (Row row : rows) {
            // If the card can be added to the row
            if (row.canAddCard(card)) {
                return row; // Return the row
            }
        }
        // If no row is available, return null
        return null;
    }

    public void endRound() {}

    public void displayResults() {}

    public void giveDecks() {

        // Create a list with all possible card values
        List<Integer> allCardValues = new ArrayList<>();
        for (int i = minCardValue; i <= maxCardValue; i++) {
            allCardValues.add(i);
        }

        // Shuffle the list of possible card values
        Collections.shuffle(allCardValues);

        for (int i = 0 ; i < players.size(); i++) {
            // Pick the player cards for their deck
            List<Card> playerHand = new ArrayList<>();
            for (int j = PLAYER_CARDS_PER_ROUND * i ; j < PLAYER_CARDS_PER_ROUND; j++) {
                int cardValue = allCardValues.get(j);
                playerHand.add(new Card(cardValue));
            }
            players.get(i).setHand(playerHand);
        }

        //Clear card values to make space
        allCardValues = null;
    }

    public void orderPlayers() {
        for (int i = 0; i < players.size() - 1; i++) {
            for (int j = i + 1; j < players.size(); j++) {
                Player player1 = players.get(i);
                Player player2 = players.get(j);

                // If the value of the property for the first object is greater than the value of the property
                // for the second object, swap them in the list
                if (player1.getCardChoiceNumber() > player2.getCardChoiceNumber()) {
                    players.set(i, player2);
                    players.set(j, player1);
                }
            }
        }
    }
}
