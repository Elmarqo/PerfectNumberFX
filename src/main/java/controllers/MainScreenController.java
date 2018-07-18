package controllers;


import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import javafx.scene.input.MouseEvent;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainScreenController {


    private StackPaneController stackPaneController;

    public void setStackPaneController(StackPaneController stackPaneController) {
        this.stackPaneController = stackPaneController;
    }

    @FXML
    private Label numberID;

    @FXML
    public HBox startButton;

    @FXML
    public Label labelInfoPerfect;

    @FXML
    public void start(MouseEvent event) {

        StringProperty value = new SimpleStringProperty();
        StringProperty perfectValue = new SimpleStringProperty();
        numberID.textProperty().bind(value);
        labelInfoPerfect.textProperty().bind(perfectValue);
        int number;
        String line;
        List<Integer> startend = new ArrayList();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("startend.txt")))) {

            while ((line = bufferedReader.readLine()) != null) {
                number = Integer.parseInt(line);
                startend.add(number);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        int start = startend.get(0);
        int limit = startend.get(1);

        startButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Task<Integer> task = new Task<Integer>() {
                    @Override
                    protected Integer call() throws Exception {
                        int i;
                        for (i = start; i < limit; i++) {
                            final int val = i;
                            final int perfVal = i;
                            if (i % 2 == 0) {
                                int sum = 0;
                                for (int j = 1; j <= i / 2; j++) {
                                    if (i % j == 0)
                                        sum += j;
                                }
                                if (sum % i == 0)
                                    System.out.println("Perfect number: " + i);
                            }
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                            }
                            // Update the GUI on the JavaFX Application Thread
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    value.setValue(df(val));
                                    //perfectValue.setValue("Perfect number:" + df(perfVal));
                                }
                            });
                        }
                        return i;
                    }
                };
                Thread thread = new Thread(task);
                thread.setDaemon(true);
                thread.start();
            }
        });
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
