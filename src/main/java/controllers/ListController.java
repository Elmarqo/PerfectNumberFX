package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pl.mareksliwinski.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.text.DecimalFormat;

public class ListController {

    private Main main;
    private Stage secondaryStage;
    private static double xOffset = 0;
    private static double yOffset = 0;
    private MainScreenController mainScreenController;

    public void setMainScreenController() {
        this.mainScreenController = new MainScreenController();
    }

    public MainScreenController getMainScreenController() {
        return mainScreenController;
    }

    public void setMain(Main main, Stage secondaryStage) {
        this.main = main;
        this.secondaryStage = secondaryStage;
    }

    @FXML
    private ListView listID;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    void mouseDragged() {
        anchorPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                secondaryStage.setX(event.getScreenX() + xOffset);
                secondaryStage.setY(event.getScreenY() + yOffset);
            }
        });
    }

    @FXML
    void mousePressed(MouseEvent event) {
        anchorPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = secondaryStage.getX() - event.getScreenX();
                yOffset = secondaryStage.getY() - event.getScreenY();
            }
        });

    }

    @FXML
    public void initialize() throws Exception {
        /*File file = new File("perfect.txt");

        if (!file.exists())
            System.out.println("Brak pliku " + file + " w katalogu aplikacji.");
        else {
            String line;
            ObservableList<String> list = FXCollections.observableArrayList();
            BufferedReader bufferedReader = null;

            try {
                bufferedReader = new BufferedReader(new FileReader(file));
                while ((line = bufferedReader.readLine()) != null) {
                    list.add(df(Integer.parseInt(line)));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bufferedReader.close();
            listID.setItems(list);
        }*/
        setMainScreenController();
        ObservableList<String> list = FXCollections.observableArrayList();
        String url = "jdbc:mysql://serwer1812074.home.pl:3306/26938208_perfect?useUnicode=true&useJDBCCompliantTimezoneShift=true" +
                "&useLegacyDatetimeCode=false&useSSL=false&serverTimezone=UTC";
        String userName = "26938208_perfect";
        String password = "M@rcoV3$p3r";

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            getMainScreenController().alerts(e.getMessage());
            Platform.exit();
            System.exit(0);
        }
        String line = "SELECT perfectnumbers from perfectnumbers";
        PreparedStatement preparedStatement = connection.prepareStatement(line);
        ResultSet resultSet = preparedStatement.executeQuery(line);
        int counter = 0;
        while (resultSet.next()) {
            int record = resultSet.getInt("perfectnumbers");
            list.add(df(record));
        }
        listID.setItems(list);
        connection.close();
    }

    @FXML
    public void exit() {
        secondaryStage.close();
    }

    public String df(int number) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
        return decimalFormat.format(number);
    }
}