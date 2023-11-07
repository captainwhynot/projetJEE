package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import conn.HibernateUtil;
import dao.CustomerDao;
import entity.Customer;
import entity.Administrator;

@WebServlet("/creationCompte")
public class ServletCreationCompte extends HttpServlet {

	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("test doGet");
		
		
		
		
		
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		String username = req.getParameter("username");
		
		Customer user = new Customer(email ,password, username);
			
		CustomerDao dao= new CustomerDao(HibernateUtil.getSessionFactory());
		boolean b = dao.saveCustomer(user);
		
		System.out.println("Sauvegarde: "+b);
		
		if(b) {System.out.println("Sauvegarde réussi");}
		else {System.out.println("Erreur");}
	}

}
