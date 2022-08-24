package Controllers;

import Model.Appointment;
import Utility.AppointmentList;
import Utility.Converters;
import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
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
     * Declare boolean to check if callendar and comboBoxes are listening
     */
    private boolean boxListening = true;

    /**
     * Declare object aList of the AppointmentList class to be used in the AppointmentScreen controller
     */
    private AppointmentList aList = new AppointmentList();

    /**
     * Declare observable list of all customerIds
     */
    private ObservableList<String> customerList = FXCollections.observableArrayList();

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
    public void setCustomerList() throws SQLException {
        String query = "SELECT Customer_name FROM customers ORDER BY customer_name";
        Connection conn = DBConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            String customer = rs.getString("customer_name");
            customerList.add(customer);
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

    /**
     * Parse date and time combo boxes
     */
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
     * Method to clear all fields in the form
     */
    public void clearFields() {
        //clear fields
        titleField.clear();
        descField.clear();
        locField.clear();
        typeField.clear();
        appIdField.clear();
        customerBox.getSelectionModel().clearSelection();
        contactBox.getSelectionModel().clearSelection();

        //clear boxes
        boxListening = false;
        dateBox.setValue(null);
        startHourBox.getSelectionModel().clearSelection();
        startMinBox.getSelectionModel().clearSelection();
        endHourBox.getSelectionModel().clearSelection();
        endMinBox.getSelectionModel().clearSelection();
        boxListening  = true;

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
            setCustomerList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        customerBox.setItems(customerList);

        //populate UserIdField
        try {
            int userId = Converters.getUserId(getUser.get());
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

    /**
     * Method that gives functionalty to the add Appointment button which adds appointment with entered information into database
     * @param actionEvent
     * @throws SQLException
     */
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
                int custId = Converters.getCustomerId(String.valueOf(customerBox.getValue()));
                int contactId = Converters.getContactId(String.valueOf(contactBox.getValue()));

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
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insufficient Information");
                alert.setContentText("Please select Appointment customer and contact");
                alert.showAndWait();
                updated = false;
            }
            catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insufficient Information");
                alert.setContentText("Please select appointment date, start time and end time");
                alert.showAndWait();
                updated = false;
            }

            if (updated) {
                //Update table with new values
                aList.clearAppointmentList();
                aList.updateAppointments();
                appointmentsTable.setItems(aList.getAppointmentList());

                //clear fields
                clearFields();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Currently Updating");
            alert.setContentText("Cannot add appointment when currently updating appointment. Please press clear button, then proceed to add appointment");
            alert.showAndWait();
        }
    }

    /**
     * gives functionality to update appointment button
     * populates all fields with appointment information
     * @param actionEvent
     */
    public void upPress(ActionEvent actionEvent) {
        if(appIdField.getText().isEmpty()) {
            try {
                Appointment selected = (Appointment) appointmentsTable.getSelectionModel().getSelectedItem();

                //populate fields
                titleField.setText(String.valueOf(selected.getTitle()));
                descField.setText(selected.getDesc());
                locField.setText(selected.getLocat());
                typeField.setText(selected.getType());
                appIdField.setText(String.valueOf(selected.getAppId()));

                boxListening = false;

                //set Calendar selection
                LocalDate date = selected.getStart().toLocalDateTime().toLocalDate();
                dateBox.setValue(date);

                //set start and end ComboBoxes
                //start hour
                long startHours = selected.getStart().getTime();
                long totalHours = TimeUnit.MILLISECONDS.toHours(startHours);
                String hours = String.valueOf(totalHours % 24);
                for (int i = 0; i < getHourList().size(); i++) {
                    if (hours.equals(getHourList().get(i))) {
                        startHourBox.getSelectionModel().select(i);
                    }
                }

                //start min
                long startMinutes = selected.getStart().getTime();
                long totalMinutes = TimeUnit.MILLISECONDS.toMinutes(startMinutes);
                String minutes = String.valueOf(totalMinutes % 60);
                for (int i = 0; i < getMinuteList().size(); i++) {
                    if (minutes.equals(getMinuteList().get(i))) {
                        startMinBox.getSelectionModel().select(i);
                    }
                }

                //end hour
                long endHours = selected.getEnd().getTime();
                long totalEndHours = TimeUnit.MILLISECONDS.toHours(endHours);
                String hours2 = String.valueOf(totalEndHours % 24);
                for (int i = 0; i < getHourList().size(); i++) {
                    if (hours2.equals(getHourList().get(i))) {
                        endHourBox.getSelectionModel().select(i);
                    }
                }

                //end min
                long endMinutes = selected.getEnd().getTime();
                long totalEndMinutes = TimeUnit.MILLISECONDS.toMinutes(endMinutes);
                String minutes2 = String.valueOf(totalEndMinutes % 60);
                for (int i = 0; i < getMinuteList().size(); i++) {
                    if (minutes2.equals(getMinuteList().get(i))) {
                        endMinBox.getSelectionModel().select(i);
                    }
                }

                // set customer and contact comboBoxes
                int selectedCustomerId = selected.getCustId();
                customerBox.getSelectionModel().select(Converters.getCustomer(selectedCustomerId));
                int selectedContactId = selected.getContId();
                contactBox.getSelectionModel().select(Converters.getContact(selectedContactId));

                boxListening = true;

            } catch (NullPointerException | SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No selection");
                alert.setContentText("Please select Customer to update.");
                alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Currently updating");
            alert.setContentText("Please save or cancel currently updating appointment");
            alert.showAndWait();
        }
    }

    public void savePress(ActionEvent actionEvent) {
        // declare boolean that indicates successful addition to database
        boolean updated = true;

        //Error message if user does not populate all fields
        if (titleField.getText().isEmpty() || descField.getText().isEmpty() || locField.getText().isEmpty() || typeField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Insufficient information");
            alert.setContentText("Please fill all information boxes");
            alert.showAndWait();
            updated = false;
            return;
        }

        //error message if user does not select date
        if (dateBox.getAccessibleText() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Insufficient information");
            alert.setContentText("Please select date");
            alert.showAndWait();
            updated = false;
            return;
        }

        //parse data entered by fields
        int appId = Integer.parseInt(appIdField.getText());
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
        String query = "UPDATE Appointments SET title = ?, description = ?, location = ?, type = ?, start = ?, end = ?, create_date = ?, created_by = ?, last_update = ?, last_updated_by = ?, customer_id = ?, user_id = ?, contact_id = ? WHERE appointment_id = " + appId;

        try {
            //Parse contact and customer id comboBoxes
            int custId = Converters.getCustomerId(String.valueOf(customerBox.getValue()));
            int contactId = Converters.getContactId(String.valueOf(contactBox.getValue()));

            //get selected date
            setDateTime();

            //Populate sql statement
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, titleInput);
            statement.setString(2, descInput);
            statement.setString(3, locationInput);
            statement.setString(4, typeInput);
            statement.setTimestamp(5, Timestamp.valueOf(startDateTime));
            statement.setTimestamp(6, Timestamp.valueOf(endDateTime));
            statement.setTimestamp(7, todayTime);
            statement.setString(8, createBy);
            statement.setTimestamp(9, todayTime);
            statement.setString(10, createBy);
            statement.setInt(11, custId);
            statement.setInt(12, userId);
            statement.setInt(13, contactId);

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

        if (updated) {
            //Update table with new values
            aList.clearAppointmentList();
            aList.updateAppointments();
            appointmentsTable.setItems(aList.getAppointmentList());

            //clear fields
            clearFields();
        }
    }

    /**
     * Adds functionality to cancel button
     * clears fields and cancels updating customer
     * @param actionEvent
     */
    public void cancelPress(ActionEvent actionEvent) {
        clearFields();
    }

    /**
     * Adds functionality to delete button which deletes selected appointment
     * @param actionEvent
     */
    public void delPress(ActionEvent actionEvent) {
        try {
            Appointment selected = (Appointment) appointmentsTable.getSelectionModel().getSelectedItem();
            int idDel = selected.getAppId();

            Connection conn = DBConnection.getConnection();
            String query = "Delete from appointments WHERE appointment_id = " + idDel;

            try {
                PreparedStatement statement = conn.prepareStatement(query);
                statement.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            aList.clearAppointmentList();
            aList.updateAppointments();
            appointmentsTable.setItems(aList.getAppointmentList());
            clearFields();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success!");
            alert.setContentText("Appointment has been deleted from database");
            alert.showAndWait();

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No selection");
            alert.setContentText("Please select Appointment to delete.");
            alert.showAndWait();
        }
    }

    /**
     * gives functionality to clear button
     * clears allfields
     * @param actionEvent
     */
    public void clearPress(ActionEvent actionEvent) {
        clearFields();
    }

    /**
     * Adds functionality to back button
     * returns customer to main screen
     * @param actionEvent
     * @throws IOException
     */
    public void backPress(ActionEvent actionEvent) throws IOException {
        Parent mainScreen = FXMLLoader.load(getClass().getResource(("../view/mainScreen.fxml")));
        Scene scene = new Scene(mainScreen);
        Stage window = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Method that makes sure selected date is not in the past
     * @param actionEvent
     */
    public void dateBoxSelect(ActionEvent actionEvent) {
        if (boxListening) {
            LocalDate selected = dateBox.getValue();
            if (selected.compareTo(LocalDate.now()) < 0) {
                dateBox.setValue(null);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Date");
                alert.setContentText("Please select future or present date");
                alert.showAndWait();
            }
        }
    }
}
