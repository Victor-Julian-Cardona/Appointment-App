package Controllers;

import Model.Appointment;
import Model.AppointmentList;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Appointment screen class which controls the AppointmentScreen view
 */
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
    public DatePicker dateFilter;

    /**
     * declare LocalDateTimes start and end parsed from comboBoxes
     */
    private Timestamp startDateTime, endDateTime;

    /**
     * Declare boolean to check if calendar and comboBoxes are listening
     */
    private boolean boxListening = true;

    /**
     * declare boolean to indicate if dates were set
     */
    private boolean datesSet = true;

    /**
     * declare Appointment object that will hold appointment currently being updated
     */
    private Appointment updatingApp;

    /**
     * Declare object aList of the AppointmentList class to be used in the AppointmentScreen controller
     */
    private AppointmentList aList = new AppointmentList();

    /**
     * Declare list filterList to be displayed when filter radials are selected
     */
    private AppointmentList filterList = new AppointmentList();

    /**
     * Declare observable list of all customers
     */
    private ObservableList<String> customerList = FXCollections.observableArrayList();

    /**
     * Declare observable list of all contacts
     */
    private ObservableList<String> contactList = FXCollections.observableArrayList();

    /**
     * Lambda supplier expression to get user name from login screen
     */
    public Supplier<String> getUser = () -> login.user;

    /**
     * Lambda supplier expression to get current date and time
     */
    public Supplier<Timestamp> getCurrTime = () -> Timestamp.valueOf(LocalDateTime.now());

    /**
     * Method to get observable list of business hours
     */
    public ObservableList getHourList() {
        ObservableList<String> hourList = FXCollections.observableArrayList();
        for ( int i = 0; i < 24; i++) {
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
     * set them to a UTC timestamp to pass to sql
     * displays error message if attempted inputs are outside of office hours
     * displays error messages if attempted inputs imply appointment has no duration or negative duration
     */
    public void setDateTime() {
        datesSet = false;
        //check all date and time related boxes have a selected value
        if(dateBox.getValue() != null && startMinBox.getValue() != null && startHourBox.getValue() != null && endHourBox.getValue() != null && endMinBox.getValue() != null) {

            //parse date, sets end date to day after selected date if start hour is greater than end hour
            LocalDate dateInputStart = dateBox.getValue();
            LocalDate dateInputEnd;

            if (Integer.parseInt(String.valueOf(startHourBox.getValue())) > Integer.parseInt(String.valueOf(endHourBox.getValue()))) {
                dateInputEnd = dateBox.getValue().plusDays(1);
            }
            else if ((Integer.parseInt(String.valueOf(startHourBox.getValue())) == Integer.parseInt(String.valueOf(endHourBox.getValue()))) && (Integer.parseInt(String.valueOf(startMinBox.getValue())) > Integer.parseInt(String.valueOf(endMinBox.getValue())))) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Appointment has no duration");
                alert.setContentText("Appointment cannot end before it starts");
                alert.showAndWait();
                datesSet = false;
                return;
            }
            else if ((Integer.parseInt(String.valueOf(startHourBox.getValue())) == Integer.parseInt(String.valueOf(endHourBox.getValue()))) && (Integer.parseInt(String.valueOf(startMinBox.getValue())) == Integer.parseInt(String.valueOf(endMinBox.getValue())))) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Appointment has no duration");
                alert.setContentText("Appointment cannot start and end at same time");
                alert.showAndWait();
                datesSet = false;
                return;
            }
            else {
                dateInputEnd = dateBox.getValue();
            }

            //parse hours and minutes of end and start
            try {
                int startTimeHour = Integer.parseInt(String.valueOf(startHourBox.getValue()));
                int endTimeHour = Integer.parseInt(String.valueOf(endHourBox.getValue()));
                int startTimeMin = Integer.parseInt(String.valueOf(startMinBox.getValue()));
                int endTimeMin = Integer.parseInt(String.valueOf(endMinBox.getValue()));

                //create LocalTime variables with parsed end and start times
                LocalTime timeStart = LocalTime.of(startTimeHour, startTimeMin);
                LocalTime timeEnd = LocalTime.of(endTimeHour, endTimeMin);

                //create LocalTime variables of office hours
                LocalTime officeTimeStart = LocalTime.of(8, 0);
                LocalTime officeTimeEnd = LocalTime.of(22, 0);

                //Check to see if dates and times selected are within office hours
                Timestamp estStart = Converters.localToEST(Timestamp.valueOf(LocalDateTime.of(dateInputStart, timeStart)));
                Timestamp estEnd = Converters.localToEST(Timestamp.valueOf(LocalDateTime.of(dateInputEnd, timeEnd)));
                Timestamp officeStart = Timestamp.valueOf(LocalDateTime.of(dateInputStart, officeTimeStart));
                Timestamp officeEnd = Timestamp.valueOf(LocalDateTime.of(dateInputStart, officeTimeEnd));
                if (estStart.compareTo(officeStart) < 0 || estEnd.compareTo(officeEnd) > 0) {
                    //error message
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Appointment is outside office hours");
                    alert.setContentText("Office hours are 8:00-22:00 EST");
                    alert.showAndWait();

                    //Ends method call so no date or time data is recorded
                    datesSet = false;
                    return;

                }

                //create LocalDateTime variables with parsed end and start times and parsed Date
                //convert that LocalDateTime to a Timestamp
                //convert the timestamp to UTC time with converter method
                startDateTime = Timestamp.valueOf(LocalDateTime.of(dateInputStart, timeStart));
                endDateTime = Timestamp.valueOf(LocalDateTime.of(dateInputEnd, timeEnd));

                //Check for conflicts
                if (conflictCheck(startDateTime, endDateTime)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Conflicting appointment");
                    alert.setContentText("That date/time conflicts with another customer's appointment");
                    alert.showAndWait();
                    datesSet = false;
                }
                else {
                    startDateTime = Converters.localToUTC(startDateTime);
                    endDateTime = Converters.localToUTC(endDateTime);
                    datesSet = true;
                }

            } catch (NumberFormatException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Insufficient Information");
            alert.setContentText("Please select date, start time and end time");
            alert.showAndWait();
            datesSet = false;
        }
    }

    /**
     * Method that checks for conflicts with the given Timestamps for start and end times of an Appointment
     * returns true if there is a conflict and false if not
     * LAMBDA Predicate used for stream filter
     * @param start
     * @param end
     * @return conflict
     */
    public boolean conflictCheck(Timestamp start, Timestamp end) {
        Stream<Appointment> myStream = aList.getAppointmentList().stream();
        long conflictCount = myStream.filter(c -> (c.getStart().compareTo(start) < 0 && c.getEnd().compareTo(start) > 0) || (c.getStart().compareTo(end) < 0 && c.getEnd().compareTo(end) > 0)).count();
        Stream<Appointment> myStream2 = aList.getAppointmentList().stream();
        long conflictCountEqual = myStream2.filter( c -> c.getStart().compareTo(start) == 0 || c.getEnd().compareTo(end) == 0).count();
        Stream<Appointment> myStream3 = aList.getAppointmentList().stream();
        long conflictCountEncompass = myStream3.filter(c -> (c.getStart().compareTo(start) > 0 && c.getEnd().compareTo(end) < 0) || (c.getStart().compareTo(start) < 0 && c.getEnd().compareTo(end) > 0)).count();
        boolean conflict = false;
        if ((conflictCount + conflictCountEqual+conflictCountEncompass) > 0) {
            conflict = true;
        }
        return conflict;
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
        dateBox.setPromptText("Select Appointment month/date/year");
        startHourBox.getSelectionModel().clearSelection();
        startMinBox.getSelectionModel().clearSelection();
        endHourBox.getSelectionModel().clearSelection();
        endMinBox.getSelectionModel().clearSelection();
        boxListening  = true;
    }

    /**
     * Method to get smallest available appointment_id
     * @return
     * @throws SQLException
     */
    public int getNewAppId() throws SQLException {

        int newId = 1;
        List idList = new ArrayList();
        Connection conn = DBConnection.getConnection();
        String query = "SELECT appointment_id FROM Appointments ORDER BY appointment_id";
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            int appId = rs.getInt("appointment_id");
            idList.add(appId);
        }
        for (int i = 0; i < idList.size(); i++) {
            if (newId == Integer.parseInt(String.valueOf(idList.get(i)))) {
                newId++;
            }
        }
        return newId;
    }

    /**
     * Method to initialize screen, populates all tables and boxes
     * LAMBDA Supplier getUser is used to fetch user from login.java without having to declare a method in that class
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
     * Method that gives functionality to the add Appointment button which adds appointment with entered information into database
     * LAMBDA Supplier getCurrTime used for convenience  when Timestamp of current time is needed
     * LAMBDA Supplier getUser is used to fetch user from login.java without having to declare a method in that class
     * @param actionEvent
     * @throws SQLException
     */
    public void addPress(ActionEvent actionEvent) throws SQLException {
        if (appIdField.getText().isEmpty()) {
            // declare boolean that indicates successful addition to database
            boolean updated = true;

            int newAppId = getNewAppId();

            //Error message if user does not populate all fields
            if (titleField.getText().isEmpty() || descField.getText().isEmpty() || locField.getText().isEmpty() || typeField.getText().isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insufficient information");
                alert.setContentText("Please fill all information boxes");
                alert.showAndWait();
                return;
            }

            //parse data entered by fields
            String titleInput = titleField.getText();
            String descInput = descField.getText();
            String locationInput = locField.getText();
            String typeInput = typeField.getText();

            //get current user and current time using lambdas
            Timestamp todayTime = getCurrTime.get();
            String createBy = getUser.get();

            //get selected date
            setDateTime();
            if (!datesSet) {
                return;
            }

            //get user
            int userId = Integer.parseInt(userIdField.getText());

            //set connection and sql statement
            Connection conn = DBConnection.getConnection();
            String query = "INSERT INTO Appointments Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            try {
                //Parse contact and customer id comboBoxes
                int custId = Converters.getCustomerId(String.valueOf(customerBox.getValue()));
                int contactId = Converters.getContactId(String.valueOf(contactBox.getValue()));

                //Populate sql statement
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, newAppId);
                statement.setString(2, titleInput);
                statement.setString(3, descInput);
                statement.setString(4, locationInput);
                statement.setString(5, typeInput);
                statement.setTimestamp(6, startDateTime);
                statement.setTimestamp(7, endDateTime);
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
                alert.setContentText("Please select customer ID and contact ID");
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
                //Update table with new values depending on filter
                if (allRad.isSelected()) {
                    aList.clearAppointmentList();
                    aList.updateAppointments();
                    appointmentsTable.setItems(aList.getAppointmentList());
                }
                if (thisWeekRad.isSelected()) {
                    filterList.clearAppointmentList();
                    filterList.filterWeek(dateFilter.getValue());
                    appointmentsTable.setItems(filterList.getAppointmentList());
                }
                if(thisMonthRad.isSelected()) {
                    filterList.clearAppointmentList();
                    filterList.filterMonth(dateFilter.getValue());
                    appointmentsTable.setItems(filterList.getAppointmentList());
                }

                //clear fields
                clearFields();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Currently Updating");
            alert.setContentText("Cannot add appointment when currently updating appointment. Please clear or save before adding an Appointment");
            alert.showAndWait();
        }
    }

    /**
     * gives functionality to update appointment button
     * populates all fields with appointment information
     * LAMBDA Supplier getCurrTime used for convenience  when Timestamp of current time is needed
     * @param actionEvent
     */
    public void upPress(ActionEvent actionEvent) {
        if (appIdField.getText().isEmpty()) {
            try {
                //remove updating appointment from list and place in placeholder
                Appointment selected = (Appointment) appointmentsTable.getSelectionModel().getSelectedItem();
                updatingApp = selected;
                aList.removeAppointment(selected);

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

                //declare long off milliseconds offset from UT
                long utcOffsetMills = TimeZone.getDefault().getOffset(getCurrTime.get().getTime());

                //start hour
                long startHours = selected.getStart().getTime();
                long totalStartHours = TimeUnit.MILLISECONDS.toHours(startHours + utcOffsetMills);
                String startHoursMod = String.valueOf(totalStartHours % 24);
                for (int i = 0; i < getHourList().size(); i++) {
                    if (startHoursMod.equals(getHourList().get(i))) {
                        startHourBox.getSelectionModel().select(i);
                    }
                }

                //start min
                long startMinutes = selected.getStart().getTime();
                long totalStartMinutes = TimeUnit.MILLISECONDS.toMinutes(startMinutes);
                String startMinutesMod = String.valueOf(totalStartMinutes % 60);
                for (int i = 0; i < getMinuteList().size(); i++) {
                    if (startMinutesMod.equals(getMinuteList().get(i))) {
                        startMinBox.getSelectionModel().select(i);
                    }
                }

                //end hour
                long endHours = selected.getEnd().getTime();
                long totalEndHours = TimeUnit.MILLISECONDS.toHours(endHours + utcOffsetMills);
                String endHoursMod = String.valueOf(totalEndHours % 24);
                for (int i = 0; i < getHourList().size(); i++) {
                    if (endHoursMod.equals(getHourList().get(i))) {
                        endHourBox.getSelectionModel().select(i);
                    }
                }

                //end min
                long endMinutes = selected.getEnd().getTime();
                long totalEndMinutes = TimeUnit.MILLISECONDS.toMinutes(endMinutes);
                String endMinutesMod = String.valueOf(totalEndMinutes % 60);
                for (int i = 0; i < getMinuteList().size(); i++) {
                    if (endMinutesMod.equals(getMinuteList().get(i))) {
                        endMinBox.getSelectionModel().select(i);
                    }
                }

                // set customer and contact comboBoxes
                int selectedCustomerId = selected.getCustId();
                customerBox.getSelectionModel().select(Converters.getCustomer(selectedCustomerId));
                int selectedContactId = selected.getContId();
                contactBox.getSelectionModel().select(Converters.getContact(selectedContactId));

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

    /**
     * Functionality for save button that updates selected appointment if all fields and boxes are populated adequately
     * Displays error messages if not
     * LAMBDA Supplier getCurrTime used for convenience  when Timestamp of current time is needed
     * LAMBDA Supplier getUser is used to fetch user from login.java without having to declare a method in that class
     * @param actionEvent
     */
    public void savePress(ActionEvent actionEvent) {
        // declare boolean that indicates successful addition to database
        boolean updated = true;

        //error message if user is not currently updating
        if (appIdField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Not updating");
            alert.setContentText("Please select appointment to update before attempting to save");
            alert.showAndWait();
            return;
        }

        //Error message if user does not populate all fields
        if (titleField.getText().isEmpty() || descField.getText().isEmpty() || locField.getText().isEmpty() || typeField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Insufficient information");
            alert.setContentText("Please fill all information boxes");
            alert.showAndWait();
            return;
        }

        //parse data entered by fields
        int appId = Integer.parseInt(appIdField.getText());
        String titleInput = titleField.getText();
        String descInput = descField.getText();
        String locationInput = locField.getText();
        String typeInput = typeField.getText();

        //get current user and current time using lambdas
        Timestamp todayTime = getCurrTime.get();
        String createBy = getUser.get();

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
            if (!datesSet) {
                return;
            }

            //Populate sql statement
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, titleInput);
            statement.setString(2, descInput);
            statement.setString(3, locationInput);
            statement.setString(4, typeInput);
            statement.setTimestamp(5, startDateTime);
            statement.setTimestamp(6, endDateTime);
            statement.setTimestamp(7, todayTime);
            statement.setString(8, createBy);
            statement.setTimestamp(9, todayTime);
            statement.setString(10, createBy);
            statement.setInt(11, custId);
            statement.setInt(12, userId);
            statement.setInt(13, contactId);

            //execute sql statement
            statement.executeUpdate();
            updated = true;


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
            //Update table with new values depending on filter
            if (allRad.isSelected()) {
                aList.clearAppointmentList();
                aList.updateAppointments();
                appointmentsTable.setItems(aList.getAppointmentList());
            }
            if (thisWeekRad.isSelected()) {
                filterList.clearAppointmentList();
                filterList.filterWeek(dateFilter.getValue());
                appointmentsTable.setItems(filterList.getAppointmentList());
            }
            if(thisMonthRad.isSelected()) {
                filterList.clearAppointmentList();
                filterList.filterMonth(dateFilter.getValue());
                appointmentsTable.setItems(filterList.getAppointmentList());
            }

            //clear fields
            clearFields();
        }
    }

    /**
     * Adds functionality to delete button which deletes selected appointment
     * @param actionEvent
     */
    public void delPress(ActionEvent actionEvent) {
        if (appIdField.getText().isEmpty()) {
            try {
                Appointment selected = (Appointment) appointmentsTable.getSelectionModel().getSelectedItem();

                //get info for success message
                int idDel = selected.getAppId();
                String typeDel = selected.getType();
                String message = "The appointment with ID: " + idDel + " and TYPE: '" + typeDel + "' was successfully deleted.";

                //Attempt to delete
                Connection conn = DBConnection.getConnection();
                String query = "Delete from appointments WHERE appointment_id = " + idDel;

                try {
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                //Update table with new values depending on filter
                if (allRad.isSelected()) {
                    aList.clearAppointmentList();
                    aList.updateAppointments();
                    appointmentsTable.setItems(aList.getAppointmentList());
                }
                if (thisWeekRad.isSelected()) {
                    filterList.clearAppointmentList();
                    filterList.filterWeek(dateFilter.getValue());
                    appointmentsTable.setItems(filterList.getAppointmentList());
                }
                if(thisMonthRad.isSelected()) {
                    filterList.clearAppointmentList();
                    filterList.filterMonth(dateFilter.getValue());
                    appointmentsTable.setItems(filterList.getAppointmentList());
                }

                //success info message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success!");
                alert.setContentText(message);
                alert.showAndWait();

            } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No selection");
                alert.setContentText("Please select Appointment to delete.");
                alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Currently Updating");
            alert.setContentText("Please clear or save currently updating customer before deleting a customer");
            alert.showAndWait();
        }
    }

    /**
     * gives functionality to clear button
     * clears allfields
     * @param actionEvent
     */
    public void clearPress(ActionEvent actionEvent) {
        if (appIdField.getText().isEmpty()) {
            clearFields();
        }
        else {
            aList.addAppointment(updatingApp);
            clearFields();

            //Update table with new values depending on filter
            if (allRad.isSelected()) {
                aList.clearAppointmentList();
                aList.updateAppointments();
                appointmentsTable.setItems(aList.getAppointmentList());
            }
            if (thisWeekRad.isSelected()) {
                filterList.clearAppointmentList();
                filterList.filterWeek(dateFilter.getValue());
                appointmentsTable.setItems(filterList.getAppointmentList());
            }
            if(thisMonthRad.isSelected()) {
                filterList.clearAppointmentList();
                filterList.filterMonth(dateFilter.getValue());
                appointmentsTable.setItems(filterList.getAppointmentList());
            }
        }
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
     * sets prompt text to selected date
     * @param actionEvent
     */
    public void dateBoxSelect(ActionEvent actionEvent) {
        if (boxListening) {
            LocalDate selected = dateBox.getValue();
            dateBox.setPromptText(String.valueOf(dateBox.getValue()));
            try {
                if (selected.compareTo(LocalDate.now()) < 0) {
                    dateBox.setValue(null);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Date");
                    alert.setContentText("Please select future or present date");
                    alert.showAndWait();
                }
            } catch (NullPointerException e) {}
        }
    }

    /**
     * Populate table with all available appointments
     * @param actionEvent
     */
    public void allRadPick(ActionEvent actionEvent) {
        aList.clearAppointmentList();
        aList.updateAppointments();
        appointmentsTable.setItems(aList.getAppointmentList());
    }

    /**
     * Populate table with appointments filtered by week start selected
     * @param actionEvent
     */
    public void weekRadPick(ActionEvent actionEvent) {
        filterList.clearAppointmentList();
        filterList.filterWeek(dateFilter.getValue());
        appointmentsTable.setItems(filterList.getAppointmentList());
    }

    /**
     * Populate table with appointments filtered by month start selected
     * @param actionEvent
     */
    public void monthRadPick(ActionEvent actionEvent) {
        filterList.clearAppointmentList();
        filterList.filterMonth(dateFilter.getValue());
        appointmentsTable.setItems(filterList.getAppointmentList());
    }

    /**
     * enables filter radials if date was selected
     * sets prompt text to selected date
     * @param actionEvent
     */
    public void dateFilterToggle(ActionEvent actionEvent) {
        if (dateFilter.getValue() != null);
        thisWeekRad.setDisable(false);
        thisMonthRad.setDisable(false);
        dateFilter.setPromptText(String.valueOf(dateFilter.getValue()));

        if (thisMonthRad.isSelected()) {
            filterList.clearAppointmentList();
            filterList.filterMonth(dateFilter.getValue());
            appointmentsTable.setItems(filterList.getAppointmentList());
        }
        if (thisWeekRad.isSelected()) {
            filterList.clearAppointmentList();
            filterList.filterWeek(dateFilter.getValue());
            appointmentsTable.setItems(filterList.getAppointmentList());
        }
    }
}
