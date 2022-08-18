package Util;

import database.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Functions {

    public int getCustomerId(String userName) throws SQLException {
        String query = "SELECT User_ID FROM USERS WHERE User_Name = '" + userName + "'";
        Connection conn = DBConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        rs.next();
        int userId = rs.getInt("USER_ID");
        return userId;
    }

}
