package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import pl.mareksliwinski.App;

import java.io.*;
import java.text.DecimalFormat;


public class ListController {

    private StackPaneController stackPaneController;
    private MainScreenController mainScreenController;
    private App app;
    private Stage stage;

    public void setApp(App app, Stage stage) {
        this.app = app;
        this.stage = stage;
    }

    public void setStackPaneController(StackPaneController stackPaneController) {
        this.stackPaneController = stackPaneController;
    }

    @FXML
    private ListView listID;

    @FXML
    public void initialize() throws IOException {
        File file = new File("perfect.txt");

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
        }
    }

    @FXML
    public void home() {
        stackPaneController.setMainScreen();
    }

    @FXML
    public void exit() {
    app.listScreen();
    stage.close();


        /*Platform.exit();
        System.exit(0);*/
    }

    public String df(int number){
        DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
        return decimalFormat.format(number);
    }

}
