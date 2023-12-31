package servlet;

import java.io.IOException;
import org.hibernate.SessionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import conn.HibernateUtil;
import dao.*;
import entity.*;

/**
 * Servlet implementation class ServletLogin
 *
 * This servlet handles user login functionality. It processes both GET and POST requests, providing a login form for GET requests and validating user credentials for POST requests.
 */
@WebServlet("/Login")
public class ServletLogin extends HttpServlet {
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
		this.getServletContext().getRequestDispatcher("/login.jsp").include(request, response);
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
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		UserDao dao = new UserDao(sessionFactory);
		// Check the login's information then log in
		if (dao.checkUserLogin(email, password)) {
			// If the login is successful, store the user in the session
			User user = dao.getUser(email);
			HttpSession session = request.getSession();
			session.setAttribute("user", user);

			response.sendRedirect("./Index");
		} else {
			response.getWriter().println("<script>showAlert('Invalid identifiants.', 'error', '');</script>");
		}
	}
}
