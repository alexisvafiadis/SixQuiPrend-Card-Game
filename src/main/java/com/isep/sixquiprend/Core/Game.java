package com.isep.sixquiprend.Core;

import com.isep.sixquiprend.GUI.GameApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private final int MAX_BEEFHEAD_COUNT = 66;
    private final int PLAYER_CARDS_PER_ROUND = 10;
    private final int MAX_PLAYER_COUNT = 10;

    private final int minCardValue = 1;
    private final int maxCardValue = 104;
    private GameState gameState;
    private List<Player> players;
    private List<Integer> playerOrder;
    private int currentPlayerIndex;
    private List<Row> rows;
    private GameApplication application;

    public Game(GameApplication application) {
        this.application = application;
        start();
    }

    public void start() {
        players = new ArrayList<>();
        rows = new ArrayList<>();
        gameState = GameState.STARTING;
        startNewRound();
    }

    public void startNewRound() {
        distributeHands();
        gameState = GameState.PLAYING;
        startNewTurn();
    }
    public void finishRound() {
        if (!isGameEnded()) {
            //TODO : display results
            //TODO : wait for next round
            startNewRound();
        }
    }

    public void startNewTurn() {
        //TODO : askForCardChoices();
    }

    public void continueTurn() {
        orderPlayers();
        /*
            if (findRow(player.getCardChoice()) == null) {
                currentPlayer.pickUpRow(currentPlayer.chooseRow());
            }
         */
    }

    public void askForNextTurnPlay() {

    }

    public void finishTurn() {
        if (!isRoundEnded()) {
            //TODO : display results
            //TODO : wait for next turn

            startNewTurn();
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

    public void distributeHands() {

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

    public void end() {
        application.displayEndGamePopup();
    }

    public Player getPlayer(int index) {
        return players.get(index);
    }

    public GameApplication getApplication() {
        return application;
    }

    public List<Integer> getPlayerOrder() {
        return playerOrder;
    }
}
