package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import pl.mareksliwinski.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DecimalFormat;

public class ListController {

    private Main main;
    private Stage secondaryStage;

    public void setMain(Main main, Stage secondaryStage) {
        this.main = main;
        this.secondaryStage = secondaryStage;
    }

    @FXML
    private ListView listID;

    @FXML
    public void initialize() throws Exception {
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
    public void exit() {
        secondaryStage.close();
    }

    public String df(int number){
        DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
        return decimalFormat.format(number);
    }
}
