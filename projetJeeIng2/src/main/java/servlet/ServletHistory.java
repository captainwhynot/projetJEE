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
import entity.Basket;
import entity.User;

/**
 * Servlet implementation class ServletHistory
 */

@SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
@WebServlet("/History")
public class ServletHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletHistory() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
   	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   	 */
   	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
   		
   		User loginUser = ServletIndex.loginUser(request, response);
   		
   		Session session = sessionFactory.openSession();
		String sql = "SELECT * FROM Basket WHERE customerId = "+ loginUser.getId() +" AND bought=1;";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Basket.class);
		List<Basket> basketList = query.list();
		session.close();		
		
	    request.setAttribute("basketList", basketList);
   		this.getServletContext().getRequestDispatcher("/history.jsp").include(request, response);
   	}

   	/**
   	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   	 */
   	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   		doGet(request, response);
   	}

}
