package Controllers;

import Model.Appointment;
import Utility.AppointmentList;
import Utility.Function;
import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableIntegerArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Supplier;

public class AppointmentScreen implements Initializable {

    public TableColumn appIdCol;
    public TableColumn titleCol;
    public TableColumn descCol;
    public TableColumn locationCol;
    public TableColumn typeCol;
    public TableColumn startCol;
    public TableColumn endCol;
    public TableColumn custIdCol;
    public TableColumn userIdCol;
    public TableView appointmentsTable;
    public Button upButton;
    public Button saveButton;
    public Button cancelButton;
    public Button delButton;
    public Button addButton;
    public Button clearButton;
    public ComboBox startHourBox;
    public ComboBox startMinBox;
    public ComboBox endHourBox;
    public ComboBox endMinBox;
    public TextField appIdField;
    public TextField titleField;
    public TextField typeField;
    public TextField descField;
    public TextField locField;
    public ComboBox customerBox;
    public DatePicker dateBox;
    public ComboBox contactBox;
    public TextField userIdField;
    public Button backButton;
    public RadioButton allRad;
    public RadioButton thisWeekRad;
    public RadioButton thisMonthRad;
    public ToggleGroup tableFilter;

    /**
     * declare LocalDateTimes start and end parsed from comboBoxes
     */
    private LocalDateTime startDateTime, endDateTime;

    /**
     * Declare object aList of the AppointmentList class to be used in the AppointmentScreen controller
     */
    private AppointmentList aList = new AppointmentList();

    /**
     * Declare observable list of all customerIds
     */
    private ObservableList<String> customerIdList = FXCollections.observableArrayList();

    /**
     * Declare observable list of all contacts
     */
    private ObservableList<String> contactList = FXCollections.observableArrayList();

    /**
     * Lambda supplier expression to get user name from login screen
     */
    Supplier<String> getUser = () -> login.user;

    /**
     * Lambda supplier expression to get current date andtime
     */
    Supplier<Timestamp> getTime = () -> Timestamp.valueOf(LocalDateTime.now());

    /**
     * Method to get observable list of buisness hours
     */
    public ObservableList getHourList() {
        ObservableList<String> hourList = FXCollections.observableArrayList();
        for ( int i = 8; i < 23; i++) {
            hourList.add(String.valueOf(i));
        }
        return hourList;
    }

    /**
     * Method to get list of minutes
     */
    public ObservableList getMinuteList() {
        ObservableList<String> minuteList = FXCollections.observableArrayList();
        for ( int i = 0; i < 60; i++) {
            minuteList.add(String.valueOf(i));
        }
        return minuteList;
    }

