package fit.se.demobookstore.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import fit.se.demobookstore.beans.CartBean;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

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
        cart.clear();
        req.setAttribute("message", "Đặt hàng thành công! Cảm ơn " + fullname + " đã mua sách.");

        req.getRequestDispatcher("thanhtoan.jsp").forward(req, resp);
    }
}
