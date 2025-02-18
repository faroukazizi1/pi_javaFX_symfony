package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {

    private String url = "jdbc:mysql://localhost:3306/finance";
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
        return conn;
    }

    private DBconnection() {
        try {
            this.conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection established");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
