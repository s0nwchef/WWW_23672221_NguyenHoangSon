package fit.iuh.bai6_tuan5.servlet;

import fit.iuh.bai6_tuan5.dao.DanhSachTinTucQuanLy;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;

@WebServlet("/quanly")
public class QuanLyFormServlet extends HttpServlet {

    @Resource(name = "jdbc/quanlytintuc")
    private DataSource dataSource;

    private DanhSachTinTucQuanLy ttDao;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        try {
            ttDao = new DanhSachTinTucQuanLy(dataSource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "delete":
                ttDao.delete(Integer.parseInt(req.getParameter("maTT")));
                resp.sendRedirect(req.getContextPath() + "/quanly");
                break;
            case "list":
            default:
                req.setAttribute("tinTucs", ttDao.getAllTinTuc());
                req.getRequestDispatcher("/QuanLyForm.jsp").forward(req, resp);
        }
    }
}
