package Controllers;

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
import java.util.ResourceBundle;
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
    public TextField yearSelectField;
    public TextField typeField;
    public TextField monthField;
    public TextField yearField;
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
     * Declare object AppointmentList, which holds a list of all appointments
     */
    private AppointmentList aList = new AppointmentList();

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

    public void setTypeList() throws SQLException {
        String query = "SELECT type FROM appointments ORDER BY type";;
        Connection conn = DBConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            String type = rs.getString("type");
            contactList.add(type);
        }
        /*
        Stream<String> myStream = contactList
        return list.stream()
                .filter(n -> !items.add(n)) // Set.add() returns false if the element was already in the set.
                .collect(Collectors.toSet());

         */
    }

    /**
     * Initializes the ContactScreen view
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set customer comboBox
        try {
            setCustomerList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //set contact comboBox
        try {
            setContactList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
            contIdCol.setCellValueFactory(new PropertyValueFactory<>("contId"));
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

    public void typeComboSelect(ActionEvent actionEvent) {
    }

    public void monthComboSelect(ActionEvent actionEvent) {
    }

    public void yearSelectFieldFilled(ActionEvent actionEvent) {
    }
}
