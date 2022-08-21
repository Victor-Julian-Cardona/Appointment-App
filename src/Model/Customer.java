package Model;

import Util.CustomerList;
import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class Customer {

    private final int cusId;
    private final String name;
    private final String address;
    private final String postalCode;
    private final String phone;
    private final Timestamp createDate;
    private final String createBy;
    private final Timestamp lastUpdate;
    private final String updateBy;
    private final int divId;
    private final String divName;

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
    public Customer(int cusId, String name, String address, String postalCode, String phone, Timestamp createDate, String createBy, Timestamp lastUpdate, String updateBy, int divId, String divName) {
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
    public int getCusId() {
        return cusId;
    }

    /**
     * setter for customer name
     * @return customer name
     */
    public  String getName() {
        return name;
    }

    /**
     * Getter for customer address
     * @return customer address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Getter for customer postal code
     * @return postal code
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Getter for customer phone
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Getter for date the customer was created
     * @return createDate
     */
    public Timestamp getCreateDate() {
        return createDate;
    }

    /**
     * Getter for user that created the customer
     * @return createBy
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * Getter for time the customer was updated
     * @return lastUpdate
     */
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Getter for the last time the customer was updated
     * @return updateBY
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * Getter for the division Id of the customer
     * @return divID
     */
    public int getDivId() {
        return divId;
    }

    /**
     * Getter for divName
     * @return
     */
    public String getDivName() {return divName;}

}


