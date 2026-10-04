package org.example.bai6_REST.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL =
            "jdbc:mariadb://localhost:3306/quanlytintuc";

    private static final String USER = "root";
    private static final String PASSWORD = "sapassword";

    public static Connection getConnection() throws SQLException {

        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Khong tim thay MariaDB Driver", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
