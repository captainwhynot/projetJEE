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
   		this.getServletContext().getRequestDispatcher("/market.jsp").include(request, response);
   	}

   	/**
   	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   	 */
   	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   		doGet(request, response);

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		
		String product = request.getParameter("category");
   		String sql = "SELECT * FROM Product WHERE name like " + product + ";";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Product.class);
		List<Product> productList = query.list();
		
		
   	}

}
