package com.isep.sixquiprend.GUI;

import com.isep.sixquiprend.Core.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ProfileCreationController {
    @FXML
    private TextField nameTextField;
    private GameApplication application;

    public ProfileCreationController(GameApplication application) {
        this.application = application;
    }

    @FXML
    public void startGame(ActionEvent e) {
        if (nameTextField.getText().isEmpty()) {
            return;
        }
        application.setPlayerUserName(nameTextField.getText());
        application.startGame();
    }
}