    /**
     * Method to populate list of available customer ids
     */
    public void setCustomerIdList() throws SQLException {
        String query = "SELECT Customer_ID FROM customers ORDER BY customer_id";
        Connection conn = DBConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("customer_ID");
            customerIdList.add(String.valueOf(id));
        }
    }

    /**
     * Method to populate list of available contacts
     * @throws SQLException
     */
    public void setContactList() throws SQLException {
        String query = "SELECT Contact_name FROM contacts ORDER BY contact_name";
        Connection conn = DBConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            String contact = rs.getString("contact_name");
            contactList.add(contact);
        }
    }

    public void setDateTime() {
        //parse date
        LocalDate dateInput = dateBox.getValue();

        //parse hours and minutes of end and start
        try {
            int startTimeHour = Integer.parseInt(String.valueOf(startHourBox.getValue()));
            int endTimeHour = Integer.parseInt(String.valueOf(endHourBox.getValue()));
            int startTimeMin = Integer.parseInt(String.valueOf(startMinBox.getValue()));
            int endTimeMin = Integer.parseInt(String.valueOf(endMinBox.getValue()));

            //create LocalTime variables with parsed end and start times
            LocalTime timeStart = LocalTime.of(startTimeHour, startTimeMin);
            LocalTime timeEnd = LocalTime.of(endTimeHour, endTimeMin);

            //create LocalDateTime variables with parsed end and start times and parsed Date
            startDateTime = LocalDateTime.of(dateInput, timeStart);
            endDateTime = LocalDateTime.of(dateInput, timeEnd);
        }
        catch (NumberFormatException | NullPointerException e) {
        }

    }

    /**
     * Method to initialize screen, populates all tables and boxes
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        aList.clearAppointmentList();
        aList.updateAppointments();
        appointmentsTable.setItems(aList.getAppointmentList());

        //populate table
        appIdCol.setCellValueFactory(new PropertyValueFactory<>("appId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("locat"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("custId"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

        //populate time comboBoxes
        startHourBox.setItems(getHourList());
        endHourBox.setItems(getHourList());
        startMinBox.setItems(getMinuteList());
        endMinBox.setItems(getMinuteList());

        //populate customer id comboBox
        try {
            setCustomerIdList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        customerBox.setItems(customerIdList);

        //populate UserIdField
        try {
            int userId = Function.getUserId(getUser.get());
            userIdField.setText(String.valueOf(userId));
        } catch (SQLException throwables) {
            userIdField.setText("1");
        }

        //populate contact comboBox
        try {
            setContactList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        contactBox.setItems(contactList);
    }

    public void addPress(ActionEvent actionEvent) throws SQLException {
        if (appIdField.getText().isEmpty()) {
            // declare boolean that indicates successful addition to database
            boolean updated = true;

            //make new sequential id
            int max = 1;
            for (int i = 0; i < aList.getAppointmentList().size(); i++) {
                if (max == aList.getAppointmentList().get(i).getAppId()) {
                    max++;
                }
            }

            //Error message if user does not populate all fields
            if (titleField.getText().isEmpty() || descField.getText().isEmpty() || locField.getText().isEmpty() || typeField.getText().isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insufficient information");
                alert.setContentText("Please fill all information boxes");
                alert.showAndWait();
                updated = false;
                return;
            }

            //parse data entered by fields
            String titleInput = titleField.getText();
            String descInput = descField.getText();
            String locationInput = locField.getText();
            String typeInput = typeField.getText();

            //get current user and current time using lambdas
            Timestamp todayTime = getTime.get();
            String createBy = getUser.get();

            //get selected date
            setDateTime();

            //get user
            int userId = Integer.parseInt(userIdField.getText());

            //set connection and sql statement
            Connection conn = DBConnection.getConnection();
            String query = "INSERT INTO Appointments Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            try {
                //Parse contact and customer id comboBoxes
                int custId = Integer.parseInt(String.valueOf(customerBox.getValue()));
                int contactId = Function.getContactId(String.valueOf(contactBox.getValue()));

                //get selected date
                setDateTime();

                //Populate sql statement
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, max);
                statement.setString(2, titleInput);
                statement.setString(3, descInput);
                statement.setString(4, locationInput);
                statement.setString(5, typeInput);
                statement.setTimestamp(6, Timestamp.valueOf(startDateTime));
                statement.setTimestamp(7, Timestamp.valueOf(endDateTime));
                statement.setTimestamp(8, todayTime);
                statement.setString(9, createBy);
                statement.setTimestamp(10, todayTime);
                statement.setString(11, createBy);
                statement.setInt(12, custId);
                statement.setInt(13, userId);
                statement.setInt(14, contactId);

                //execute sql statement
                statement.executeUpdate();


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insufficient Information");
                alert.setContentText("Please select appointment date, start time and end time");
                alert.showAndWait();
                updated = false;
            }
            catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insufficient Information");
                alert.setContentText("Please select customer Id and contact");
                alert.showAndWait();
                updated = false;
            }

            if (updated) {
                //Update table with new values
                aList.clearAppointmentList();
                aList.updateAppointments();
                appointmentsTable.setItems(aList.getAppointmentList());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Currently Updating");
            alert.setContentText("Cannot add appointment when currently updating appointment. Please press clear button, then proceed to add appointment");
            alert.showAndWait();
        }
    }

    public void upPress(ActionEvent actionEvent) {
    }

    public void savePress(ActionEvent actionEvent) {
    }

    public void cancelPress(ActionEvent actionEvent) {
    }

    public void delPress(ActionEvent actionEvent) {
    }

    public void clearPress(ActionEvent actionEvent) {
    }

    public void backPress(ActionEvent actionEvent) {
    }

    public void dateBoxSelect(ActionEvent actionEvent) {
    }
}
