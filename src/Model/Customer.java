package Model;

import Util.CustomerList;
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
    private static String divName;

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
    public Customer(int cusId, String name, String address, String postalCode, String phone, Date createDate, String createBy, Timestamp lastUpdate, String updateBy, int divId, String divName) {
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
        this.divName = divName;
    }

    /**
     * default constructor for Customer object
     */
    public Customer() {
        this.cusId = 0;
        this.name = null;
        this.address = null;
        this.postalCode = null;
        this.phone = null;
        this.createDate = null;
        this.createBy = null;
        this.lastUpdate = null;
        this.updateBy = null;
        this.divId = 0;
        this.divName = null;
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

    /**
     * Getter for divName
     * @return
     */
    public static String getDivName() {return divName;}

}


