package iuh.fit.crud.sevlet;

import iuh.fit.crud.dao.productDAO;
import iuh.fit.crud.model.Product;
import iuh.fit.crud.util.SchemaInitializer; // Bổ sung - tự tạo bảng khi khởi động

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

@WebServlet("/products")
public class productServlet extends HttpServlet {

    @Resource(name = "jdbc/productdb")
    private DataSource dataSource;

    private productDAO productdao;
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        try {
            productdao = new productDAO(dataSource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        SchemaInitializer.initSchema(dataSource);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";
        switch (action) {
            case "list":
                List<Product> coproduct = productdao.getAllProduct();
                req.setAttribute("products", coproduct);
                req.getRequestDispatcher("views/listproduct.jsp").forward(req, resp);
                break;
            case "detail": {
                int id = Integer.parseInt(req.getParameter("id"));
                Product pro = productdao.getProductById(id);
                req.setAttribute("product", pro);
                req.getRequestDispatcher("views/detailproduct.jsp").forward(req, resp);
                break;
            }

            case "edit": {
                int id = Integer.parseInt(req.getParameter("id"));
                Product pro = productdao.getProductById(id);
                req.setAttribute("product", pro);
                req.getRequestDispatcher("views/editproduct.jsp").forward(req, resp);
                break;
            }

            case "delete": {
                int id = Integer.parseInt(req.getParameter("id"));
                productdao.deleteProduct(id);
                resp.sendRedirect("products");
                break;
            }

            case "new":
                req.getRequestDispatcher("views/newproduct.jsp").forward(req, resp);
                break;
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Product pro = new com.fasterxml.jackson.databind.ObjectMapper()
                .readValue(req.getReader(), Product.class);
        productdao.addProduct(pro);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        resp.getWriter().write(
                new com.fasterxml.jackson.databind.ObjectMapper()
                        .writeValueAsString(pro)
        );
    }
}