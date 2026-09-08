package iuh.fit.crud.dao;

import iuh.fit.crud.model.Product;
import iuh.fit.crud.util.DBUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class productDAO {

    private DBUtil dbutil;

    public productDAO(DataSource dataSource) {
        dbutil = new DBUtil(dataSource);
    }

    public List<Product> getAllProduct() {
        List<Product> lstProduct = new ArrayList<>();
        String sql = "select * from products";
        try {
            Connection con = dbutil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product pro = new Product();
                pro.setId(rs.getInt("id"));
                pro.setName(rs.getString("name"));
                pro.setPrice(rs.getDouble("price"));
                lstProduct.add(pro);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return lstProduct;
    }

    public Product getProductById(int id) {
        String sql = "select * from products where id = ?";
        try (Connection con = dbutil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Product pro = new Product();
                pro.setId(rs.getInt("id"));
                pro.setName(rs.getString("name"));
                pro.setPrice(rs.getDouble("price"));
                pro.setDescription(rs.getString("description"));
                return pro;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void addProduct(Product pro) {
        String sql = "insert into products(name, price, description) values(?, ?, ?)";
        try (Connection con = dbutil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pro.getName());
            ps.setDouble(2, pro.getPrice());
            ps.setString(3, pro.getDescription());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateProduct(Product pro) {
        String sql = "update products set name=?, price=?, description=? where id=?";
        try (Connection con = dbutil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pro.getName());
            ps.setDouble(2, pro.getPrice());
            ps.setString(3, pro.getDescription());
            ps.setInt(4, pro.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteProduct(int id) {
        String sql = "delete from products where id=?";
        try (Connection con = dbutil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
