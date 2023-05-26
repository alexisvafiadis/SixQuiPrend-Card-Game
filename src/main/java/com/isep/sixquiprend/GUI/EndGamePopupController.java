package com.isep.sixquiprend.GUI;

import com.isep.sixquiprend.Core.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class EndGamePopupController {
    @FXML
    private Button newGameButton;

    @FXML
    private Button quitButton;
    @FXML
    private Label announcementLabel;
    @FXML
    private ImageView endGameBackgroundImage;
    private GameApplication application;
    private Game game;

    public EndGamePopupController(GameApplication application, Game game) {
        this.application = application;
        this.game = game;
    }

    @FXML
    public void initialize() {
        endGameBackgroundImage.setImage(application.getImage())
    }

    @FXML
    public void onButtonClick(ActionEvent e) {
        if (e.getSource().equals(newGameButton)) {
            application.startGame();
        }
        if (e.getSource().equals(quitButton)) {
            application.startMenu();
        }
    }
}
