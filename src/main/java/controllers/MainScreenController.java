package controllers;


import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.When;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.text.DecimalFormat;

public class MainScreenController {

    private LongProperty number = new SimpleLongProperty();
    private StackPaneController stackPaneController;

    public void setStackPaneController(StackPaneController stackPaneController) {
        this.stackPaneController = stackPaneController;
    }

    @FXML
    private Label numberID;

    @FXML
    private TextField textFieldID;

    @FXML
    public void start() {
        number.addListener((Observable, oldValue, newValue)->{
            if (newValue.longValue() > oldValue.longValue())
                numberID.setText(newValue.toString());
        }) ;
        for (int i = 0; i < 10000; i++) {
            number.set(i);
        }
    }

    @FXML
    public void stop() {

    }

    @FXML
    public void showList() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/List.fxml"));
        AnchorPane anchorPane = null;

        try {
            anchorPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ListController listController = loader.getController();
        listController.setStackPaneController(stackPaneController);
        stackPaneController.setScreen(anchorPane);
    }

    @FXML
    public void exit() {
        Platform.exit();
    }

    @FXML
    public void initialize() {
    }

    public String df(long number) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
        return decimalFormat.format(number);
    }
}
