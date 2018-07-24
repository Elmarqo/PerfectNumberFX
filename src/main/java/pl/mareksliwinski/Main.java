package pl.mareksliwinski;

import controllers.ListController;
import controllers.MainScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        loadMainScreen();
    }

    public void loadMainScreen() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainScreen.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainScreenController mainScreenController = loader.getController();
        mainScreenController.setMain(this);

        Scene scene = new Scene(pane);
        primaryStage.setTitle("Perfect Numbers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void loadListScreen(){

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/List.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(pane);
        Stage secondaryStage = new Stage();

        ListController listController = loader.getController();
        listController.setMain(this, secondaryStage);
        secondaryStage.initModality(Modality.APPLICATION_MODAL);
        secondaryStage.setScene(scene);
        secondaryStage.show();
    }
}
