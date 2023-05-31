package com.isep.sixquiprend.Core;

import com.isep.sixquiprend.GUI.DialogueBox;
import com.isep.sixquiprend.GUI.GameApplication;
import com.isep.sixquiprend.GUI.GameController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private boolean DEBUG_MODE = false;
    private final int BOTS_DIFFICULTY = 0;
    private final int MAX_BEEFHEAD_COUNT = 66;
    private final int PLAYER_CARDS_PER_ROUND = 10;
    private final int ROW_COUNT = 4;
    private final int MAX_PLAYER_COUNT = 10;

    private final int minCardValue = 1;
    private final int maxCardValue = 104;
    private GameState gameState;
    private List<Player> players;
    private Player mainPlayer;
    private int currentPlayerIndex;
    private List<Row> rows;
    private GameApplication application;
    private GameController gameController;
    private DialogueBox dialogueBox;
    private int roundNumber;

    public Game(GameApplication application) {
        this.application = application;
        players = new ArrayList<>();
        players.add(new Player(this,application.getPlayerUserName()));
        players.add(new Bot(this,"Bot 1",BOTS_DIFFICULTY));
        players.add(new Bot(this,"Bot 2",BOTS_DIFFICULTY));
        players.add(new Bot(this,"BotTouron",BOTS_DIFFICULTY));
        roundNumber = 1;
    }

    public void start() {
        dialogueBox = DialogueBox.getInstance();
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
        dialogueBox.displayInfo("Please pick a card for this turn.");
        dialogueBox.setOnFinish((e) ->         gameState = GameState.WAITING_FOR_CARD_PICK);
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
        dialogueBox.displayInfo("It is " + player.getName() + "'s turn.");
        //If the card cannot be placed in any row, the player picks up a row, else the card is placed in the right
        //row and we execute this same function for the next player
        if (rowIndex == null) {
            System.out.println("No row found for card " + player.getCardChoice().getValue());
            if (player instanceof Bot) {
                giveRowToPlayer(((Bot) player).chooseRow(),player);
                decideActionAfterMove();
            }
            else {
                dialogueBox.displayInfo("That card is too low to be placed in any row. Pick a row to pick up.");
                dialogueBox.setOnFinish((e) -> gameState = GameState.WAITING_FOR_ROW_PICK);
            }
        }
        else {
            System.out.println("Row found for card " + player.getCardChoice().getValue() + " : " + rowIndex);
            addCardToRow(player.getCardChoice(),rowIndex,false);
            Row rowForCard = rows.get(rowIndex);
            dialogueBox.setOnFinish((e) -> {
                gameController.playCardMovementAnimation(player, rowIndex, rowForCard.getLastCardIndex(), (finish) -> {
                    decideActionAfterMove();
                });
            });
        }
    }

    public void executeMainPlayerRowPickup(int rowIndex) {
        giveRowToPlayer(getRow(rowIndex),getMainPlayer());
        decideActionAfterMove();
        gameState = GameState.PLAYING;
    }

/*    public int convertRowIndex(int rowIndex, boolean toController) {
        if (toController) {
            return rowIndex + (ROW_COUNT - rows.size());
        }
        else {
            return rowIndex - (ROW_COUNT - rows.size());
        }
    }*/

    public void decideActionAfterMove() {
        System.out.println("Deciding action after move");
        dialogueBox.setOnFinish((e) -> {
            if (!isThereAnyRowLeft()) {
                finishRound();
            }
            if (currentPlayerIndex < players.size() - 1) {
                currentPlayerIndex++;
                executePlayerMove();
            } else {
                finishTurn();
            }
        });
    }

    public void giveRowToPlayer(Row row, Player player) {
        player.pickUpRow(row);
        gameController.hideRow(rows.indexOf(row));
        int rowIndex = rows.indexOf(row);
        rows.set(rowIndex,null);
        dialogueBox.displayInfo(player.getName() + " picked up row " + (rowIndex + 1));
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
        System.out.println("Finishing round");
        dialogueBox.displayInfo("This round has ended!");
        incrementRoundNumber();
        gameController.prepareForNewRound();
        if (!isGameEnded()) {
            //TODO : display results
            //TODO : wait for next round
            startNewRound();
        }
        else {
            end();
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
            if (row == null) continue;
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
        dialogueBox.displayInfo("The cards for this round have been distributed.");
    }

    public void setRows(List<Integer> allCardValues) {
        rows = new ArrayList<>();
        List<Card> rowCards = new ArrayList<>();
        for (int j = 0; j < ROW_COUNT; j ++) {
            rows.add(new Row());
            int cardValue = allCardValues.get(80 + j);
            rowCards.add(new Card(cardValue));
        }
        rowCards = orderCards(rowCards);
        for (int i = 0 ; i < rows.size() ; i++) {
            addCardToRow(rowCards.get(i),i,true);
        }
    }

    public void addCardToRow(Card card, int rowIndex, boolean instantPlacement) {
        Row row = rows.get(rowIndex);
        row.addCard(card);
        if (instantPlacement) gameController.placeCardInRow(card.getValue(),rowIndex, row.getLastCardIndex());
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
        if (getMainPlayer().getHand().size() == 0) {
            return true;
        }
        return !isThereAnyRowLeft();
    }

    public boolean isThereAnyRowLeft() {
        for (Row row : rows) {
            if (row != null) {
                return true;
            }
        }
        return false;
    }

    public void incrementRoundNumber() {
        roundNumber++;
        gameController.updateRoundNumber(roundNumber);
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

    public boolean isInDebugMode() {
        return DEBUG_MODE;
    }
}
