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
   		if (request.getAttribute("productList") == null) {
   			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
   			//All results
   			ProductDao productDao = new ProductDao(sessionFactory);
   			List<Product> productList = productDao.getProductList();
   			paginateProducts(request, productList); // Méthode pour la pagination
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
            productList = new ProductDao(sessionFactory).getProductList();
        }

        paginateProducts(request, productList); // Méthode pour la pagination
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
