package Utility;

import Model.Appointment;
import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class AppointmentList {

    /**
     * Declare observable list of all appointments currently in the database
     */
    private static ObservableList<Appointment> aList = FXCollections.observableArrayList();

    /**
     * method to update and populate the list of all appointments in the database
     */
    public static void updateAppointments() {

        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM Appointments";

        try {
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                int appId = rs.getInt("appointment_id");
                String title = rs.getString("title");
                String desc = rs.getString("description");
                String location = rs.getString("location");
                String type = rs.getString("type");
                Timestamp start = rs.getTimestamp("start");
                Timestamp end = rs.getTimestamp("end");
                Timestamp createDate = rs.getTimestamp("create_date");
                String createBy = rs.getString("created_by");
                Timestamp lastUpdate = rs.getTimestamp("last_update");
                String updateBy = rs.getString("Last_Updated_By");
                int custId = rs.getInt("customer_ID");
                int userId = rs.getInt("user_ID");
                int contId = rs.getInt("contact_ID");

                Appointment A = new Appointment(appId, title, desc, location, type, start, end, createDate, createBy, lastUpdate, updateBy, custId, userId, contId);
                aList.add(A);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Method to get appointment list
     * @return
     */
    public static ObservableList<Appointment> getAppointmentList() {
        return aList;
    }

    /**
     * Method to clear appointment list
     */
    public static void clearAppointmentList() {aList.clear();}

}
