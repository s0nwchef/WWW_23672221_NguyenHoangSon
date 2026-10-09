package fit.iuh.bai6_tuan5.dao;

import fit.iuh.bai6_tuan5.model.TinTuc;
import fit.iuh.bai6_tuan5.util.DBUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DanhSachTinTucQuanLy {
    private DBUtil dbutil;

    public DanhSachTinTucQuanLy(DataSource dataSource) {
        dbutil = new DBUtil(dataSource);
    }

    private static final String SELECT_JOIN =
            "SELECT t.MATT, t.TIEUDE, t.NOIDUNGTT, t.LIENKET, t.MADM, d.TENDANHMUC "
          + "FROM tintuc t LEFT JOIN danhmuc d ON t.MADM = d.MADM ";

    private TinTuc map(ResultSet rs) throws Exception {
        TinTuc tt = new TinTuc(
                rs.getInt("MATT"),
                rs.getString("TIEUDE"),
                rs.getString("NOIDUNGTT"),
                rs.getString("LIENKET"),
                rs.getInt("MADM"));
        tt.setTenDanhMuc(rs.getString("TENDANHMUC"));
        return tt;
    }

    public List<TinTuc> getAllTinTuc() {
        List<TinTuc> list = new ArrayList<>();
        String sql = SELECT_JOIN + "ORDER BY t.MATT";
        try (Connection conn = dbutil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<TinTuc> getAllByDanhMuc(int maDM) {
        List<TinTuc> list = new ArrayList<>();
        String sql = SELECT_JOIN + "WHERE t.MADM = ? ORDER BY t.MATT";
        try (Connection conn = dbutil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDM);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean save(TinTuc tt) {
        String sql = "INSERT INTO tintuc(MATT, TIEUDE, NOIDUNGTT, LIENKET, MADM) VALUES (?,?,?,?,?)";
        try (Connection conn = dbutil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tt.getMaTT());
            ps.setString(2, tt.getTieuDe());
            ps.setString(3, tt.getNoiDungTT());
            ps.setString(4, tt.getLienKet());
            ps.setInt(5, tt.getMaDM());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void delete(int maTT) {
        String sql = "DELETE FROM tintuc WHERE MATT=?";
        try (Connection conn = dbutil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maTT);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
