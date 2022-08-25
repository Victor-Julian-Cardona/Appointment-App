package Utility;

import database.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public abstract class Converters {

    /**
     * Utility method that takes userId and returns User name
     * @param userName
     * @return user name
     * @throws SQLException
     */
    public static int getUserId(String userName) throws SQLException {
        String query = "SELECT User_ID FROM USERS WHERE User_Name = '" + userName + "'";
        Connection conn = DBConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        rs.next();
        int userId = rs.getInt("USER_ID");
        return userId;
    }

    /**
     * Utility method that takes contact id and returns contact name
     * @param contact
     * @return contact name
     * @throws SQLException
     */
    public static int getContactId(String contact) throws SQLException {
        String query = "SELECT contact_id FROM contacts WHERE Contact_Name = '" + contact + "'";
        Connection conn = DBConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        rs.next();
        int contactId = rs.getInt("contact_id");
        return contactId;
    }

    /**
     * Utility method that takes division id and returns country id
     * @param divId
     * @return Conutry Id
     * @throws SQLException
     */
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

    /**
     * Utility method that takes customer name and returns customer id
     * @param customer
     * @return customer id
     * @throws SQLException
     */
    public static int getCustomerId(String customer) throws SQLException {
        String query = "SELECT customer_id FROM customers WHERE customer_name = '" + customer + "'";
        Connection conn = DBConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        rs.next();
        int customerId = rs.getInt("customer_id");
        return customerId;
    }

    /**
     * Utility method that takes customer id and returns customer name
     * @param customerId
     * @return customer name
     * @throws SQLException
     */
    public static String getCustomer(int customerId) throws SQLException {
        String query = "SELECT customer_name FROM customers WHERE customer_id = "+customerId;
        Connection conn = DBConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        rs.next();
        String customer = rs.getString("customer_name");
        return customer;
    }

    /**
     * Utility method that takes contact id and returns contact name
     * @param contactId
     * @return contact name
     * @throws SQLException
     */
    public static String getContact(int contactId) throws SQLException {
        String query = "SELECT contact_name FROM contacts WHERE contact_id = "+contactId;
        Connection conn = DBConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        rs.next();
        String contact = rs.getString("contact_name");
        return contact;
    }

    static public Timestamp localToUTC(Timestamp localTime) {
        long offset = TimeZone.getDefault().getOffset(Timestamp.valueOf(LocalDateTime.now()).getTime());
        long time = localTime.getTime();
        Timestamp converted = localTime;
        converted.setTime(time - offset);
        return converted;
    }

    static public Timestamp UTCToLocal(Timestamp localTime) {
        long offset = TimeZone.getDefault().getOffset(localTime.getTime());
        long time = localTime.getTime();
        Timestamp converted = localTime;
        converted.setTime(time + offset);
        return converted;
    }

    /**
     * Method to convert local time to EST
     * converts to UTC first, then converts to EST
     * @param localTime
     * @return
     */
    static public Timestamp localToEST(Timestamp localTime) {
        long localOffset = TimeZone.getDefault().getOffset(Timestamp.valueOf(LocalDateTime.now()).getTime());
        long time = localTime.getTime();
        Timestamp converted = localTime;
        converted.setTime(time - localOffset);
        TimeZone estZone = TimeZone.getTimeZone("EST");
        long utcTime = converted.getTime();
        long ESTOffset = estZone.getOffset(converted.getTime());
        converted.setTime(utcTime + ESTOffset);
        return converted;
    }

    /**
     * Converts EST to local time
     * First converts to UTC then to local
     * @param EST
     * @return
     */
    static public Timestamp ESTToLocal(Timestamp EST) {
        TimeZone estZone = TimeZone.getTimeZone("EST");
        long estOffset = estZone.getOffset(EST.getTime());
        long estTime = EST.getTime();
        Timestamp converted = EST;
        converted.setTime(estTime - estOffset);
        long utcTime = converted.getTime();
        long localOffset = TimeZone.getDefault().getOffset(Timestamp.valueOf(LocalDateTime.now()).getTime());
        converted.setTime(utcTime + localOffset);
        return converted;
    }

    /**
     * Converts EST to local time
     * @param EST
     * @return
     */
    public static Timestamp estToUTC(Timestamp EST) {
        TimeZone estZone = TimeZone.getTimeZone("EST");
        long estOffset = estZone.getOffset(EST.getTime());
        long estTime = EST.getTime();
        Timestamp converted = EST;
        converted.setTime(estTime - estOffset);
        return converted;
    }

    /**
     * Method to convert UTC to EST
     * @param UTC
     * @return
     */
    static public Timestamp utcToEST(Timestamp UTC) {
        long utcTime = UTC.getTime();
        TimeZone estZone = TimeZone.getTimeZone("EST");
        long ESTOffset = estZone.getOffset(utcTime);
        Timestamp converted = UTC;
        converted.setTime(utcTime + ESTOffset);
        return converted;
    }
}
