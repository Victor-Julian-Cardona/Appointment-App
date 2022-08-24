package Utility;

import database.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;

public abstract class Converters {

    public static int getUserId(String userName) throws SQLException {
        String query = "SELECT User_ID FROM USERS WHERE User_Name = '" + userName + "'";
        Connection conn = DBConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        rs.next();
        int userId = rs.getInt("USER_ID");
        return userId;
    }

    public static int getContactId(String contact) throws SQLException {
        String query = "SELECT contact_id FROM contacts WHERE Contact_Name = '" + contact + "'";
        Connection conn = DBConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        rs.next();
        int contactId = rs.getInt("contact_id");
        return contactId;
    }

    public static String getCountry(int divId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "SELECT Country_id FROM first_level_divisions WHERE division_id = " + divId;

        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        rs.next();
        int countryId = rs.getInt("country_id");

        String query2 = "SELECT country FROM Countries WHERE country_id = "+ countryId;
        PreparedStatement statement2 = conn.prepareStatement(query2);
        ResultSet rs2 = statement2.executeQuery();
        rs2.next();
        String country = rs2.getString("country");
        return country;
    }

    public static int getCustomerId(String customer) throws SQLException {
        String query = "SELECT customer_id FROM customers WHERE customer_name = '" + customer + "'";
        Connection conn = DBConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        rs.next();
        int customerId = rs.getInt("customer_id");
        return customerId;
    }

    public static String getCustomer(int customerId) throws SQLException {
        String query = "SELECT customer_name FROM customers WHERE customer_id = "+customerId;
        Connection conn = DBConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        rs.next();
        String customer = rs.getString("customer_name");
        return customer;
    }

    public static String getContact(int contactId) throws SQLException {
        String query = "SELECT contact_name FROM contacts WHERE contact_id = "+contactId;
        Connection conn = DBConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        rs.next();
        String contact = rs.getString("contact_name");
        return contact;
    }
}
