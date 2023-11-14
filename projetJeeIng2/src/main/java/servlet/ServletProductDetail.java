package servlet;

import java.io.IOException;
import java.io.PrintWriter;

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
 */

@WebServlet("/Product")
public class ServletProductDetail extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletProductDetail() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
   	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   	 */
   	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   		String productIdString = request.getParameter("productId");
   		if (productIdString != null) {
	   		int productId = Integer.parseInt(productIdString);
			request.setAttribute("productId", productId);
			this.getServletContext().getRequestDispatcher("/product.jsp").include(request, response);
   		}
   		else {
   			this.getServletContext().getRequestDispatcher("/product.jsp").include(request, response);
   			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("showAlert(\"This product does not exist.\", \"error\", \"./Market\");");
			out.println("</script>");
   		}
   	}

   	/**
   	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   	 */
   	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
   		String action = request.getParameter("action");
   		String productIdString = request.getParameter("productId");
   		System.out.println(action);
   		if (action != null && productIdString != null) {
	   		int productId = Integer.parseInt(productIdString);
	   		doGet(request, response);
   			if (action.equals("addOrder")) {
				PrintWriter out = response.getWriter();
   				if (ServletIndex.isLogged(request, response)) {
   					User user = ServletIndex.loginUser(request, response);
   					if (user.getTypeUser().equals("Customer")) {
   						CustomerDao customerDao = new CustomerDao(sessionFactory);
   						Customer customer = customerDao.getCustomer(user.getId());

   						ProductDao productDao = new ProductDao(sessionFactory);
   						Product product = productDao.getProduct(productId);
   						
   						BasketDao basketDao = new BasketDao(sessionFactory);
   						Basket basket = new Basket(product, 1, customer);
   						if (basketDao.addOrder(basket)) {
   	   	   					out.println("<script>");
   	   	   					out.println("showAlert('Product successfully added to the basket.', 'success', '');");
   	   	   					out.println("</script>");
   	   	   					return;
   						} else {
   	   	   					out.println("<script>");
   	   	   					out.println("showAlert('Failed to add the product to the basket.', 'error', '');");
   	   	   					out.println("</script>");
   	   	   					return;
   						}
   					}
   					else {
   	   					out.println("<script>");
   	   					out.println("showAlert('You must be logged in with a Customer account to add a product to your basket.', 'warning', './Login');");
   	   					out.println("</script>");
   	   					return;
   					}
   				} else {
   					out.println("<script>");
   					out.println("showAlert('You must be logged in to add a product to your basket.', 'warning', './Login');");
   					out.println("</script>");
   					return;
   				}
   			}
   		} else {
	   		doGet(request, response);
   		}
   	}

}
