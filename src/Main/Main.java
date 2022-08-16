package Main;

import database.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
       // URL pop = getClass().getResource("view.practice.fxml");
      //  System.out.println(pop)
        Parent root = FXMLLoader.load(getClass().getResource("view.practice.fxml"));
        stage.setTitle("practice table");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        DBConnection.startConnection();
        launch(args);
        DBConnection.closeConnection();
    }

}
