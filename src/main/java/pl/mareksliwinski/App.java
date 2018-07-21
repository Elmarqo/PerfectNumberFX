package pl.mareksliwinski;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StackPane.fxml"));
        Pane Pane = loader.load();
        Scene scene = new Scene(Pane);
        primaryStage.setTitle("Perfect Numbers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
