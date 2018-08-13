package controllers;

import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.application.Platform;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import pl.mareksliwinski.Main;

import java.io.*;
import java.sql.*;
import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainScreenController {


    public static final int LIMIT_VALUE = 999999999;
    private Main main;
    private Stage primaryStage;
    private static double xOffset = 0;
    private static double yOffset = 0;
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setMain(Main main, Stage primaryStage) {
        this.main = main;
        this.primaryStage = primaryStage;
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
    private Region region;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label titleLabe;

    @FXML
    void mouseDragged() {
        anchorPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() + xOffset);
                primaryStage.setY(event.getScreenY() + yOffset);
            }
        });
    }

    @FXML
    void mousePressed(MouseEvent event) {
        anchorPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = primaryStage.getX() - event.getScreenX();
                yOffset = primaryStage.getY() - event.getScreenY();
            }
        });
    }

    @FXML
    public void start() throws SQLException {
        connect2DB();
        StringProperty value = new SimpleStringProperty();
        StringProperty perfectValue = new SimpleStringProperty();
        StringProperty dummyNumber = new SimpleStringProperty();
        numberLabel.textProperty().bind(value);
        labelInfoPerfect.textProperty().bind(perfectValue);
        bell.visibleProperty().bind(perfectValue.isNotEmpty());
        dummyLabel.textProperty().bind(dummyNumber);

        //List<Integer> startend = new ArrayList<>();
        //fileLoader(startend);

        int start = getStartNumberFromDB();
        connection.close();
        //int start = startend.get(0);
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
                            perfVal = i;
                            connect2DB();
                            writePerfectNumber(i);
                            addNewPerfectNumber2DB(i);
                            connection.close();
                        }
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        alerts(e.getMessage());
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
            connect2DB();
            updateLimitsInDB();
            try {
                connection.close();
            } catch (SQLException e) {
                loadError(e.getMessage());
            }
            Platform.exit();
            System.exit(0);
        }
    }

    @FXML
    public void info() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Perfect NumberFX");
        alert.setHeaderText("Perfect NumberFX ver 1.1 SQL. \n(C) Marek Śliwiński. \nIcons made by Freepik from www.flaticon.com");
        alert.showAndWait();
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

        try (BufferedWriter bufferedWriter =
                     new BufferedWriter(new FileWriter(new File("perfect.txt").getAbsoluteFile(), true))) {
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
                loadError(e.getMessage());
            }
        }
    }

    public Optional<ButtonType> alertExitButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("");
        alert.setTitle("POTWIERDZENIE");
        alert.setContentText("Czy na pewno chcesz wyjść z aplikacji?" + " Plik z ostatnim sprawdzanym numerem " +
                "zostanie zapisany.");
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

    public void connect2DB() {
        Statement statement;
        String url = "jdbc:mysql://serwer1812074.home.pl:3306/26938208_perfect?useUnicode=true" +
                "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&useSSL=false&serverTimezone=UTC";
        String userName = "26938208_perfect";
        String password = "M@rcoV3$p3r";

        try {
            connection = DriverManager.getConnection(url, userName, password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            alerts(e.getMessage());
            Platform.exit();
            System.exit(0);
        }
    }

    public int getStartNumberFromDB() throws SQLException {

        String query = "SELECT limits.limitnumbers FROM limits WHERE limits.id = 1";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet result = preparedStatement.executeQuery();
        result.next();
        String temp = result.getString("limitnumbers");
        int start = Integer.parseInt(temp);

        return start;
    }

    public void updateLimitsInDB() {
        if (!dummyLabel.getText().isEmpty()) {
            int zapis = Integer.parseInt(dummyLabel.getText());
            String update = "UPDATE limits SET limitnumbers = ? WHERE id = 1";

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(update);
                preparedStatement.setInt(1, zapis);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                alerts(e.getMessage());
            }
        }
    }

    public void addNewPerfectNumber2DB(int number) {
        String add = "INSERT INTO perfectnumber.perfectnumbers VALUES (NULL, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(add);
            preparedStatement.setInt(1, number);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void alerts(String string) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(string);
        alert.showAndWait();
    }
}
