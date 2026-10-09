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
import java.util.List;

@WebServlet("/danhsachtintuc")
public class DanhSachTinTucServlet extends HttpServlet {

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
        String maDM = req.getParameter("maDM");
        List<TinTuc> list;

        if (maDM != null && !maDM.isEmpty()) {
            list = ttDao.getAllByDanhMuc(Integer.parseInt(maDM));
        } else {
            list = ttDao.getAllTinTuc();
        }
        req.setAttribute("tinTucs", list);
        req.setAttribute("danhMucs", dmDao.getAll());
        req.getRequestDispatcher("/DanhSachTinTuc.jsp").forward(req, resp);
    }
}
