package com.isep.sixquiprend.GUI;

import com.isep.sixquiprend.Core.Card;
import com.isep.sixquiprend.Core.Game;
import com.isep.sixquiprend.Core.GameState;
import com.isep.sixquiprend.Core.Player;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.List;

public class GameController {
    private final double CARD_ANIMATION_DURATION = 2;
    private final String MAIN_PLAYER_CARD_ID_PREFIX = "card";
    private final String ROW_CARD_ID_PREFIX = "rowCard";
    @FXML
    private AnchorPane gameAnchorPane;
    @FXML
    private HBox playersBox;
    @FXML
    private VBox rowsBox;
    @FXML
    private HBox mainPlayerCardsBox;
    @FXML
    private Text roundText;
    private ImageView emptyCardImageView;

    private GameApplication application;
    private Game game;

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
            ((Text) ((HBox) playerBox.getChildren().get(0)).getChildren().get(1)).setText(player.getName());
            getBeefheadText(playerBox).setText(String.valueOf(player.getBeefHeadCount()));
            setCardImageToBackside(getBoxImageView(playerBox));
            System.out.println("Set player box id to " + playerBox.getId());
        }
    }

    public void updatePlayerBeefHead(Player player) {
        VBox playerBox = getPlayerBox(player.getName());
        getBeefheadText(playerBox).setText(String.valueOf(player.getBeefHeadCount()));
    }

    public void updatePlayerCard(Player player, Card card) {
        VBox playerBox = getPlayerBox(player.getName());
        ImageView boxImageView = getBoxImageView(playerBox);
        if (card == null) {
            setCardImageToBackside(boxImageView);
            return;
        }
        boxImageView.setImage(getCardImage(card.getValue()));
        boxImageView.setId(ROW_CARD_ID_PREFIX + card.getValue());
    }
    public void resetPlayerCard(Player player) {updatePlayerCard(player,null);}
    public void updateMainPlayerHand(List<Card> hand) {
        for (int i = 0; i < hand.size(); i++) {
            ImageView cardImageView = ((ImageView) mainPlayerCardsBox.getChildren().get(i));
            if (emptyCardImageView == null) emptyCardImageView = cloneImageView(cardImageView);
            cardImageView.setImage(application.getImage("cards/" + hand.get(i).getValue() + ".png"));
            cardImageView.setId(MAIN_PLAYER_CARD_ID_PREFIX + hand.get(i).getValue());
        }
    }

    public void updateRoundNumber(int roundNumber) {
        roundText.setText("Round " + String.valueOf(roundNumber));
    }
    public void placeCardInRow(int cardValue, int rowId, int cardIndex) {
        ImageView cardImageView = getRowCardImageView(rowId, cardIndex);
        cardImageView.setImage(getCardImage(cardValue));
    }

    public ImageView getRowCardImageView(int rowId, int cardIndex) {
        HBox rowBox = ((HBox) rowsBox.getChildren().get(rowId));
        return (((ImageView) rowBox.getChildren().get(cardIndex)));
    }

    public void resetRow(int rowId) {
        HBox rowBox = ((HBox) rowsBox.getChildren().get(rowId));
        for (int i = 0; i < rowBox.getChildren().size(); i++) {
            ImageView cardImageView = ((ImageView) rowBox.getChildren().get(i));
            cardImageView.setImage(null);
        }
    }

    public void hideOpponentsCards() {
        for (int i = 0; i < game.getPlayers().size(); i++) {
            VBox playerBox = (VBox) playersBox.getChildren().get(i);
            setCardImageToBackside(getBoxImageView(playerBox));
        }
    }

    public void hideRow(int controllerRowIndex) {
        rowsBox.getChildren().get(controllerRowIndex).setVisible(false);
    }

    @FXML
    public void onCardChooseEventHandler(MouseEvent e) {
        if (!(e.getEventType().equals(MouseEvent.MOUSE_CLICKED))) {
            return;
        }
        if (!game.getGameState().equals(GameState.WAITING_FOR_CARD_PICK)) {
            return;
        }
        ImageView cardImageView = ((ImageView) e.getSource());
        if (cardImageView.getImage() == null) {
            return;
        }
        if (!cardImageView.getParent().isVisible()) return;
        int cardValue = Integer.parseInt(cardImageView.getId().substring(MAIN_PLAYER_CARD_ID_PREFIX.length()));
        game.pickPlayerCard(cardValue);
        mainPlayerCardsBox.getChildren().remove(cardImageView);
    }

    @FXML
    public void onRowChooseEventHandler(MouseEvent e) {
        if (!(e.getEventType().equals(MouseEvent.MOUSE_CLICKED))) {
            return;
        }
        if (!game.getGameState().equals(GameState.WAITING_FOR_ROW_PICK)) {
            return;
        }
        ImageView rowImageView = ((ImageView) e.getSource());
        if (rowImageView.getImage() == null) {
            return;
        }
        HBox rowBox = ((HBox) rowImageView.getParent());
        System.out.println("Clicked on row number " + rowsBox.getChildren().indexOf(rowBox));
        game.executeMainPlayerRowPickup(rowsBox.getChildren().indexOf(rowBox));
    }
    public VBox getPlayerBox(String userName) {
        System.out.println("Looking for player box with ID : " + userName + "Box");
        for (Node node : playersBox.getChildren()) {
            if (node instanceof VBox && node.getId().equals(userName + "Box")) {
                return ((VBox) node);
            }
            //System.out.println("Node of ID " + node.getId() + " of class " + node.getClass());
        }
        return null;
        //For some reason the following line didn't work:
        //return ((VBox) playersBox.lookup("#" + userName + "Box"));
    }

    public ImageView getBoxImageView(VBox vbox) {
        return ((ImageView) vbox.getChildren().get(2));
    }

    public Text getBeefheadText(VBox vbox) {
        return ((Text) ((HBox) vbox.getChildren().get(1)).getChildren().get(0));
    }

    public void playCardMovementAnimation(Player player, int rowId, int cardIndex, EventHandler<ActionEvent> onFinished) {
        ImageView sourceImageView = getBoxImageView(getPlayerBox(player.getName()));
        ImageView destinationImageView = getRowCardImageView(rowId, cardIndex);
        Image sourceImage = getCardImage(Integer.parseInt(sourceImageView.getId().substring(ROW_CARD_ID_PREFIX.length())));

        ImageView clonedImageView = cloneImageView(sourceImageView);
        gameAnchorPane.getChildren().add(clonedImageView);

        TranslateTransition translate = new TranslateTransition(Duration.seconds(CARD_ANIMATION_DURATION), clonedImageView);

        translate.setFromX(getRealImageX(sourceImageView));
        translate.setFromY(getRealImageY(sourceImageView));
        translate.setToX(getRealImageX(destinationImageView));
        translate.setToY(getRealImageY(destinationImageView));
        translate.setOnFinished((e) -> {
            gameAnchorPane.getChildren().remove(clonedImageView);
            destinationImageView.setImage(sourceImage);
            onFinished.handle(e);
        });
        setCardImageToBackside(sourceImageView);
        translate.play();
    }

    public ImageView cloneImageView(ImageView imageView) {
        ImageView clonedImageView = new ImageView(imageView.getImage());
        clonedImageView.setFitHeight(imageView.getFitHeight());
        clonedImageView.setFitWidth(imageView.getFitWidth());
        clonedImageView.toFront();
        return clonedImageView;
    }

    public void prepareForNewRound() {
        resetRows();
        resetHand();
    }

    public void resetRows() {
        for (Node node : rowsBox.getChildren()) {
            HBox rowBox = ((HBox) node);
            rowBox.setVisible(true);
            for (Node cardNode : rowBox.getChildren()) {
                ImageView cardImageView = ((ImageView) cardNode);
                cardImageView.setImage(null);
            }
        }
    }

    public void resetHand() {
        for (int i = 0 ; i < game.getMaxHandSize() ; i++) {
            mainPlayerCardsBox.getChildren().add(cloneImageView(emptyCardImageView));
        }
    }

    public void setCardImageToBackside(ImageView imageView) {
        imageView.setImage(application.getImage("cards/backside.png"));
    }
    public double getRealImageX(ImageView imageView, double factor) {
        Bounds bounds = imageView.localToScene(imageView.getBoundsInLocal());
        if (factor == 0) return bounds.getMinX();
        return bounds.getMinX() + bounds.getWidth() / factor;
    }

    public double getRealImageY(ImageView imageView, double factor) {
        Bounds bounds = imageView.localToScene(imageView.getBoundsInLocal());
        if (factor == 0) return bounds.getMinY();
        return bounds.getMinY() + bounds.getHeight() / factor;
    }

    public double getRealImageX(ImageView imageView) {
        return getRealImageX(imageView, 0);
    }
    public double getRealImageY(ImageView imageView) {
        return getRealImageY(imageView, 0);
    }

    public Image getCardImage(int cardValue) {
        return application.getImage("cards/" + cardValue + ".png");
    }

}
