package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;

import conn.HibernateUtil;
import dao.CustomerDao;
import entity.Customer;

/**
 * Servlet implementation class ServletManageFidelityPoint
 *
 * This servlet manages the fidelity points of customers. It processes both GET and POST requests, allowing administrators to view and update the fidelity points of customers.
 */
@WebServlet("/ManageFidelityPoint")  
public class ServletManageFidelityPoint extends HttpServlet {
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
		
   		// Get the customer list
		CustomerDao customerDao = new CustomerDao(sessionFactory);
		List<Customer> customerList = customerDao.getCustomerList();
		
	    request.setAttribute("customerList", customerList);
   		this.getServletContext().getRequestDispatcher("/manageFidelityPoint.jsp").include(request, response);
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

   		// Get the fidelity points list updated from the request
		String[] fidelityPointList = request.getParameterValues("fidelityPointList");
		String[] customerList = request.getParameterValues("customerList");
		CustomerDao customerDao = new CustomerDao(sessionFactory);
		
		if (fidelityPointList != null && customerList != null) {
			Customer customer = null;
			String email = null;
			int fidelityPoint = 0;
			// Update the fidelity points of each customer
    		for (int i = 0; i < customerList.length; i++) {
    			email = customerList[i];
    			fidelityPoint = Integer.parseInt(fidelityPointList[i]);
    			customer = customerDao.getCustomer(email);
    			if (!customerDao.setFidelityPoint(customer, fidelityPoint - customer.getFidelityPoint())) {
    				response.getWriter().println("<script>showAlert('Update failed.', 'error', './ManageFidelityPoint')</script>");
    			}
    		}
    		response.getWriter().println("<script>showAlert('Update completed.', 'success', './ManageFidelityPoint')</script>");
		}
   	}
}
