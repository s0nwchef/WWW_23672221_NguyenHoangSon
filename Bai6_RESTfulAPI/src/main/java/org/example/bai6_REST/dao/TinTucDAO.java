package org.example.bai6_REST.dao;

import org.example.bai6_REST.model.DanhMuc;
import org.example.bai6_REST.model.TinTuc;
import org.example.bai6_REST.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TinTucDAO {


    // Map 1 dòng ResultSet (đã JOIN danhmuc) -> TinTuc
    private TinTuc map(ResultSet rs) throws SQLException {

        DanhMuc danhMuc = null;

        if (rs.getObject("MADM") != null) {
            danhMuc = new DanhMuc(
                    rs.getInt("MADM"),
                    rs.getString("TENDANHMUC"),
                    rs.getString("NGUOIQUANLY"),
                    rs.getString("GHICHU")
            );
        }

        return new TinTuc(
                rs.getInt("MATT"),
                rs.getString("TIEUDE"),
                rs.getString("NOIDUNGTT"),
                rs.getString("LIENKET"),
                danhMuc
        );
    }

    // Lấy tất cả tin tức
    public List<TinTuc> getAll() throws SQLException {

        List<TinTuc> list = new ArrayList<>();

        String sql = """
                SELECT t.MATT, t.TIEUDE, t.NOIDUNGTT, t.LIENKET,
                       d.MADM, d.TENDANHMUC, d.NGUOIQUANLY, d.GHICHU
                FROM tintuc t
                LEFT JOIN danhmuc d
                ON t.MADM = d.MADM
                """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
        }

        return list;
    }

    // Lấy tin tức theo mã
    public TinTuc getById(int id) throws SQLException {

        String sql = """
                SELECT t.MATT, t.TIEUDE, t.NOIDUNGTT, t.LIENKET,
                       d.MADM, d.TENDANHMUC, d.NGUOIQUANLY, d.GHICHU
                FROM tintuc t
                LEFT JOIN danhmuc d
                ON t.MADM = d.MADM
                WHERE t.MATT = ?
                """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        }

        return null;
    }

    // Lấy tin tức theo danh mục
    public List<TinTuc> getByDanhMuc(int maDM) throws SQLException {

        List<TinTuc> list = new ArrayList<>();

        String sql = """
                SELECT t.MATT, t.TIEUDE, t.NOIDUNGTT, t.LIENKET,
                       d.MADM, d.TENDANHMUC, d.NGUOIQUANLY, d.GHICHU
                FROM tintuc t
                JOIN danhmuc d
                ON t.MADM = d.MADM
                WHERE d.MADM = ?
                """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maDM);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }

        return list;
    }

    // Thêm tin tức
    // - maTT > 0  : chèn đúng mã người dùng nhập (Mã TT bắt buộc theo đề)
    // - maTT <= 0 : để AUTO_INCREMENT tự sinh
    // Sau khi thêm, tintuc.maTT được gán lại bằng mã thực tế trong DB
    public boolean add(TinTuc tinTuc) throws SQLException {

        boolean hasId = tinTuc.getMaTT() > 0;

        String sql = hasId
                ? "INSERT INTO tintuc(MATT, TIEUDE, NOIDUNGTT, LIENKET, MADM) VALUES (?, ?, ?, ?, ?)"
                : "INSERT INTO tintuc(TIEUDE, NOIDUNGTT, LIENKET, MADM) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            int i = 1;

            if (hasId) {
                ps.setInt(i++, tinTuc.getMaTT());
            }

            ps.setString(i++, tinTuc.getTieuDe());
            ps.setString(i++, tinTuc.getNoiDungTT());
            ps.setString(i++, tinTuc.getLienKet());

            if (tinTuc.getDanhMuc() != null) {
                ps.setInt(i, tinTuc.getDanhMuc().getMaDM());
            } else {
                ps.setNull(i, Types.INTEGER);
            }

            boolean ok = ps.executeUpdate() > 0;

            if (ok && !hasId) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        tinTuc.setMaTT(keys.getInt(1));
                    }
                }
            }

            return ok;
        }
    }

    // Xóa tin tức
    public boolean delete(int id) throws SQLException {

        String sql = "DELETE FROM tintuc WHERE MATT = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }
}
