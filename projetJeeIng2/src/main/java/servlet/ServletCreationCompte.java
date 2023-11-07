package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import conn.HibernateUtil;
import dao.UserDao;
import entity.User;
import entity.User2;

@WebServlet("/creationCompte")
public class ServletCreationCompte extends HttpServlet {

	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("test doGet");
		
		
		
		
		
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		System.out.println("test doPost");
		
		
		
		String typeUser = req.getParameter("typeUser");
		System.out.println("typeUser : "+typeUser);
		if(typeUser.equals("user1") ) {
			String nom = req.getParameter("nom");
			String prenom = req.getParameter("prenom");
			User user = new User(nom,prenom);
			
			System.out.println(user.getNom()+" + "+user.getPrenom());
			
			UserDao dao= new UserDao(HibernateUtil.getSessionFactory());
			boolean b = dao.saveUser1(user);
			
			
			System.out.println("le bool: "+b);
			
			if(b) {System.out.println("c bon");}
			else {System.out.println("ERREUR ZEBI");}
		}
		
		if(typeUser.equals("user2")) {
			String nom = req.getParameter("nom");
			String prenom = req.getParameter("prenom");
			User2 user = new User2(nom,prenom);
			
			System.out.println(user.getNom()+" + "+user.getPrenom());
			
			UserDao dao= new UserDao(HibernateUtil.getSessionFactory());
			boolean b = dao.saveUser2(user);
			
			
			System.out.println("le bool: "+b);
			
			if(b) {System.out.println("c bon");}
			else {System.out.println("ERREUR ZEBI");}
		}
		
	}

}
