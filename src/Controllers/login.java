package Controllers;

import Model.Appointment;
import Model.AppointmentList;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import database.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Login screen controller
 * Sets up logic for login screen
 */
public class login implements Initializable {

    public TextField namefield;
    public PasswordField passfield;
    public Label timezonefield;
    public Button loginButton;
    public Label userNameLabel;
    public Label passLabel;
    public Label timeZoneLabel;
    public Label title;
    public Button exitButton;

    /**
     * Declare and set locale language to be used by methods
     */
    private final String lang = Locale.getDefault().getDisplayName();

    /**
     * Declare Appointment list to be used in login Controller class
     */
    private AppointmentList aList = new AppointmentList();

    /**
     * Declare user String to be supplied my Lambda expressions in Controllers
     */
    public static String user = "tester";

    /**
     * Lambda supplier expression to get current date and time
     */
    public Supplier<Timestamp> getCurrTime = () -> Timestamp.valueOf(LocalDateTime.now());

    /** Initializes login screen
     * Sets time zone label to user time zone
     * Changes all text in log in form to french if locale language is french
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ZoneId userZone = ZoneId.systemDefault();
        timezonefield.setText(userZone.toString());

        if (lang.equals("français")) {

            userNameLabel.setText("Nom d'utilsateur:");
            passLabel.setText("Le mot de passe:");
            timeZoneLabel.setText("Fuseau horaire:");
            loginButton.setText("Connexion");
            exitButton.setText("Sortir");
            title.setText("Veuillez entrer les informations d'identification");
        }
    }

    /**
     * Method to check if user name exists in database
     * Also checks if entered password matches the password for the user name in the database
     * Displays errors in french or english depending on user locale language
     * @param actionEvent
     * @throws SQLException
     */
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
                Parent mainScreen = FXMLLoader.load(getClass().getResource(("../view/mainScreen.fxml")));
                Scene scene = new Scene(mainScreen);
                Stage window = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
                user = userName;

                // Checks if there are appointments within 15 minutes of successful login
                aList.updateAppointments();
                Stream<Appointment> myStream = aList.getAppointmentList().stream();
                List<Appointment> results = myStream.filter(c -> (c.getStart().getTime() >= getCurrTime.get().getTime()) && (c.getStart().getTime() <= (getCurrTime.get().getTime() + 900000))).collect(Collectors.toList());
                Stream<Appointment> myStream2 = aList.getAppointmentList().stream();

                long resultsCount = myStream2.filter(c -> (c.getStart().getTime() >= getCurrTime.get().getTime()) && (c.getStart().getTime() <= (getCurrTime.get().getTime() + 900000))).count();

                List<Integer> idList = new ArrayList();
                List<LocalDateTime> startList = new ArrayList();
                results.forEach(t -> startList.add(t.getStart().toLocalDateTime()));
                results.forEach(t -> idList.add(t.getAppId()));

                if (resultsCount > 0) {

                    String message = "There is an appointment in the next 15 minutes.";
                    message = message + " An appointment today: " + startList.get(0).toLocalDate() + " that starts at " + startList.get(0).toLocalTime() + ". With the following ID: " + idList.get(0) + ".";

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Appointments soon!");
                    alert.setContentText(message);
                    alert.showAndWait();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No appointments soon");
                    alert.setContentText("There are no appointments within the next 15 minutes");
                    alert.showAndWait();
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                if (lang.equals("français"))  {
                    alert.setTitle("Mot de passe incorrect");
                    alert.setContentText("Veuillez entrer le mot de passe correct.");
                }
                else {
                    alert.setTitle("Invalid Password");
                    alert.setContentText("Please enter correct Password.");
                }
                alert.showAndWait();
            }
        } catch (SQLException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            if (lang.equals("français")) {
                alert.setTitle("Nom d'utilisateur invalide");
                alert.setContentText("Veuillez entrer un nom d'utilisateur valide.");
            }
            else {
                alert.setTitle("Invalid User Name");
                alert.setContentText("Please enter valid User Name.");
            }
            alert.showAndWait();
        }
    }

    /**
     * Method to close the stage for the program
     * @param actionEvent
     */
    public void exitPress(ActionEvent actionEvent) {
        Stage close = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        close.close();
    }
}
