package dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;
import java.util.ResourceBundle;

public class DialogsTools {

    static ResourceBundle bundle = ResourceBundle.getBundle("dialogs.messages");

    public static Optional<ButtonType> exitDialog(){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(bundle.getString("exit.title"));
        alert.setHeaderText(bundle.getString("exit.header"));
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }
}
