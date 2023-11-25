package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

import conn.HibernateUtil;
import dao.UserDao;
import entity.Customer;

/**
 * Servlet implementation class ServletRegistration
 *
 * This servlet handles user registration. It processes both GET and POST requests, allowing users to view the registration page and submit their registration information. The servlet uses the UserDao to interact with the database and perform user registration. The registration.jsp page is used to display the registration form.
 */
@WebServlet("/Registration")
public class ServletRegistration extends HttpServlet {

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
		this.getServletContext().getRequestDispatcher("/registration.jsp").include(request, response);
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
		
		// Get registration information from the request
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String username = request.getParameter("username");
		
		// Create a new Customer user with hashed password
		Customer user = new Customer(email, BCrypt.hashpw(password, BCrypt.gensalt(12)), username);
		UserDao dao = new UserDao(HibernateUtil.getSessionFactory());		
		
		if(dao.saveUser(user)) {
			// Store the user in the session
			HttpSession session = request.getSession();
			session.setAttribute("user", user);

			// Mail's content
			String container = "Your account has been created successfully.<br>";
			container += "<div style='color: black'>"+
					      "Your identifiants are : <br>";
			container += "<ul>";
			container += "<li>e-mail : " + email + "<br></li>";
			container += "<li>password : " + password + "<br></li>";
			container += "<li>username : " + username + "<br></li>";
			container += "</ul>";
			container += "Go to the site : ";
			container += "<a href=\"http://localhost:8080/projetJeeIng2/Index\">MANGASTORE</a>" +
					     "</div>";
            // Send registration confirmation mail
			if (dao.sendMail(email, "MANGASTORE : Registration", container)) {
				response.getWriter().println("<script>showAlert('Your account has been successfully created.', 'success', './Index');</script>");
			} else {
		        response.getWriter().println("<script>showAlert('Confirmation mail didn\\'t send well.', 'warning', './Index');</script>");
			}
		} else {
	        response.getWriter().println("<script>showAlert('This e-mail is already taken.', 'error', '');</script>");
		}
	}
}
