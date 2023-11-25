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
 *
 * This servlet handles operations related to the user's purchase history, such as retrieving and displaying past orders.
 */
@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
@WebServlet("/History")
public class ServletHistory extends HttpServlet {
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
		if (!ServletIndex.isLogged(request, response)) {
			response.sendRedirect("./Index");
			return;
		}
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

		User loginUser = ServletIndex.loginUser(request, response);

		// Get the customer's purchase history
		Session session = sessionFactory.openSession();
		String sql = "SELECT * FROM Basket WHERE customerId = " + loginUser.getId() + " AND purchaseDate IS NOT NULL;";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Basket.class);
		List<Basket> basketList = query.list();
		session.close();

		request.setAttribute("basketList", basketList);
		this.getServletContext().getRequestDispatcher("/history.jsp").include(request, response);
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
		doGet(request, response);
	}
}
