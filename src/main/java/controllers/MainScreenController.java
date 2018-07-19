package controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import dialogs.DialogsTools;
import javafx.application.Platform;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.*;
import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainScreenController {


    public static final int LIMIT_VALUE = 999999999;
    private StackPaneController stackPaneController;

    public void setStackPaneController(StackPaneController stackPaneController) {
        this.stackPaneController = stackPaneController;
    }

    @FXML
    private Label numberLabel;

    @FXML
    public HBox startButton;

    @FXML
    public Label labelInfoPerfect;

    @FXML
    public FontAwesomeIconView bell;

    @FXML
    public Label dummyLabel;

    @FXML
    public void start() {

        StringProperty value = new SimpleStringProperty();
        StringProperty perfectValue = new SimpleStringProperty();
        StringProperty dummyNumber = new SimpleStringProperty();
        numberLabel.textProperty().bind(value);
        labelInfoPerfect.textProperty().bind(perfectValue);
        bell.visibleProperty().bind(perfectValue.isNotEmpty());
        dummyLabel.textProperty().bind(dummyNumber);

        int number;
        String line;
        List<Integer> startend = new ArrayList<>();

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

        Task<Integer> task = new Task<Integer>() {
            int perfVal = 0;
            int dummyVal = 0;

            @Override
            protected Integer call() throws Exception {
                int i;
                for (i = start; i < limit; i++) {
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

        if (!dummyLabel.getText().equals("Label")) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("startend.txt")))) {
                bufferedWriter.write(dummyLabel.getText() + "\n");
                bufferedWriter.write(String.valueOf(LIMIT_VALUE));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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

        Optional<ButtonType> result = DialogsTools.exitDialog();
        if (result.get() == ButtonType.OK) {
            if (!dummyLabel.getText().equals("Label")) {
                char separator = 13;
                try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("startend.txt"))) {
                    bufferedWriter.write(dummyLabel.getText() + separator);
                    bufferedWriter.write(String.valueOf(LIMIT_VALUE));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

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

    public void writePerfectNumber(int perfectNumber) {

        File file = new File("perfect.txt");
        try (FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true)) {
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(perfectNumber + "\n");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
