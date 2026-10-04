package org.example.bai6_REST.dao;

import org.example.bai6_REST.model.DanhMuc;
import org.example.bai6_REST.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DanhMucDAO {

    private static final String DATABASE = "quanlytintuc";

    // Lấy tất cả danh mục
    public List<DanhMuc> getAll() throws SQLException {
        List<DanhMuc> list = new ArrayList<>();

        String sql = """
                     SELECT MADM, TENDANHMUC, NGUOIQUANLY, GHICHU
                     FROM danhmuc
                     """;

        try (
                Connection connection = DBUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
        ) {

            while (rs.next()) {
                DanhMuc danhMuc = new DanhMuc(
                        rs.getInt("MADM"),
                        rs.getString("TENDANHMUC"),
                        rs.getString("NGUOIQUANLY"),
                        rs.getString("GHICHU")
                );

                list.add(danhMuc);
            }
        }
        return list;
    }

    // Lấy danh mục theo mã
    public DanhMuc getById(int id) throws SQLException {
        String sql = """
                     SELECT MADM, TENDANHMUC, NGUOIQUANLY, GHICHU
                     FROM danhmuc
                     WHERE MADM = ?
                     """;

        try (
                Connection connection = DBUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new DanhMuc(
                            rs.getInt("MADM"),
                            rs.getString("TENDANHMUC"),
                            rs.getString("NGUOIQUANLY"),
                            rs.getString("GHICHU")
                    );
                }
            }
        }

        return null;
    }
}
