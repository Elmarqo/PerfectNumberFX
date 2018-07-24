package controllers;

import javafx.scene.control.*;
import javafx.application.Platform;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import pl.mareksliwinski.Main;

import java.io.*;
import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainScreenController {


    public static final int LIMIT_VALUE = 999999999;
    private Main main;

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private Label numberLabel;

    @FXML
    public HBox startButton;

    @FXML
    public Label labelInfoPerfect;

    @FXML
    public Label dummyLabel;

    @FXML
    private ImageView bell;

    @FXML
    public void start() {

        StringProperty value = new SimpleStringProperty();
        StringProperty perfectValue = new SimpleStringProperty();
        StringProperty dummyNumber = new SimpleStringProperty();
        numberLabel.textProperty().bind(value);
        labelInfoPerfect.textProperty().bind(perfectValue);
        bell.visibleProperty().bind(perfectValue.isNotEmpty());
        dummyLabel.textProperty().bind(dummyNumber);

        List<Integer> startend = new ArrayList<>();
        fileLoader(startend);

        int start = startend.get(0);
        //int limit = startend.get(1);

        Task<Integer> task = new Task<Integer>() {
            int perfVal = 0;
            int dummyVal = 0;

            @Override
            protected Integer call() throws Exception {
                int i;
                for (i = start; i < LIMIT_VALUE; i++) {
                    final int val = i;
                    dummyVal = i;
                    if (i % 2 == 0) {
                        int sum = 0;
                        for (int j = 1; j <= i / 2; j++) {
                            if (i % j == 0)
                                sum += j;
                        }
                        if (sum % i == 0) {
                            System.out.println("Perfect number: " + i);
                            perfVal = i;
                            writePerfectNumber(i);
                        }
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                    // Update the GUI on the JavaFX Application Thread
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            value.setValue(df(val));
                            dummyNumber.setValue(String.valueOf(dummyVal));
                            if (perfVal != 0)
                                perfectValue.setValue(df(perfVal));
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

    @FXML
    public void showList() {
        main.loadListScreen();
    }

    @FXML
    public void exit() {
        Optional<ButtonType> result = alertExitButton();
        if (result.get() == ButtonType.OK) {
            writeLimits();
            Platform.exit();
            System.exit(0);
        }
    }

    @FXML
    public void initialize() {
        bell.visibleProperty().setValue(false);
        dummyLabel.visibleProperty().setValue(false);
    }

    public String df(long number) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
        return decimalFormat.format(number);
    }

    public void fileLoader(List<Integer> startend) {
        String line;
        int number;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("startend.txt")))) {

            while ((line = bufferedReader.readLine()) != null) {
                number = Integer.parseInt(line);
                startend.add(number);
            }
        } catch (Exception e) {
            loadError(e.getMessage());
            Platform.exit();
            System.exit(0);
        }
    }

    public void writePerfectNumber(int perfectNumber) {

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("perfect.txt").getAbsoluteFile(), true))) {
            bufferedWriter.write(perfectNumber + "\n");
        } catch (Exception e) {
            loadError(e.getMessage());
        }
    }

    public void writeLimits() {
        if (!dummyLabel.getText().equals("Label")) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("startend.txt")))) {
                bufferedWriter.write(dummyLabel.getText() + "\n");
                bufferedWriter.write(String.valueOf(LIMIT_VALUE));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Optional<ButtonType> alertExitButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("");
        alert.setTitle("POTWIERDZENIE");
        alert.setContentText("Czy na pewno chcesz wyjść z aplikacji?" + " Plik z ostatnim sprawdzanym numerem zostanie zapisany.");
        return alert.showAndWait();
    }

    public void loadError(String error) {
        Alert readError = new Alert(Alert.AlertType.ERROR);
        readError.setTitle("BŁĄD!");
        readError.setHeaderText("Coś poszło nie tak!");
        TextArea textArea = new TextArea(error);
        textArea.setPrefHeight(100);
        readError.getDialogPane().setContent(textArea);
        readError.showAndWait();
    }
}
