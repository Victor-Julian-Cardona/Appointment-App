package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class mainScreen implements Initializable {
    public Button custButton;
    public Button exitButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
    }

    public void custPress(ActionEvent actionEvent) throws IOException {
        Parent mainScreen = FXMLLoader.load(getClass().getResource(("../view/customerScreen.fxml")));
        Scene scene = new Scene(mainScreen);
        Stage window = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void exitPress(ActionEvent actionEvent) {
        Stage close = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        close.close();
    }
}
