package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;

import conn.HibernateUtil;
import dao.*;
import entity.*;

/**
 * Servlet implementation class ServletProductDetail
 *
 * This servlet manages the display and interaction with product details. It processes both GET and POST requests, allowing users to view product details and add products to their basket. The product.jsp page is used to display the product details.
 */
@WebServlet("/Product")
public class ServletProductDetail extends HttpServlet {
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
   		int productId = Integer.parseInt(request.getParameter("productId"));
   		// Display the product's information
		request.setAttribute("productId", productId);
		this.getServletContext().getRequestDispatcher("/product.jsp").include(request, response);
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
   		// Get the action from the request (addOrder)
   		String action = request.getParameter("action");
   		int productId = Integer.parseInt(request.getParameter("productId"));
   		
   		if (action != null) {
   			// Add the product to the basket
   			if (action.equals("addOrder")) {
   				if (ServletIndex.isLogged(request, response)) {
   					User user = ServletIndex.loginUser(request, response);
   					if (user.getTypeUser().equals("Customer")) {
   						CustomerDao customerDao = new CustomerDao(sessionFactory);
   						Customer customer = customerDao.getCustomer(user.getId());

   						ProductDao productDao = new ProductDao(sessionFactory);
   						Product product = productDao.getProduct(productId);
   						
   						BasketDao basketDao = new BasketDao(sessionFactory);
   						Basket basket = new Basket(product, 1, customer);
   						if (basketDao.addOrder(basket, user.getId(), 1)) {
   	   	   					response.getWriter().println("<script>showAlert('Product successfully added to the basket.', 'success', '');</script>");
   						} else {
   	   	   					response.getWriter().println("<script>showAlert('Failed to add the product to the basket.', 'error', '');</script>");
   						}
   					}
   					else {
   	   					response.getWriter().println("<script>showAlert('You must be logged in with a Customer account to add a product to your basket.', 'warning', '');</script>");
   					}
   				} else {
   					response.getWriter().println("<script>showAlert('You must be logged in to add a product to your basket.', 'warning', './Login');</script>");
   				}
   			}
   		}
   	}
}
