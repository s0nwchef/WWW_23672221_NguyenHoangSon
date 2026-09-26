package fit.se.democart.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class DBUtil {

    private DataSource dataSource;

    public DBUtil(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() {
        Connection conn;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }
}
