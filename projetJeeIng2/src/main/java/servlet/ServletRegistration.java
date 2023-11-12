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
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String username = request.getParameter("username");
		
		Customer user = new Customer(email ,password, username);
			
		UserDao dao = new UserDao(HibernateUtil.getSessionFactory());		
		if(dao.saveUser(user)) {
			HttpSession session = request.getSession();
			session.setAttribute("user", user);

			response.sendRedirect("./Index");
			}
		else {
	        PrintWriter out = response.getWriter();
	        out.println("<html>");
	        out.println("<script>");
	        out.println("alert('Erreur, email déjà pris');");
	        out.println("</script>");
	        out.println("</html>");
			}
	}

}
