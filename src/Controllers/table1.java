package Controllers;

import DBAccess.DBCountries;
import Model.Countries;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class table1 implements Initializable {


    public TableView tablelol;
    public TableColumn col1;
    public TableColumn col2;
    public Button butt;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    public void buttpress(ActionEvent actionEvent) {

        ObservableList<Countries> countrylist = DBCountries.getAllCountries();
        for(Countries C :countrylist) {
            System.out.println("Country Id: " + C.getId() + " Name " + C.getName());
        }

    }
}
