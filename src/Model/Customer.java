package Model;

import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class Customer {

    private static int cusId;
    private static String name;
    private static String address;
    private static String postalCode;
    private static String phone;
    private static Date createDate;
    private static String createBy;
    private static Timestamp lastUpdate;
    private static String updateBy;
    private static int divId;

    /**
     * Constructor for Customer class
     * @param cusId
     * @param name
     * @param address
     * @param postalCode
     * @param phone
     * @param createDate
     * @param createBy
     * @param lastUpdate
     * @param updateBy
     * @param divId
     */
    public Customer(int cusId, String name, String address, String postalCode, String phone, Date createDate, String createBy, Timestamp lastUpdate, String updateBy, int divId) {
        this.cusId = cusId;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDate = createDate;
        this.createBy = createBy;
        this.lastUpdate = lastUpdate;
        this.updateBy = updateBy;
        this.divId = divId;
    }

    /**
     * declare observable list of all customers currently in the database
     */
    private static ObservableList<Customer> customerList = FXCollections.observableArrayList();

    /**
     * Method to parse customer table and add all rows to an observable list
     */
    public static ObservableList getCurrentCustomers() {

        String query = "SELECT * FROM customers";
        Connection conn = DBConnection.getConnection();
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int custID =rs.getInt("customer_id");
                String name = rs.getString("customer_name");
                String addresss = rs.getString("address");
                String postalCode = rs.getString("Postal_code");
                String phone  = rs.getString("phone");
                Date createDate = rs.getDate("Create_Date");
                String createBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String updateBy = rs.getString("Last_Updated_By");
                int divId = rs.getInt("Division_ID");
                Customer C = new Customer(custID, name, addresss, postalCode, phone, createDate, createBy, lastUpdate, updateBy, divId);
                customerList.add(C);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customerList;
    }

    /**
     * getter for customer id
     * @return customer id
     */
    public static int getCusId() {
        return cusId;
    }

    /**
     * setter for customer name
     * @return customer name
     */
    public static String getName() {
        return name;
    }

    /**
     * Getter for customer address
     * @return customer address
     */
    public static String getAddress() {
        return address;
    }

    /**
     * Getter for customer postal code
     * @return postal code
     */
    public static String getPostalCode() {
        return postalCode;
    }

    /**
     * Getter for customer phone
     * @return phone
     */
    public static String getPhone() {
        return phone;
    }

    /**
     * Getter for date the customer was created
     * @return createDate
     */
    public static Date getCreateDate() {
        return createDate;
    }

    /**
     * Getter for user that created the customer
     * @return createBy
     */
    public static String getCreateBy() {
        return createBy;
    }

    /**
     * Getter for time the customer was updated
     * @return lastUpdate
     */
    public static Timestamp getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Getter for the last time the customer was updated
     * @return updateBY
     */
    public static String getUpdateBy() {
        return updateBy;
    }

    /**
     * Getter for the division Id of the customer
     * @return divID
     */
    public static int getDivId() {
        return divId;
    }
}
