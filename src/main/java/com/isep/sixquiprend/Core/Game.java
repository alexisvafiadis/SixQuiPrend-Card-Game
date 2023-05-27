package com.isep.sixquiprend.Core;

import com.isep.sixquiprend.GUI.GameApplication;
import com.isep.sixquiprend.GUI.GameController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private final int BOTS_DIFFICULTY = 0;
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
    private GameController gameController;

    public Game(GameApplication application) {
        this.application = application;
        players = new ArrayList<>();
        players.add(new Player(this,application.getPlayerUserName()));
        players.add(new Bot(this,"Bot 1",BOTS_DIFFICULTY));
        players.add(new Bot(this,"Bot 2",BOTS_DIFFICULTY));
        players.add(new Bot(this,"BotTouron",BOTS_DIFFICULTY));
    }

    public void start() {
        gameState = GameState.STARTING;
        startNewRound();
    }

    public void startNewRound() {
        distributeCards();
        gameState = GameState.PLAYING;
        startNewTurn();
    }

    public void startNewTurn() {
        System.out.println("Starting new turn");
        //TODO : askForCardChoices();
        gameState = GameState.WAITING_FOR_CARD_PICK;
    }

    public void pickPlayerCard(int cardValue) {
        gameState = GameState.PLAYING;
        System.out.println("Picking card " + cardValue);
        getMainPlayer().pickCardByIndex(getMainPlayer().getCardIndexByValue(cardValue));
        for (int i = 1; i < players.size(); i++) {
            Player player = players.get(i);
            if (player instanceof Bot) {
                player.pickCardByIndex(((Bot) player).decideCardIndex());
            }
        }
        continueTurn();
    }

    public void continueTurn() {
        //orderPlayers();
        currentPlayerIndex = 0;
        executePlayerMove();
    }

    public void executePlayerMove() {
        Player player = players.get(currentPlayerIndex);
        Integer rowIndex = findRow(player.getCardChoice());
        //If the card cannot be placed in any row, the player picks up a row, else the card is placed in the right
        //row and we execute this same function for the next player
        if (rowIndex == null) {
            System.out.println("No row found for card " + player.getCardChoice().getValue());
            if (player instanceof Bot) {
                //If the player is a bot, they choose the row instantly and we go to the next player
                player.pickUpRow(((Bot) player).chooseRow());
                decideActionAfterMove();
            }
            else {
                //If the player is the main player, we wait for them to choose a row
                //TODO : ask for the player to choose a row
                gameState = GameState.WAITING_FOR_ROW_PICK;
            }
        }
        else {
            System.out.println("Row found for card " + player.getCardChoice().getValue() + " : " + rowIndex);
            Row rowForCard = rows.get(rowIndex);
            addCardToRow(player.getCardChoice(),rowIndex);
            gameController.playCardMovementAnimation(player, rowIndex, rowForCard.getLastCardIndex(), (e) -> {
                decideActionAfterMove();
            });
        }
    }

    public void executeMainPlayerRowPickup(Row row) {
        getMainPlayer().pickUpRow(row);
        decideActionAfterMove();
        gameState = GameState.PLAYING;
    }

    public void decideActionAfterMove() {
        System.out.println("Deciding action after move");
        if (currentPlayerIndex < players.size() - 1) {
            currentPlayerIndex++;
            executePlayerMove();
        } else {
            finishTurn();
        }
    }

    public void askForNextTurnPlay() {

    }

    public void finishTurn() {
        if (!isRoundEnded()) {
            //TODO : display results
            //TODO : wait for next turn

            startNewTurn();
        }
        else {
            finishRound();
        }
    }

    public void finishRound() {
        if (!isGameEnded()) {
            //TODO : display results
            //TODO : wait for next round
            startNewRound();
        }
    }

    public void end() {
        application.displayEndGamePopup();
    }

    public Integer findRow(Card card) {
        boolean hasFoundRow = false;
        int cardValue = card.getValue();
        int minDiff = 100;
        int minRowIndex = 0;

        for (int i = 0 ; i < rows.size(); i++) {
            Row row = rows.get(i);
            if (row.canAddCard(card)) {
                hasFoundRow = true;
                int diff = Math.abs(row.getLastCardValue() - cardValue);
                if (diff < minDiff) {
                    minDiff = diff;
                    minRowIndex = i;
                }
            }
        }
        // If no row is available, return null
        if (!hasFoundRow) return null;
        // Else, return the index of the row with the smallest difference between its last card and the card to add
        return minRowIndex;

    }

    public void distributeCards() {

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
            for (int j = PLAYER_CARDS_PER_ROUND * i ; j < PLAYER_CARDS_PER_ROUND * i + PLAYER_CARDS_PER_ROUND; j++) {
                int cardValue = allCardValues.get(j);
                playerHand.add(new Card(cardValue));
            }
            players.get(i).setAndOrderHand(playerHand);
        }
        setRows(allCardValues);
        gameController.updateMainPlayerHand(getMainPlayer().getHand());
    }

    public void setRows(List<Integer> allCardValues) {
        rows = new ArrayList<>();
        List<Card> rowCards = new ArrayList<>();
        for (int j = 0; j < 4; j ++) {
            rows.add(new Row());
            int cardValue = allCardValues.get(80 + j);
            rowCards.add(new Card(cardValue));
        }
        rowCards = orderCards(rowCards);
        for (int i = 0 ; i < rows.size() ; i++) {
            addCardToRow(rowCards.get(i),i);
        }
    }

    public void addCardToRow(Card card, int rowIndex) {
        Row row = rows.get(rowIndex);
        row.addCard(card);
        gameController.placeCardInRow(card.getValue(),rowIndex, row.getLastCardIndex());
    }

    public List<Card> orderCards(List<Card> cards) {
            // order the cards in ascending order
            // useful for the bots to pick the best card, and to display the hand and the rows in the right order for the main player
            for (int i = 0; i < cards.size() - 1; i++) {
                for (int j = i + 1; j < cards.size(); j++) {
                    Card card1 = cards.get(i);
                    Card card2 = cards.get(j);

                    // If the value of the property for the first object is greater than the value of the property
                    // for the second object, swap them in the list
                    if (card1.getValue() > card2.getValue()) {
                        cards.set(i, card2);
                        cards.set(j, card1);
                    }
                }
            }
            return cards;
    }
    public void orderPlayers() {
        for (int i = 0; i < players.size() - 1; i++) {
            for (int j = i + 1; j < players.size(); j++) {
                Player player1 = players.get(i);
                Player player2 = players.get(j);

                // If the value of the property for the first object is greater than the value of the property
                // for the second object, swap them in the list
                if (player1.getCardChoice().getValue() > player2.getCardChoice().getValue()) {
                    players.set(i, player2);
                    players.set(j, player1);
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
        return (getMainPlayer().getHand().size() == 0);
    }

    public Player getMainPlayer() {
        return players.get(0);
    }

    public Player getPlayer(int index) {
        return players.get(index);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public GameApplication getApplication() {
        return application;
    }

    public List<Integer> getPlayerOrder() {
        return playerOrder;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public List<Row> getRows() {
        return rows;
    }

    public Row getRow(int index) {
        return rows.get(index);
    }

    public GameState getGameState() {
        return gameState;
    }
}
