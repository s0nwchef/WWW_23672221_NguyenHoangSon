package iuh.fit.crud.util;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaInitializer {
    public static void initSchema(DataSource dataSource) {
        String sql = "CREATE TABLE IF NOT EXISTS products (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL," +
                "price DOUBLE NOT NULL," +
                "description VARCHAR(1000))";
        try (Connection con = dataSource.getConnection();
             Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Không thể tự tạo bảng products: " + e.getMessage(), e);
        }
    }
}