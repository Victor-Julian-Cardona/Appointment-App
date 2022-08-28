package Model;

import Utility.Converters;
import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDate;

/**
 * AppointmentList class which holds an observable list of appointments
 * includes methods to populate, filter, and modify the list of appointments
 */
public class AppointmentList {

    /**
     * Declare observable list of all appointments currently in the database
     */
    private ObservableList<Appointment> aList = FXCollections.observableArrayList();

    /**
     * method to update and populate the list of all appointments in the database
     */
    public void updateAppointments() {

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
                Timestamp start = Converters.UTCToLocal(rs.getTimestamp("start"));
                Timestamp end = Converters.UTCToLocal(rs.getTimestamp("end"));
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
    public ObservableList<Appointment> getAppointmentList() {
        return aList;
    }

    /**
     * Method to clear appointment list
     */
    public void clearAppointmentList() {aList.clear();}

    /**
     * Method to remove selected appointment from list
     * @param selected
     */
    public void removeAppointment(Appointment selected) {
        aList.remove(selected);
    }

    /**
     * Method to add an appointment to the list
     * @param newApp
     */
    public void addAppointment(Appointment newApp) {
        aList.remove(newApp);
    }

    /**
     * Filter observable list by selected week start date
     * @param filterStart
     */
    public void filterWeek(LocalDate filterStart) {
        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT * FROM Appointments WHERE start BETWEEN '" + filterStart + "' and '" + filterStart.plusDays(7) + "'";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                int appId = rs.getInt("appointment_id");
                String title = rs.getString("title");
                String desc = rs.getString("description");
                String location = rs.getString("location");
                String type = rs.getString("type");
                Timestamp start = Converters.UTCToLocal(rs.getTimestamp("start"));
                Timestamp end = Converters.UTCToLocal(rs.getTimestamp("end"));
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
     * Filter observable list by selected month start date
     * @param filterStart
     */
    public void filterMonth(LocalDate filterStart) {
        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT * FROM Appointments WHERE start BETWEEN '" + filterStart + "' and '" + filterStart.plusMonths(1) + "'";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                int appId = rs.getInt("appointment_id");
                String title = rs.getString("title");
                String desc = rs.getString("description");
                String location = rs.getString("location");
                String type = rs.getString("type");
                Timestamp start = Converters.UTCToLocal(rs.getTimestamp("start"));
                Timestamp end = Converters.UTCToLocal(rs.getTimestamp("end"));
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
     * Populate appointment list with appointments that correspond to a particular customer
     * @param customer
     * @throws SQLException
     */
    public void filterCustId(String customer) throws SQLException {

        int customerId = Converters.getCustomerId(customer);

        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT * FROM appointments WHERE customer_id = '" + customerId + "'";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                int appId = rs.getInt("appointment_id");
                String title = rs.getString("title");
                String desc = rs.getString("description");
                String location = rs.getString("location");
                String type = rs.getString("type");
                Timestamp start = Converters.UTCToLocal(rs.getTimestamp("start"));
                Timestamp end = Converters.UTCToLocal(rs.getTimestamp("end"));
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
     * Populate appointment list with appointments that correspond to a particular contact
     * @param contact
     * @throws SQLException
     */
    public void filterContId(String contact) throws SQLException {

        int contactId = Converters.getContactId(contact);

        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT * FROM appointments WHERE contact_id = '" + contactId + "'";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                int appId = rs.getInt("appointment_id");
                String title = rs.getString("title");
                String desc = rs.getString("description");
                String location = rs.getString("location");
                String type = rs.getString("type");
                Timestamp start = Converters.UTCToLocal(rs.getTimestamp("start"));
                Timestamp end = Converters.UTCToLocal(rs.getTimestamp("end"));
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
}
