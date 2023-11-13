package servlet;

import java.io.IOException;
import java.util.Arrays;
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
import dao.*;
import entity.*;

/**
 * Servlet implementation class ServletAddModerator
 */

@SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
@WebServlet("/AddModerator")
public class ServletAddModerator extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletAddModerator() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!ServletIndex.isLogged(request, response)) {
			response.sendRedirect("./Index");
			return;
		}
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		
		String sql = "SELECT *, 0 AS clazz_ FROM User WHERE typeUser != 'Administrator';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(User.class);
		List<User> userList = query.list();
		
		session.close();

	    request.setAttribute("userList", userList);
		this.getServletContext().getRequestDispatcher("/addModerator.jsp").include(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		String[] transferList = request.getParameterValues("transferList");
		String[] userList = request.getParameterValues("userList");
		UserDao userDao = new UserDao(sessionFactory);
        if (userList != null) {
    		for (String email: userList) {
            	boolean isChecked = false;
            	if (transferList != null) isChecked = Arrays.asList(transferList).contains(email);
        		User user = userDao.getUser(email);
        		if (user.getTypeUser().equals("Moderator") && !isChecked) {
        			ModeratorDao moderatorDao = new ModeratorDao(sessionFactory);
        			Moderator moderator = moderatorDao.getModerator(email);
        			moderatorDao.transferIntoCustomer(moderator);
        		}
        		else if (user.getTypeUser().equals("Customer") && isChecked) {
        			CustomerDao customerDao = new CustomerDao(sessionFactory);
        			Customer customer = customerDao.getCustomer(email);
        			customerDao.transferIntoModerator(customer);
        		}
        	}
        }
		response.sendRedirect("./AddModerator");
	}

}
