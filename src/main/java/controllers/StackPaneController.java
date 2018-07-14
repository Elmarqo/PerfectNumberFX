package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class StackPaneController {

    @FXML
    private StackPane stackPaneID;

    @FXML
    public void initialize(){
        setMainScreen();
    }

    public void setMainScreen() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainScreen.fxml"));
        AnchorPane anchorPane = null;

        try {
            anchorPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainScreenController mainScreenController = loader.getController();
        mainScreenController.setStackPaneController(this);
        setScreen(anchorPane);
    }

    public void setScreen(AnchorPane anchorPane) {
        stackPaneID.getChildren().clear();
        stackPaneID.getChildren().add(anchorPane);
    }
}
