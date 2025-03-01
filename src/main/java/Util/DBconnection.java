package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {

    private String url = "jdbc:mysql://localhost:3306/gestion_conges";
    private String user = "root";
    private String password = "";
    private Connection conn;
    private static DBconnection instance;

    public static DBconnection getInstance() {
        if (instance == null) {
            instance = new DBconnection();
        }
        return instance;
    }

    public Connection getConn() {
        try {
            if (conn == null || conn.isClosed()) {
                this.conn = DriverManager.getConnection(url, user, password);
                System.out.println("Connection re-established");
            }
        } catch (SQLException e) {
            System.out.println("Failed to reconnect: " + e.getMessage());
        }
        return conn;
    }

    private DBconnection() {
        try {
            this.conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection established");
        } catch (SQLException e) {
            System.out.println("Error during connection: " + e.getMessage());
        }
    }
    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Connection closed");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
