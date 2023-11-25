package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.hibernate.SessionFactory;

import conn.HibernateUtil;
import dao.*;
import entity.*;

/**
 * Servlet implementation class ServletAddProduct
 *
 * This servlet handles the addition of products.
 */

@MultipartConfig
@WebServlet("/AddProduct")
public class ServletAddProduct extends HttpServlet {
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
		this.getServletContext().getRequestDispatcher("/addProduct.jsp").include(request, response);
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
		if (!ServletIndex.isLogged(request, response)) {
			response.sendRedirect("./Index");
			return;
		}
		doGet(request, response);
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		// Get the form's data
		String name = request.getParameter("name");
		double price = Double.parseDouble(request.getParameter("price"));
		int stock = Integer.parseInt(request.getParameter("stock"));
		int sellerId = Integer.parseInt(request.getParameter("sellerId"));
		Part filePart = request.getPart("img");
        String fileName = ServletIndex.getSubmittedFileName(filePart);
		
		UserDao userDao = new UserDao(sessionFactory);
		User seller = userDao.getUser(sellerId);
		// Create the product to add
		Product product = new Product();
		product.setName(name);
		product.setPrice(price);
		product.setStock(stock);
		product.setUser(seller);

		ProductDao productDao = new ProductDao(sessionFactory);
        String savePath = this.getServletContext().getRealPath("/img/Product");

        // Add the producrt in the database
		if (productDao.addProduct(product)) {
	        // Save the image in the database
			if (productDao.updateProductImg(product, filePart, fileName, savePath)) {
				response.getWriter().println("<script>showAlert('The product has been added!', 'success', './ManageProduct');</script>");
			} else {
				response.getWriter().println("<script>showAlert('The product\\'s image has not been saved', 'warning', './ManageProduct');</script>");
			}
		} else {
			response.getWriter().println("<script>showAlert('Error ! The product has not been added', 'error', '');</script>");
		}
	}
}
