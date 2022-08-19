package Controllers;

import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        cusTable.setItems(Customer.getCurrentCustomers());

        idCol.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        adressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        codeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        stateCol.setCellValueFactory(new PropertyValueFactory<>("divId"));
    }
}
