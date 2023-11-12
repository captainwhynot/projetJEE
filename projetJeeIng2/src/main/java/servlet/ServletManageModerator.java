package servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;

import conn.HibernateUtil;
import dao.ModeratorDao;
import entity.Moderator;

/**
 * Servlet implementation class ServletManageModerator
 */

@WebServlet("/ManageModerator")
public class ServletManageModerator extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */ 
    public ServletManageModerator() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
   	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   	 */
   	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		
		ModeratorDao moderatorDao = new ModeratorDao(sessionFactory);
		List<Moderator> moderatorList = moderatorDao.getModeratorList();
		
	    request.setAttribute("moderatorList", moderatorList);
   		this.getServletContext().getRequestDispatcher("/manageModerator.jsp").include(request, response);
   	}

   	/**
   	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   	 */
   	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   		doGet(request, response);
   		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		String[] addProductList = request.getParameterValues("addProductList");
		String[] modifyProductList = request.getParameterValues("modifyProductList");
		String[] deleteProductList = request.getParameterValues("deleteProductList");
		String[] moderatorList = request.getParameterValues("moderatorList");
		
		if (moderatorList != null) {
			ModeratorDao moderatorDao = new ModeratorDao(sessionFactory);
			Moderator moderator = null;
    		for (String email: moderatorList) {
    			moderator = moderatorDao.getModerator(email);
    			if (addProductList != null && Arrays.asList(addProductList).contains(email)) {
    				 moderatorDao.modifyRight(moderator, "addProduct", true);
    			} else {
   				 	moderatorDao.modifyRight(moderator, "addProduct", false);
    			}
    			if (modifyProductList != null && Arrays.asList(modifyProductList).contains(email)) {
   				 	moderatorDao.modifyRight(moderator, "modifyProduct", true);
    			} else {
   				 	moderatorDao.modifyRight(moderator, "modifyProduct", false);
    			}
    			if (deleteProductList != null && Arrays.asList(deleteProductList).contains(email)) {
   				 	moderatorDao.modifyRight(moderator, "deleteProduct", true);
   				} else {
    				moderatorDao.modifyRight(moderator, "deleteProduct", false);
				}
    		}
		}
		response.sendRedirect("./ManageModerator");
   	}

}
