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

import fit.se.demobookstore.beans.Book;
import fit.se.demobookstore.beans.CartBean;
import fit.se.demobookstore.dao.BookDAO;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private BookDAO bookDAO;

    @Resource(name = "jdbc/bookstoredb")
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        try {
            bookDAO = new BookDAO(dataSource);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("giohang.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        CartBean cart = (CartBean) session.getAttribute("cart");
        if (cart == null) {
            cart = new CartBean();
            session.setAttribute("cart", cart);
        }

        String action = req.getParameter("action");

        try {
            if ("add".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Book b = bookDAO.getBookById(id);
                cart.addBook(b);
            } else if ("update".equals(action)) {
                int id = Integer.parseInt(req.getParameter("bookId"));
                int quantity = Integer.parseInt(req.getParameter("quantity"));
                cart.updateQuantity(id, quantity);
            } else if ("remove".equals(action)) {
                int id = Integer.parseInt(req.getParameter("bookId"));
                cart.removeBook(id);
            } else if ("clear".equals(action)) {
                cart.clear();
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }

        resp.sendRedirect("cart");
    }
}
