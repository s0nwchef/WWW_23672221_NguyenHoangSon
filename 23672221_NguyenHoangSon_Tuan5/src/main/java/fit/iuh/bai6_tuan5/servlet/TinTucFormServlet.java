package fit.iuh.bai6_tuan5.servlet;

import fit.iuh.bai6_tuan5.dao.DanhMucDAO;
import fit.iuh.bai6_tuan5.dao.DanhSachTinTucQuanLy;
import fit.iuh.bai6_tuan5.model.TinTuc;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet("/tintucform")
public class TinTucFormServlet extends HttpServlet {

    private static final Pattern P_MATT = Pattern.compile("^\\d{1,9}$");
    private static final Pattern P_LIENKET = Pattern.compile("^http://\\S+$");
    private static final Pattern P_NOIDUNG = Pattern.compile("^[\\s\\S]{1,255}$");

    @Resource(name = "jdbc/quanlytintuc")
    private DataSource dataSource;

    private DanhSachTinTucQuanLy ttDao;
    private DanhMucDAO dmDao;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        try {
            ttDao = new DanhSachTinTucQuanLy(dataSource);
            dmDao = new DanhMucDAO(dataSource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("danhMucs", dmDao.getAll());
        req.getRequestDispatcher("/TinTucForm.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String maTT = trim(req.getParameter("maTT"));
        String tieuDe = trim(req.getParameter("tieuDe"));
        String lienKet = trim(req.getParameter("lienKet"));
        String noiDung = req.getParameter("noiDungTT") == null ? "" : req.getParameter("noiDungTT").replace("\r\n", "\n");
        String maDM = trim(req.getParameter("maDM"));

        String error = null;
        if (maTT.isEmpty() || tieuDe.isEmpty() || lienKet.isEmpty() || noiDung.trim().isEmpty() || maDM.isEmpty()) {
            error = "Mã TT, Tiêu đề, Liên kết, Nội dung, Danh mục là bắt buộc nhập";
        } else if (!P_MATT.matcher(maTT).matches()) {
            error = "Mã TT phải là số";
        } else if (!P_LIENKET.matcher(lienKet).matches()) {
            error = "Liên kết phải bắt đầu bằng http://";
        } else if (!P_NOIDUNG.matcher(noiDung).matches()) {
            error = "Nội dung không quá 255 ký tự";
        }

        if (error == null) {
            TinTuc tt = new TinTuc(Integer.parseInt(maTT), tieuDe, noiDung, lienKet, Integer.parseInt(maDM));
            if (ttDao.save(tt)) {
                resp.sendRedirect(req.getContextPath() + "/danhsachtintuc?maDM=" + maDM);
                return;
            }
            error = "Không thêm được tin tức (Mã TT đã tồn tại hoặc dữ liệu không hợp lệ)";
        }

        req.setAttribute("error", error);
        req.setAttribute("danhMucs", dmDao.getAll());
        req.getRequestDispatcher("/TinTucForm.jsp").forward(req, resp);
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
