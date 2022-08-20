package Util;

import Model.Customer;
import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CustomerList {

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
            int i = 0;

            while (rs.next()) {

                int custID = rs.getInt("customer_id");
                String name = rs.getString("customer_name");
                String addresss = rs.getString("address");
                String postalCode = rs.getString("Postal_code");
                String phone  = rs.getString("phone");
                Date createDate = rs.getDate("Create_Date");
                String createBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String updateBy = rs.getString("Last_Updated_By");
                int divId = rs.getInt("Division_ID");
                String divName = getDivName(divId);

                Customer C  = new Customer(custID, name, addresss, postalCode, phone, createDate, createBy, lastUpdate, updateBy, divId, divName);
                //System.out.println(C.getPhone());
                customerList.add(i, C);
                i++;

                System.out.println("boom");
                for (int j = 0; j < customerList.size(); j++) {
                    System.out.println(customerList.get(j).getPhone());
                }

                System.out.println(i);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

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

    public static ObservableList<Customer> getCustomerList() {
        return customerList;
    }
    public static void addCustomer(Customer C) {
        customerList.add(C);
    }
    public static void clearCustomerList() {
        customerList.clear();
    }
    public static void printList() {
        System.out.println("boom");
        for (int i = 0; i <3; i++) {
            System.out.println(customerList.get(i).getPhone());
        }
    }
}

