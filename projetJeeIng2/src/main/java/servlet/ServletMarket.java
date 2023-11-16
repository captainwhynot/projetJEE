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
    private static final int PRODUCTS_PER_PAGE = 10; // Nombre de produits par page

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        ProductDao productDao = new ProductDao(sessionFactory);
        List<Product> productList;

        // Récupérez les critères de recherche/filtrage à partir des attributs de requête
        String search = (String) request.getAttribute("search");
        String seller = (String) request.getAttribute("sellerId");

        if (search != null || seller != null) {
            // Si des critères de recherche ou de filtrage existent, ne faites rien car la liste est déjà filtrée
        } else {
            // Sinon, récupérez la liste complète des produits
            productList = productDao.getProductList();
            paginateProducts(request, productList);
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

        paginateProducts(request, productList);
        doGet(request, response);
    }


    private void paginateProducts(HttpServletRequest request, List<Product> productList) {
        int currentPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
        int startIndex = (currentPage - 1) * PRODUCTS_PER_PAGE;
        int endIndex = Math.min(startIndex + PRODUCTS_PER_PAGE, productList.size());
        List<Product> pageProducts = productList.subList(startIndex, endIndex);

        request.setAttribute("productList", pageProducts);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", (int) Math.ceil((double) productList.size() / PRODUCTS_PER_PAGE));
    }

}
