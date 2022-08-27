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

/**
 * class mainScreen which controls the mainScreen view
 */
public class mainScreen implements Initializable {
    public Button custButton;
    public Button exitButton;
    public Button appButton;

    /**
     * Initializer for mainScreen class
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
    }

    /**
     * Changes screen from mainScreen to customer screen
     * @param actionEvent
     * @throws IOException
     */
    public void custPress(ActionEvent actionEvent) throws IOException {
        Parent mainScreen = FXMLLoader.load(getClass().getResource(("../view/customerScreen.fxml")));
        Scene scene = new Scene(mainScreen);
        Stage window = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * closes the stage and therefore the program
     * @param actionEvent
     */
    public void exitPress(ActionEvent actionEvent) {
        Stage close = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        close.close();
    }

    /**
     * Method to change from the mainScren to the AppointmentScreen
     * @param actionEvent
     * @throws IOException
     */
    public void appPress(ActionEvent actionEvent) throws IOException {
        Parent mainScreen = FXMLLoader.load(getClass().getResource(("../view/AppointmentScreen.fxml")));
        Scene scene = new Scene(mainScreen);
        Stage window = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
