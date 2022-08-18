package Main;

import DBAccess.DBCountries;
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

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
        stage.setTitle("Login Form");
        stage.setScene(new Scene(root));
        stage.show();
    }


    public static void main(String[] args) {
        // Locale.setDefault(new Locale("fr"));
        DBConnection.startConnection();
        launch(args);
        DBConnection.closeConnection();
    }
}
