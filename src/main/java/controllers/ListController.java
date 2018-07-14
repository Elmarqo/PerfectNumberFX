package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;

public class ListController {

    private StackPaneController stackPaneController;

    public void setStackPaneController(StackPaneController stackPaneController) {
        this.stackPaneController = stackPaneController;
    }

    @FXML
    public void initialize() {

    }

    @FXML
    public void home() {
        stackPaneController.setMainScreen();
    }

    @FXML
    public void exit() {
        Platform.exit();
    }
}
