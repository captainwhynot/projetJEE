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

/**
 * Servlet implementation class ServletMarket
 *
 * This servlet manages the display of products in the market. It processes both GET and POST requests, allowing users to view and filter products based on search criteria or the seller. The market.jsp page is used to display the products.
 */
@SuppressWarnings({"deprecation", "unchecked", "rawtypes"})
@WebServlet("/Market")
public class ServletMarket extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Handles the HTTP GET method.
     *
     * @param request  The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @throws ServletException If the servlet encounters a servlet-specific problem.
     * @throws IOException      If an I/O error occurs.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        ProductDao productDao = new ProductDao(sessionFactory);
        List<Product> productList;

        String search = request.getParameter("search");
        String seller = request.getParameter("sellerId");

        if (search != null || seller != null) {
            // If search or filtering criteria exist, do nothing as the list is already filtered.
        } else {
        	// Get the list of all products for display
            productList = productDao.getProductList();
            request.setAttribute("productList", productList);
        }

        this.getServletContext().getRequestDispatcher("/market.jsp").include(request, response);
    }

    /**
     * Handles the HTTP POST method.
     *
     * @param request  The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @throws ServletException If the servlet encounters a servlet-specific problem.
     * @throws IOException      If an I/O error occurs.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        String search = request.getParameter("search");
        String seller = request.getParameter("sellerId");
        List<Product> productList = null;

        if (search != null) {
            // If a search parameter exists, filter products based on the search criteria
            String sql = "SELECT * FROM Product WHERE name LIKE '%" + search + "%';";
            SQLQuery query = session.createSQLQuery(sql).addEntity(Product.class);
            productList = query.list();
            request.setAttribute("search", search);
        } else if (seller != null) {
            // If a seller parameter exists, filter products based on the seller's ID
        	int sellerId = Integer.parseInt(seller);
            String sql = "SELECT * FROM Product WHERE sellerId = "+ sellerId +";";
            SQLQuery query = session.createSQLQuery(sql).addEntity(Product.class);
            productList = query.list();
            request.setAttribute("sellerId", sellerId);
        } else {
            // If no search or seller parameter exists, get the list of all products for display
        	ProductDao productDao = new ProductDao(sessionFactory);
            productList = productDao.getProductList();
        }

        request.setAttribute("productList", productList);
        doGet(request, response);
    }
}
