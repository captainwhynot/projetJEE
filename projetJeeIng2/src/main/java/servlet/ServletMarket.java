package servlet;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import conn.HibernateUtil;
import dao.ProductDao;
import entity.Product;

@WebServlet("/Market")
public class ServletMarket extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        ProductDao productDao = new ProductDao(sessionFactory);
        List<Product> productList;

        String search = (String) request.getAttribute("search");
        String seller = (String) request.getAttribute("sellerId");

        if (search != null || seller != null) {
            // Si des critères de recherche ou de filtrage existent, ne faites rien car la liste est déjà filtrée
        } else {
            productList = productDao.getProductList();
            request.setAttribute("productList", productList);
        }

        this.getServletContext().getRequestDispatcher("/market.jsp").include(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        String search = request.getParameter("search");
        String seller = request.getParameter("sellerId");
        List<Product> productList = null;

        if (search != null) {
            String sql = "SELECT * FROM Product WHERE name LIKE '%" + search + "%';";
            SQLQuery query = session.createSQLQuery(sql).addEntity(Product.class);
            productList = query.list();
            request.setAttribute("search", search);
        } else if (seller != null) {
            int sellerId = Integer.parseInt(seller);
            String sql = "SELECT * FROM Product WHERE sellerId = "+ sellerId +";";
            SQLQuery query = session.createSQLQuery(sql).addEntity(Product.class);
            productList = query.list();
            request.setAttribute("sellerId", sellerId);
        } else {
            ProductDao productDao = new ProductDao(sessionFactory);
            productList = productDao.getProductList();
        }

        request.setAttribute("productList", productList);
        doGet(request, response);
    }
}
