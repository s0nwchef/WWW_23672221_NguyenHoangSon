package iuh.fit.crud.util;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
public class DBUtil {
    private final DataSource dataSources;

    public DBUtil(DataSource dataSource) {
        this.dataSources = dataSource;
    }

    public Connection getConnection() throws SQLException {
        Connection conn = dataSources.getConnection();
        return conn;
    }
}
