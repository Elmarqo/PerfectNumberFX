package pl.mareksliwinski;

import controllers.ListController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StackPane.fxml"));
        Pane pane = loader.load();
        Scene scene = new Scene(pane);
        primaryStage.setTitle("Perfect Numbers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void listScreen(){
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/List.fxml"));
        AnchorPane anchorPane = null;

        try {
            anchorPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(anchorPane);
        Stage secondaryStage = new Stage();

        ListController listController = loader.getController();
        listController.setApp(this, secondaryStage);
        secondaryStage.initModality(Modality.APPLICATION_MODAL);
        secondaryStage.setScene(scene);
        secondaryStage.show();
    }
}
