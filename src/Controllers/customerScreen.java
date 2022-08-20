package Controllers;

import Util.CustomerList;
import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import Model.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        CustomerList clist = new CustomerList();
        clist.updateCustomers();

        cusTable.setItems(clist.getCustomerList());

        idCol.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        adressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        codeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        stateCol.setCellValueFactory(new PropertyValueFactory<>("divName"));
    }

    public void addPress(ActionEvent actionEvent) {
    }

    public void upPress(ActionEvent actionEvent) {
    }

    public void savePress(ActionEvent actionEvent) {
    }

    public void cancelPress(ActionEvent actionEvent) {
    }
}
