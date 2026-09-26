package fit.se.demobookstore.servlet;

import java.io.IOException;
import java.util.List;

import jakarta.annotation.Resource;
import javax.sql.DataSource;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import fit.se.demobookstore.beans.Book;
import fit.se.demobookstore.dao.BookDAO;

@WebServlet({"/books", "/book"})
public class BookServlet extends HttpServlet {
    private BookDAO bookDAO;

    @Resource(name = "jdbc/bookstoredb")
    private DataSource dataSource;

    @Override
    public void init() {
        bookDAO = new BookDAO(dataSource);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idstr = req.getParameter("id");
        String keyword = req.getParameter("keyword");

        // Xem chi tiết 1 sách
        if (idstr != null) {
            int id = Integer.parseInt(idstr);
            Book book = bookDAO.getBookById(id);
            if (book != null) {
                req.setAttribute("book", book);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/chitietsach.jsp");
                dispatcher.forward(req, resp);
                return;
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found");
                return;
            }
        }

        // Tìm kiếm sách theo tên
        List<Book> books;
        if (keyword != null && !keyword.trim().isEmpty()) {
            books = bookDAO.searchByTittle(keyword.trim());
            req.setAttribute("keyword", keyword.trim());
        } else {
            books = bookDAO.getAllBooks();
        }

        req.setAttribute("books", books);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/danhsach.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
