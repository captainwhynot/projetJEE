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
 */

@WebServlet("/ManageFidelityPoint")  
public class ServletManageFidelityPoint extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
   	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   	 */
   	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   		if (!ServletIndex.isLogged(request, response)) {
			response.sendRedirect("./Index");
			return;
		}
   		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		
		CustomerDao customerDao = new CustomerDao(sessionFactory);
		List<Customer> customerList = customerDao.getCustomerList();
		
	    request.setAttribute("customerList", customerList);
   		this.getServletContext().getRequestDispatcher("/manageFidelityPoint.jsp").include(request, response);
   	}

   	/**
   	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   	 */
   	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   		doGet(request, response);
   		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

		String[] fidelityPointList = request.getParameterValues("fidelityPointList");
		String[] customerList = request.getParameterValues("customerList");
		CustomerDao customerDao = new CustomerDao(sessionFactory);
		
		if (fidelityPointList != null && customerList != null) {
			Customer customer = null;
			String email = null;
			int fidelityPoint = 0;
			
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
