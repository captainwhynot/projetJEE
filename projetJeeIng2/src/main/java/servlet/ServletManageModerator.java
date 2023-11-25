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
 *
 * This servlet manages moderators, providing functionality to view and update moderator rights. It processes both GET and POST requests, allowing administrators to interact with the list of moderators.
 */
@WebServlet("/ManageModerator")
public class ServletManageModerator extends HttpServlet {
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
		
   		// Get the moderator list
		ModeratorDao moderatorDao = new ModeratorDao(sessionFactory);
		List<Moderator> moderatorList = moderatorDao.getModeratorList();
		
	    request.setAttribute("moderatorList", moderatorList);
   		this.getServletContext().getRequestDispatcher("/manageModerator.jsp").include(request, response);
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
   		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
   		
   		// Get the updated right's list from the request
		String[] addProductList = request.getParameterValues("addProductList");
		String[] modifyProductList = request.getParameterValues("modifyProductList");
		String[] deleteProductList = request.getParameterValues("deleteProductList");
		String[] moderatorList = request.getParameterValues("moderatorList");
		
		if (moderatorList != null) {
			ModeratorDao moderatorDao = new ModeratorDao(sessionFactory);
			Moderator moderator = null;
			boolean allUpdated = true;

			// Update moderator rights
    		for (String email: moderatorList) {
    			moderator = moderatorDao.getModerator(email);
    			// addProduct's Right
    			if (addProductList != null && Arrays.asList(addProductList).contains(email)) {
    				allUpdated = allUpdated && moderatorDao.modifyRight(moderator, "addProduct", true);
    			} else {
    				allUpdated = allUpdated && moderatorDao.modifyRight(moderator, "addProduct", false);
    			}
    			// modifyProduct's Right
    			if (modifyProductList != null && Arrays.asList(modifyProductList).contains(email)) {
    				allUpdated = allUpdated && moderatorDao.modifyRight(moderator, "modifyProduct", true);
    			} else {
    				allUpdated = allUpdated && moderatorDao.modifyRight(moderator, "modifyProduct", false);
    			}
    			// deleteProduct's Right
    			if (deleteProductList != null && Arrays.asList(deleteProductList).contains(email)) {
    				allUpdated = allUpdated && moderatorDao.modifyRight(moderator, "deleteProduct", true);
   				} else {
   					allUpdated = allUpdated && moderatorDao.modifyRight(moderator, "deleteProduct", false);
				}
    		}
    		
    		if (allUpdated) {
    			response.getWriter().println("<script>showAlert('All rights updated successfully.', 'success', './ManageModerator')</script>");
			} else {
				response.getWriter().println("<script>showAlert('Update failed.', 'error', './ManageModerator')</script>");
			}
		}
   	}
}
