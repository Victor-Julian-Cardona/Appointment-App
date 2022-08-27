package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
    }

    public void contBoxSelect(ActionEvent actionEvent) {
    }

    public void custBoxSelect(ActionEvent actionEvent) {
    }

    public void backPress(ActionEvent actionEvent) {
    }

    public void typeComboSelect(ActionEvent actionEvent) {
    }

    public void monthComboSelect(ActionEvent actionEvent) {
    }

    public void yearSelectFieldFilled(ActionEvent actionEvent) {
    }
}
