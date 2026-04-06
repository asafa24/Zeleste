package com.iza.zeleste;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller {
    private Main mainApp;

    @FXML
    private VBox pause;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleStart() {
        mainApp.startGame();
    }

    @FXML
    private void handleQuit() {
        System.exit(0);
    }

    @FXML
    public void handleResume() {
        mainApp.togglePause();
    }

    @FXML
    public void handleQuitToMenu() {
        System.out.println("Byee !");
        System.exit(0);
    }

}