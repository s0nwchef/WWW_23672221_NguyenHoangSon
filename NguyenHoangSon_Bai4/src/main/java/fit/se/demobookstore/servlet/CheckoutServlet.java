package fit.se.demobookstore.servlet;

import java.io.IOException;

import jakarta.annotation.Resource;
import javax.sql.DataSource;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import fit.se.demobookstore.beans.CartBean;
import fit.se.demobookstore.dao.OrderDAO;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private OrderDAO orderDAO;

    @Resource(name = "jdbc/bookstoredb")
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO(dataSource);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("thanhtoan.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        CartBean cart = (CartBean) session.getAttribute("cart");

        String action = req.getParameter("action");

        if ("cancel".equals(action)) {
            resp.sendRedirect("cart");
            return;
        }

        if (cart == null || cart.getItems().isEmpty()) {
            resp.sendRedirect("cart");
            return;
        }

        String fullname = req.getParameter("fullname");
        String address = req.getParameter("address");
        String paymentMethod = req.getParameter("paymentMethod");

        try {
            orderDAO.saveOrder(fullname, address, cart.getTotal(), paymentMethod, cart.getItems());
            cart.clear();
            req.setAttribute("message", "Đặt hàng thành công! Cảm ơn " + fullname + " đã mua sách.");
        } catch (Exception e) {
            req.setAttribute("error", "Đặt hàng thất bại, vui lòng thử lại.");
            e.printStackTrace();
        }

        req.getRequestDispatcher("thanhtoan.jsp").forward(req, resp);
    }
}
