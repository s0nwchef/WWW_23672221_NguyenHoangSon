package fit.iuh.bai6_tuan5.dao;

import fit.iuh.bai6_tuan5.model.DanhMuc;
import fit.iuh.bai6_tuan5.util.DBUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DanhMucDAO {
    private DBUtil dbutil;

    public DanhMucDAO(DataSource dataSource) {
        dbutil = new DBUtil(dataSource);
    }

    public List<DanhMuc> getAll() {
        List<DanhMuc> list = new ArrayList<>();
        String sql = "SELECT * FROM danhmuc ORDER BY MADM";
        try (Connection conn = dbutil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new DanhMuc(
                        rs.getInt("MADM"),
                        rs.getString("TENDANHMUC"),
                        rs.getString("NGUOIQUANLY"),
                        rs.getString("GHICHU")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
