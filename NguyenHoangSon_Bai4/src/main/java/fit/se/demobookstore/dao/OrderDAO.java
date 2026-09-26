package fit.se.demobookstore.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import fit.se.demobookstore.beans.CartItemBean;
import fit.se.demobookstore.util.DBUtil;

public class OrderDAO {
    private DBUtil dbUtil;

    public OrderDAO(DataSource dataSource) {
        dbUtil = new DBUtil(dataSource);
    }

    public int saveOrder(String fullname, String address, double totalPrice,
                          String paymentMethod, List<CartItemBean> items) throws Exception {
        String sqlOrder = "INSERT INTO orders (fullname, shipping_address, total_price, payment_method) "
                + "VALUES (?, ?, ?, ?)";
        String sqlDetail = "INSERT INTO order_details (order_id, book_id, quantity, price) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conn = dbUtil.getConnection()) {
            conn.setAutoCommit(false);
            int orderId;
            try (PreparedStatement ps = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, fullname);
                ps.setString(2, address);
                ps.setDouble(3, totalPrice);
                ps.setString(4, paymentMethod);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    keys.next();
                    orderId = keys.getInt(1);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlDetail)) {
                for (CartItemBean item : items) {
                    ps.setInt(1, orderId);
                    ps.setInt(2, item.getBook().getId());
                    ps.setInt(3, item.getQuantity());
                    ps.setDouble(4, item.getBook().getPrice());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
            return orderId;
        }
    }
}
