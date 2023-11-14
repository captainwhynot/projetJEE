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
 * Servlet implementation class ServletAddProduct
 */

@WebServlet("/AddProduct")
public class ServletAddProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!ServletIndex.isLogged(request, response)) {
			response.sendRedirect("./Index");
			return;
		}
		this.getServletContext().getRequestDispatcher("/addProduct.jsp").include(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!ServletIndex.isLogged(request, response)) {
			response.sendRedirect("./Index");
			return;
		}
		doGet(request, response);
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

		String name = request.getParameter("name");
		double price = Double.parseDouble(request.getParameter("price"));
		int stock = Integer.parseInt(request.getParameter("stock"));
		int sellerId = Integer.parseInt(request.getParameter("sellerId"));

		ModeratorDao moderatorDao = new ModeratorDao(sessionFactory);
		Moderator seller = moderatorDao.getModerator(sellerId);

		// Créer un objet Product à factoriser quand on aura les 5 attributs
		Product product = new Product();
		product.setName(name);
		product.setPrice(price);
		product.setStock(stock);
		product.setModerator(seller);
		// product.setImg(img)

		ProductDao productDao = new ProductDao(sessionFactory);

		if (productDao.addProduct(product)) {
			response.getWriter().println(
					"<script>showAlert('The product has been added!', 'success', './ManageProduct');</script>");
		} else {
			response.getWriter()
					.println("<script>showAlert('Error ! The product has not been added', 'error', '');</script>");
		}
	}

}
