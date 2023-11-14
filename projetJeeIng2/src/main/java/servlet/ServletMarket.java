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
@SuppressWarnings({"rawtypes", "deprecation", "unchecked"})

/**
 * Servlet implementation class ServletProduct
 */

@WebServlet("/Market")
public class ServletMarket extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
   	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   	 */
   	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   		if (request.getAttribute("productList") == null) {
   			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

   			ProductDao productDao = new ProductDao(sessionFactory);
   			List<Product> productList = productDao.getProductList();

   			request.setAttribute("productList", productList);
   		}
   		this.getServletContext().getRequestDispatcher("/market.jsp").include(request, response);
   	}

   	/**
   	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   	 */
   	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		String search = request.getParameter("search");
		String seller = request.getParameter("sellerId");

		if (search != null) {
	   		String sql = "SELECT * FROM Product WHERE name LIKE '%" + search + "%';";
			SQLQuery query = session.createSQLQuery(sql).addEntity(Product.class);
			List<Product> productList = query.list();

			request.setAttribute("productList", productList);
			request.setAttribute("search", search);
		}
		else if (seller != null) {
			int sellerId = Integer.parseInt(seller);
			String sql = "SELECT * FROM Product WHERE sellerId = "+ sellerId +";";
			SQLQuery query = session.createSQLQuery(sql).addEntity(Product.class);
			List<Product> productList = query.list();

			request.setAttribute("productList", productList);
			request.setAttribute("sellerId", sellerId);
		}
   		doGet(request, response);
   	}

}
