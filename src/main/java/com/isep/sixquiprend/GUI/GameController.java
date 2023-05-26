package com.isep.sixquiprend.GUI;

import com.isep.sixquiprend.Core.Card;
import com.isep.sixquiprend.Core.Game;
import com.isep.sixquiprend.Core.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class GameController {
    @FXML
    private HBox playersBox;
    @FXML
    private VBox rowsBox;
    @FXML
    private HBox mainPlayerCardsBox;

    private GameApplication application;
    private Game game;
    private final String MAIN_PLAYER_CARD_ID_PREFIX = "card";

    public GameController(GameApplication application, Game game) {
        this.application = application;
        this.game = game;
    }
    @FXML
    public void initialize() {
        for (int i = 0; i < game.getPlayers().size(); i++) {
            VBox playerBox = (VBox) playersBox.getChildren().get(i);
            Player player = game.getPlayers().get(i);
            playerBox.setId(player.getName() + "Box");
            ((Text) playerBox.getChildren().get(0)).setText(player.getName());
            getBeefheadText(playerBox).setText(String.valueOf(player.getBeefHeadCount()));
            getBoxImageView(playerBox).setImage(application.getImage("cards/backside.png"));
        }
    }

    public void updatePlayerBeefHead(Player player) {
        VBox playerBox = getPlayerBox(player.getName());
        getBeefheadText(playerBox).setText(String.valueOf(player.getBeefHeadCount()));
    }

    public void updatePlayerCard(Player player, Card card) {
        VBox playerBox = (VBox) getPlayerBox(player.getName());
        Image cardImage = application.getImage("cards/" + card.getValue() + ".png");
        getBoxImageView(playerBox).setImage(cardImage);
    }
    public void updateMainPlayerHand(List<Card> hand) {
        for (int i = 0; i < hand.size(); i++) {
            ImageView cardImageView = ((ImageView) mainPlayerCardsBox.getChildren().get(i));
            cardImageView.setImage(application.getImage("cards/" + hand.get(i).getValue() + ".png"));
            cardImageView.setId(MAIN_PLAYER_CARD_ID_PREFIX + hand.get(i).getValue());
        }
    }
    public void placeCardInRow(int cardValue, int rowId) {
        ImageView cardImageView = ((ImageView) rowsBox.getChildren().get(rowId));
        cardImageView.setImage(application.getImage("cards/" + cardValue + ".png"));
    }

    public void hideOpponentsCards() {
        for (int i = 0; i < game.getPlayers().size(); i++) {
            VBox playerBox = (VBox) playersBox.getChildren().get(i);
            getBoxImageView(playerBox).setImage(application.getImage("cards/backside.png"));
        }
    }

    @FXML
    public void onCardChooseEventHandler(ActionEvent e) {
        ImageView cardImageView = ((ImageView) e.getSource());
        int cardValue = Integer.parseInt(cardImageView.getId().substring(MAIN_PLAYER_CARD_ID_PREFIX.length()));

    }
    public VBox getPlayerBox(String userName) {
        return ((VBox) playersBox.lookup("#" + userName + "Box"));
    }

    public ImageView getBoxImageView(VBox vbox) {
        return ((ImageView) vbox.getChildren().get(2));
    }

    public Text getBeefheadText(VBox vbox) {
        return ((Text) vbox.getChildren().get(1));
    }
}
