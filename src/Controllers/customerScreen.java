package Controllers;

import Utility.CustomerList;
import Utility.Function;
import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import Model.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.function.Supplier;

public class customerScreen implements Initializable {
    public TableView cusTable;
    public TableColumn idCol;
    public TableColumn nameCol;
    public TableColumn adressCol;
    public TableColumn codeCol;
    public TableColumn phoneCol;
    public TableColumn stateCol;
    public TextField idfield;
    public TextField nameField;
    public TextField postalField;
    public TextField phoneFIeld;
    public ComboBox countryBox;
    public ComboBox stateBox;
    public Button addButton;
    public Button upButton;
    public Button delButton;
    public TextField addressField;
    public Button clearButton;
    public Button backButton;

    /**
     * Declare CustomerList object to be used throughout the customerScreen class
     */
    private CustomerList clist = new CustomerList();

    /**
     * Declare list of countries for comboBox
     */
    private ObservableList<String> countryList = FXCollections.observableArrayList();

    /**
     * Declare list of states for comboBox
     */
    private ObservableList<String> stateList = FXCollections.observableArrayList();

    /**
     * Declare countrySelection string, to be used throughout the customerScreen class
     */
    private String countrySelection;

    /**
     * Declare countryIdSelection int to be used in customerScreen class
     */
    private int countryIdSelection;

    /**
     * Declare string stateSelection to be used in customerScreen class
     */
    private String stateSelection;

    /**
     * Declare division Id selection to be used in customer screen class
     */
    private int stateIdSelection;

    /**
     * Declare booleans to check if comboBoxes should update
     */
    private boolean countryListen = true, stateListen = true;

    /**
     * Declare boolean that indicates if customer is being updated
     */
    private boolean updatingStatus = false;

