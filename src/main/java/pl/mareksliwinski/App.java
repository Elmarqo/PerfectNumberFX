package pl.mareksliwinski;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Hello world!
 */
public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/StackPane.fxml"));
        StackPane stackPane = loader.load();
        Scene scene = new Scene(stackPane);
        primaryStage.setTitle("Perfect Numbers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
