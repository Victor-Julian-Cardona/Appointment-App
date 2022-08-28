package database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Class that includes all methods relating to the connection to the database to be used in the program
 */
public class DBConnection {

    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//localhost:3306/";
    private static final String dbName = "client_schedule";

    private static final String jdbcURL = protocol + vendorName + ipAddress + dbName;

    private static final String MYSQLJBCDriver = "com.mysql.jdbc.Driver";

    private static final String username = "sqlUser";
    private static final String password = "Passw0rd!";
    private static Connection conn = null;

    /**
     * Method to start the connection to the database
     * @return
     */
    public static Connection startConnection() {
        try {
            Class.forName(MYSQLJBCDriver);
            conn = DriverManager.getConnection(jdbcURL, username, password);

            System.out.println("Connection successful");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * Method that returns the connection established by startConnection
     * @return
     */
    public static Connection getConnection() {
        return conn;
    }

    /**
     * Method that closes the connection established by startConnection
     */
    public static void closeConnection() {
        try {
            conn.close();
        }
        catch (Exception e){
        }
    }
}
