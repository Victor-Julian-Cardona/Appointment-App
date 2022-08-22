package Main;


import database.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;

/**
 * main class that contains methods for the application
 */
public class Main extends Application {

    /**
     * Sets the Stage and the Scene to start the applicaiton
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("../view/AppointmentScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Appointment Management Program");
        stage.show();
    }

    /**
     * Main method that launches all agruments of the application
     * @param args
     */
    public static void main(String[] args) {
        // Locale.setDefault(new Locale("fr"));
        DBConnection.startConnection();
        launch(args);
        DBConnection.closeConnection();
    }
}
