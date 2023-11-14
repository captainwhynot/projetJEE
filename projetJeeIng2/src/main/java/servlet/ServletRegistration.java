package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import conn.HibernateUtil;
import dao.UserDao;
import entity.Customer;

@WebServlet("/Registration")
public class ServletRegistration extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher("/registration.jsp").include(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
        PrintWriter out = response.getWriter();
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String username = request.getParameter("username");
		
		Customer user = new Customer(email ,password, username);
		UserDao dao = new UserDao(HibernateUtil.getSessionFactory());		
		
		if(dao.saveUser(user)) {
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			String container = "Your account has been created successfully.<br>";
			container += "Your identifiants are : <br>";
			container += "<ul>";
			container += "<li>e-mail : " + email + "<br></li>";
			container += "<li>password : " + password + "<br></li>";
			container += "<li>username : " + username + "<br></li>";
			container += "</ul>";
			container += "Go to the site : ";
			container += "<a href=\"http://localhost:8080/projetJeeIng2/Index\">MANGASTORE</a>";
			if (dao.sendMail(email, "MANGASTORE : Registration", container)) {
				out.println("<script>");
		        out.println("showAlert('Your account has been successfully created.', 'success', './Index');");
		        out.println("</script>");
			} else {
		        out.println("<script>");
		        out.println("showAlert('Confirmation mail didn't send well.', 'warning', './Index');");
		        out.println("</script>");
			}
		} else {
	        out.println("<script>");
	        out.println("showAlert('This e-mail is already taken.', 'error', '');");
	        out.println("</script>");
	        return;
			}
	}

}
