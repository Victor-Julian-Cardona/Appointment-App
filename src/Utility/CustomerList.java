package Utility;

import Model.Customer;
import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CustomerList {

    /**
     * List to be presented in Customer screen table
     */
    private static ObservableList<Customer> customerList = FXCollections.observableArrayList();

    /**
     * Method to parse customer table and add all rows to the observable list
     */
    public static void updateCustomers() {

        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM Customers";

        try {
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                int custID = rs.getInt("customer_id");
                String name = rs.getString("customer_name");
                String address = rs.getString("address");
                String postalCode = rs.getString("Postal_code");
                String phone = rs.getString("phone");
                Timestamp createDate = rs.getTimestamp("Create_Date");
                String createBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String updateBy = rs.getString("Last_Updated_By");
                int divId = rs.getInt("Division_ID");
                String divName = getDivName(divId);

                Customer C = new Customer(custID, name, address, postalCode, phone, createDate, createBy, lastUpdate, updateBy, divId, divName);
                customerList.add(C);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Method that receives division id of a Customer object and returns the divisions name
     * @param divId
     * @return division name
     */
    public static String getDivName(int divId) {

        Connection conn = DBConnection.getConnection();
        String query = "SELECT Division FROM first_level_divisions WHERE Division_ID=" + divId;
        PreparedStatement statement = null;
        String stateName = null;
        try {
            statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            rs.next();
            stateName = rs.getString("Division");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return stateName;
    }

    /**
     * getter for observable Customer list
     * @return
     */
    public static ObservableList<Customer> getCustomerList() {
        return customerList;
    }

    /**
     * method to clear observable customer list
     */
    public static void clearCustomerList() {
        customerList.clear();
    }

}

