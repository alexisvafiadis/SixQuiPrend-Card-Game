package com.isep.sixquiprend.GUI;

import com.isep.sixquiprend.Core.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class EndGamePopupController {
    @FXML
    private Button newGameButton;

    @FXML
    private Button quitButton;
    private GameApplication application;


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