    /**
     * Method to populate country combo box
     */
    public void setCountryCombo() {

        Connection conn = DBConnection.getConnection();
        String query = "SELECT country FROM COUNTRIES";
        countryList.clear();

        try {
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String country = rs.getString("country");
                countryList.add(country);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        countryBox.setItems(countryList);
    }

    /**
     * Method to populate state combo box
     */
    public void setStateCombo() {

        stateList.clear();
        Connection conn = DBConnection.getConnection();
        String query = "SELECT Division FROM First_level_divisions where country_id = " + countryIdSelection;

        try {
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String state = rs.getString("Division");
                stateList.add(state);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        stateBox.setItems(stateList);
    }

    /**
     * Method that initializes the comboboxes and the Customer table
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clist.updateCustomers();
        cusTable.setItems(clist.getCustomerList());

        idCol.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        adressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        codeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        stateCol.setCellValueFactory(new PropertyValueFactory<>("divName"));

        setCountryCombo();
    }

    /**
     * adds customer to database
     * prints errors if fields and comboboxes arent populated and selected
     * repopulates table
     * @param actionEvent
     */

    public void addPress(ActionEvent actionEvent) {

        if(idfield.getText().isEmpty()) {
            // declare boolean that indicates successful addition to database
            boolean updated = true;

            //make new sequential id
            int max = 1;
            for (int i = 0; i < clist.getCustomerList().size(); i++) {
                if (max == clist.getCustomerList().get(i).getCusId()) {
                    max++;
                }
            }
            idfield.setText(String.valueOf(max));

            //Error message if user does not populate all fields
            if (nameField.getText().isEmpty() || addressField.getText().isEmpty() || postalField.getText().isEmpty() || phoneFIeld.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insufficient information");
                alert.setContentText("Please fill all information boxes");
                alert.showAndWait();
                updated = false;
                return;
            }

            //parse data entered
            String nameInput = nameField.getText();
            String addressInput = addressField.getText();
            String postalInput = postalField.getText();
            String phoneInput = phoneFIeld.getText();
            Timestamp todayTime = Timestamp.valueOf(LocalDateTime.now());
            String createBy = "admin";
            Timestamp updateTime = Timestamp.valueOf(LocalDateTime.now());
            String updateBy = "admin";

            //Insert data into table
            Connection conn = DBConnection.getConnection();
            String query = "INSERT INTO Customers Values (?,?,?,?,?,?,?,?,?,?)";

            try {
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, max);
                statement.setString(2, nameInput);
                statement.setString(3, addressInput);
                statement.setString(4, postalInput);
                statement.setString(5, phoneInput);
                statement.setTimestamp(6, todayTime);
                statement.setString(7, createBy);
                statement.setTimestamp(8, updateTime);
                statement.setString(9, updateBy);
                statement.setInt(10, stateIdSelection);

                statement.executeUpdate();


            } catch (SQLException throwables) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insufficient information");
                alert.setContentText("Please select country and state/province");
                alert.showAndWait();
                updated = false;
            }

            if (updated) {
                //Update table with new values
                cusTable.getItems().clear();
                clist.updateCustomers();
                cusTable.setItems(clist.getCustomerList());

                clearData();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Currently Updating");
            alert.setContentText("Cannot add customer when currently updating customer. Please press clear button, then proceed to add customer");
            alert.showAndWait();
        }
    }

    /**
     * Method to reset clear all fields and reset comboBoxes
     */
    public void clearData() {
        //Clear fields
        nameField.clear();
        phoneFIeld.clear();
        postalField.clear();
        addressField.clear();
        idfield.clear();

        //Clear comboBoxes
        countryListen = false;
        stateListen = false;
        stateBox.getSelectionModel().clearSelection();
        stateBox.setItems(null);
        countryBox.getSelectionModel().clearSelection();
        countryBox.setItems(null);
        countryListen = true;
        stateListen = true;
        setCountryCombo();
    }

    /**
     * Functionality for updateButton that populates all fields and comboBoxes with information of customer selected for update
     * @param actionEvent
     */
    public void upPress(ActionEvent actionEvent) {

        updatingStatus = true;
        try {
            Customer selected = (Customer) cusTable.getSelectionModel().getSelectedItem();

            //populate fields
            idfield.setText(String.valueOf(selected.getCusId()));
            nameField.setText(selected.getName());
            addressField.setText(selected.getAddress());
            postalField.setText(selected.getPostalCode());
            phoneFIeld.setText(selected.getPhone());

            //set comboBox selection
            try {
                String country = Function.getCountry(selected.getDivId());
                for (int i = 0; i < countryList.size(); i++) {
                    if (country.equals(countryList.get(i))) {
                        countryBox.getSelectionModel().select(i);
                    }
                }
                for (int i = 0; i < stateList.size(); i++) {
                    if (selected.getDivName().equals(stateList.get(i))) {
                        stateBox.getSelectionModel().select(i);
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No selection");
            alert.setContentText("Please select Customer to update.");
            alert.showAndWait();
        }

    }

    /**
     * Functionality for save button that updates customer with updated information from fields
     * @param actionEvent
     */
    public void savePress(ActionEvent actionEvent) {
        if (updatingStatus) {
            // declare boolean that indicates successful addition to database
            boolean updated = true;

            //Error message if user does not populate all fields
            if (nameField.getText().isEmpty() || addressField.getText().isEmpty() || postalField.getText().isEmpty() || phoneFIeld.getText().isEmpty() || stateBox.getValue() == null || countryBox.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insufficient information");
                alert.setContentText("Please fill all information fields. Please select country and state/province");
                alert.showAndWait();
                updated = false;
                return;
            }

            //parse data entered
            int cusId = Integer.parseInt(idfield.getText());
            String nameInput = nameField.getText();
            String addressInput = addressField.getText();
            String postalInput = postalField.getText();
            String phoneInput = phoneFIeld.getText();
            Timestamp updateTime = Timestamp.valueOf(LocalDateTime.now());
            String updateBy = "admin";

            //Insert data into table
            Connection conn = DBConnection.getConnection();
            String query = "UPDATE Customers SET customer_name = ?, address = ?, postal_code = ?, phone = ?, last_update = ?, last_updated_by = ?, division_id = ? WHERE customer_id = ?";

            try {
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, nameInput);
                statement.setString(2, addressInput);
                statement.setString(3, postalInput);
                statement.setString(4, phoneInput);
                statement.setTimestamp(5, updateTime);
                statement.setString(6, updateBy);
                statement.setInt(7, stateIdSelection);
                statement.setInt(8, cusId);

                statement.executeUpdate();


            } catch (SQLException throwables) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insufficient information");
                alert.setContentText("Please select country and state/province");
                alert.showAndWait();
                updated = false;
            }

            if (updated) {
                //Update table with new values
                cusTable.getItems().clear();
                clist.updateCustomers();
                cusTable.setItems(clist.getCustomerList());
                updatingStatus = false;
                clearData();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Not updating");
            alert.setContentText("Not currently updating Customer. To update: select customer, press update button, then save when updating is complete");
            alert.showAndWait();
            return;
        }
    }

    /**
     * functionality for cancel button that clears all fields
     * @param actionEvent
     */
    public void cancelPress(ActionEvent actionEvent) {
        clearData();
        updatingStatus = false;
    }

    /**
     * populates the state combobox
     * @param actionEvent
     */
    public void countryCombo(ActionEvent actionEvent) {

        if(countryListen) {
            countrySelection = String.valueOf(countryBox.getValue());

            Connection conn = DBConnection.getConnection();
            String query = "SELECT Country_id FROM countries WHERE country = '" + countrySelection + "'";

            try {
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet rs = statement.executeQuery();
                rs.next();
                countryIdSelection = rs.getInt("Country_Id");

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            setStateCombo();
        }
    }

    /**
     * selects divId of selected part and sets it to stateIdSelection variable
     * @param actionEvent
     */
    public void stateCombo(ActionEvent actionEvent) {

        if(stateListen) {
            stateSelection = String.valueOf(stateBox.getValue());

            Connection conn = DBConnection.getConnection();
            String query = "SELECT Division_id FROM first_level_divisions WHERE division = '" + stateSelection + "'";

            try {
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet rs = statement.executeQuery();
                rs.next();
                stateIdSelection = rs.getInt("Division_Id");

            } catch (SQLException throwables) {
            }
        }
    }

    /**
     * deletes selected Customer or prints error message if none selected
     * confirm message to confirm deletion
     * @param actionEvent
     */
    public void delPress(ActionEvent actionEvent) {

        try {
            Customer selected = (Customer) cusTable.getSelectionModel().getSelectedItem();
            int idDel = selected.getCusId();

            Connection conn = DBConnection.getConnection();
            String query1 = "Delete from appointments WHERE customer_id = " + idDel;
            String query2 = "Delete from customers WHERE customer_id = " + idDel;

            try {
                PreparedStatement statement = conn.prepareStatement(query1);
                statement.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                PreparedStatement statement = conn.prepareStatement(query2);
                statement.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            cusTable.getItems().clear();
            clist.updateCustomers();
            cusTable.setItems(clist.getCustomerList());
            clearData();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success!");
            alert.setContentText("Customer has been deleted from database");
            alert.showAndWait();

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No selection");
            alert.setContentText("Please select Customer to delete.");
            alert.showAndWait();
        }
    }

    /**
     * functionality for clear button that clears all fields
     * @param actionEvent
     */
    public void clearPress(ActionEvent actionEvent) {
        clearData();
        updatingStatus = false;
    }

    /**
     * adds functionality to back button which returns user to main screen
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
}
