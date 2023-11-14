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
import org.hibernate.Transaction;

import conn.HibernateUtil;
import entity.Product;
@SuppressWarnings({"rawtypes", "deprecation", "unchecked"})

/**
 * Servlet implementation class ServletProduct
 */

@WebServlet("/Market")
public class ServletMarket extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletMarket() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
   	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   	 */
   	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   		if (request.getAttribute("productList") == null) {
   			Session session = HibernateUtil.getSessionFactory().openSession();

   			String sql = "SELECT * FROM Product;";
   			SQLQuery query = session.createSQLQuery(sql).addEntity(Product.class);
   			List<Product> productList = query.list();

   			request.setAttribute("productList", productList);
   		}
   		this.getServletContext().getRequestDispatcher("/market.jsp").include(request, response);
   	}

   	/**
   	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   	 */
   	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		String search = request.getParameter("category");
		String seller = request.getParameter("sellerId");
		System.out.println(search + " - " + seller);
		if (search != null) {
	   		String sql = "SELECT * FROM Product WHERE name LIKE '%" + search + "%';";
			SQLQuery query = session.createSQLQuery(sql).addEntity(Product.class);
			List<Product> productList = query.list();
			
			request.setAttribute("productList", productList);
		}
		else if (seller != null) {
			int sellerId = Integer.parseInt(seller);
			String sql = "SELECT * FROM Product WHERE sellerId = "+ sellerId +";";
			SQLQuery query = session.createSQLQuery(sql).addEntity(Product.class);
			List<Product> productList = query.list();
			
			request.setAttribute("productList", productList);
		}
   		doGet(request, response);
   	}

}
