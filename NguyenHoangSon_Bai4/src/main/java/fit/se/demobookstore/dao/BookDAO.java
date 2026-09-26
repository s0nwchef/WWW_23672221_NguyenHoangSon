package fit.se.demobookstore.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import fit.se.demobookstore.beans.Book;
import fit.se.demobookstore.util.DBUtil;

public class BookDAO {
    private DBUtil dbUtil;

    public BookDAO(DataSource dataSource) {
        dbUtil = new DBUtil(dataSource);
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String tittle = rs.getString("tittle");
        String author = rs.getString("author");
        String imgbook = rs.getString("imgbook");
        double price = rs.getDouble("price");
        int quantity = rs.getInt("quantity");
        return new Book(id, tittle, author, imgbook, price, quantity);
    }

    public List<Book> getAllBooks() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY id";
        try (Connection conn = dbUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Book getBookById(int id) {
        String sql = "SELECT * FROM books WHERE id=?";
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Book> searchByTittle(String keyword) {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE tittle LIKE ? ORDER BY id";
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
