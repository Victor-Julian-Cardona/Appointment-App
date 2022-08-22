package Util;

import database.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Function {

    public static int getUserId(String userName) throws SQLException {
        String query = "SELECT User_ID FROM USERS WHERE User_Name = '" + userName + "'";
        Connection conn = DBConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        rs.next();
        int userId = rs.getInt("USER_ID");
        return userId;
    }

    public static String getUserName(int userID) throws SQLException {
        String query = "SELECT User_ID FROM USERS WHERE User_Id = '" + userID + "'";
        Connection conn = DBConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        rs.next();
        String userName = rs.getString("USER_Name");
        return userName;
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
}
