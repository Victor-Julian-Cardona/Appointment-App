package Controllers;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import database.DBConnection;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class login implements Initializable {

    public TextField namefield;
    public PasswordField passfield;
    public Label timezonefield;
    public Button loginButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ZoneId userZone = ZoneId.systemDefault();
        timezonefield.setText(userZone.toString());
    }


    public void loginPress(javafx.event.ActionEvent actionEvent) throws SQLException {

        String userName = namefield.getText();
        String passInput = passfield.getText();
        String query = "SELECT Password FROM USERS WHERE User_Name = '" + userName + "'";
        Connection conn = DBConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();


        try {
            rs.next();
            String userPass = rs.getString("Password");

            if (passInput.equals(userPass)) {
                System.out.println("Success");
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Password");
                alert.setContentText("Please enter correct Password.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid User Name");
            alert.setContentText("Please enter valid User Name.");
            alert.showAndWait();
        }
    }
}
