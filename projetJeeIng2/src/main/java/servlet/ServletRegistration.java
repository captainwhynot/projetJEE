package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import conn.HibernateUtil;
import dao.UserDao;
import entity.Customer;

@WebServlet("/Registration")
public class ServletRegistration extends HttpServlet {

	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher("/registration.jsp").include(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher("/registration.jsp").include(req, resp);
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		String username = req.getParameter("username");
		
		Customer user = new Customer(email ,password, username);
			
		UserDao dao= new UserDao(HibernateUtil.getSessionFactory());
		boolean b = dao.saveUser(user);
		
		if(b) {
			System.out.println("Sauvegarde réussie");
			//sauvegarder en session l'utilisateur
	        resp.sendRedirect("./Index");
			}
		else {
	        PrintWriter out = resp.getWriter();
	        out.println("<html>");
	        out.println("<script>");
	        out.println("alert('Erreur, email déjà pris');");
	        out.println("</script>");
	        out.println("</html>");
			}
	}

}
