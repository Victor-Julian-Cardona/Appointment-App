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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * InformationScreen class which controls the informationScreen view
 */
public class InformationScreen implements Initializable {
    public ComboBox custBox;
    public ComboBox contBox;
    public Button backButton;
    public TableView appointmentsTable;
    public TableColumn appIdCol;
    public TableColumn titleCol;
    public TableColumn descCol;
    public TableColumn locationCol;
    public TableColumn typeCol;
    public TableColumn startCol;
    public TableColumn endCol;
    public TableColumn custIdCol;
    public ComboBox typeCombo;
    public ComboBox monthCombo;
    public TextField typeField;
    public TextField monthField;
    public TableColumn contIdCol;

    /**
     * Declare observable list of all customers
     */
    private ObservableList<String> customerList = FXCollections.observableArrayList();

    /**
     * Declare observable list of all contacts
     */
    private ObservableList<String> contactList = FXCollections.observableArrayList();

    /**
     * Declare observable list of all distinct appointment types
     */
    private ObservableList<String> typeList = FXCollections.observableArrayList();

    /**
     * Declare object aList, which holds a list of all appointments to be shown in table
     */
    private AppointmentList aList = new AppointmentList();

    /**
     * Declare object bList, to be used for counting purposes
     */
    private AppointmentList bList = new AppointmentList();

    /**
     * Declare observable list monthList to hold all months
     */
    private ObservableList<Integer> monthList = FXCollections.observableArrayList();

    /**
     * Declare boolean that checks if comboBoxes are listening
     */
    private boolean boxListening = true;

    /**
     * Sets the customer list to be passed to be set in the comboBox "custBox"
     * @throws SQLException
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
        custBox.setItems(customerList);
    }

    /**
     * Sets the contact list to be passed to be set in the comboBox "contBox"
     * @throws SQLException
     */
    public void setContactList() throws SQLException {
        String query = "SELECT Contact_name FROM contacts ORDER BY contact_name";;
        Connection conn = DBConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            String contact = rs.getString("contact_name");
            contactList.add(contact);
        }
        contBox.setItems(contactList);
    }

    /**
     * Method to set typeComboBox with observable list of all available appointment types
     * Uses stream.filter to remove duplicates
     * LAMBDA: Predicate used for stream filter
     * @throws SQLException
     */
    public void setTypeList() throws SQLException {
        //populate contactList with all types
        String query = "SELECT type FROM appointments ORDER BY type";;
        Connection conn = DBConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            String type = rs.getString("type");
            typeList.add(type);
        }
        //populate filteredTypeList with all types without duplicates
        Set<String> filteredTypeList= new HashSet<String>();
        Stream<String> myStream = typeList.stream();
        List newTypeList = myStream.filter(t -> filteredTypeList.add(t)).collect(Collectors.toList());

        //populate contactList with all types without duplicates
        typeList.clear();
        for (int i = 0; i < newTypeList.size(); i++) {
            typeList.add(String.valueOf(newTypeList.get(i)));
        }
        //populate combo with contactList without duplicates
        typeCombo.setItems(typeList);
    }

    /**
     * Method to set month comboBox with all months
     */
    public void setMonthList() {
        for (int i = 1; i <= 12; i++) {
            monthList.add(i);
        }
        monthCombo.setItems(monthList);
    }

    /**
     * Initializes the ContactScreen view
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //set customer comboBox, type comboBox, contact comboBox
        try {
            setCustomerList();
            setContactList();
            setTypeList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //set month comboBox
        setMonthList();
    }

    /**
     * Populates the table with all appointments associated with the selected contact
     * resets customer comboBox
     * @param actionEvent
     * @throws SQLException
     */
    public void contBoxSelect(ActionEvent actionEvent) throws SQLException {

        if(boxListening) {
            aList.clearAppointmentList();
            aList.filterContId(String.valueOf(contBox.getValue()));
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
            contIdCol.setCellValueFactory(new PropertyValueFactory<>("contId"));

            boxListening = false;
            custBox.getSelectionModel().clearSelection();
            boxListening = true;
        }
    }

    /**
     * Populates the table with all appointments associated with the selected customer
     * resets the contact comboBox
     * @param actionEvent
     * @throws SQLException
     */
    public void custBoxSelect(ActionEvent actionEvent) throws SQLException {

        if (boxListening) {
            aList.clearAppointmentList();
            aList.filterCustId(String.valueOf(custBox.getValue()));
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

            boxListening = false;
            contBox.getSelectionModel().clearSelection();
            boxListening = true;
        }
    }

    /**
     * Gives functionality to back button which returns user to the mainScreen view
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
     * Method that gets selection from type comboBox and populates the type Field with the amount of appointments of that type
     * @param actionEvent
     * @throws SQLException
     */
    public void typeComboSelect(ActionEvent actionEvent) throws SQLException {

        int typeCount = 0;
        String appType = String.valueOf(typeCombo.getSelectionModel().getSelectedItem());

        String query = "SELECT appointment_id FROM appointments WHERE type = '" + appType + "'";;
        Connection conn = DBConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            typeCount++;
        }
        typeField.setText(String.valueOf(typeCount));
    }

    /**
     * Gets the amount of appointments in the selected month and populates month field with that value
     * LAMBDA: predicate used for stream filter
     * @param actionEvent
     */
    public void monthComboSelect(ActionEvent actionEvent) {
        bList.clearAppointmentList();
        bList.updateAppointments();
        int monthType = Integer.parseInt(String.valueOf(monthCombo.getSelectionModel().getSelectedItem()));
        Stream<Appointment> bListStream = bList.getAppointmentList().stream();
        long monthCount = bListStream.filter(m -> (m.getStart().toLocalDateTime().getMonth().getValue() == monthType)).count();
        monthField.setText(String.valueOf(monthCount));
    }
}
