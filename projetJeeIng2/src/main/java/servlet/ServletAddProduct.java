package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
     * @see HttpServlet#HttpServlet()
     */
    public ServletAddProduct() {
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
        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        int stock = Integer.parseInt(request.getParameter("stock"));

        // Créer un objet Product
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setStock(stock);
        //product.setModerator(modo);

        // Appeler la méthode addProduct
        ProductDao productDao = new ProductDao(HibernateUtil.getSessionFactory());
        boolean success = productDao.addProduct(product);

        // Rediriger en fonction du résultat
        if (success) {
        	response.getWriter().println("<script>alert('The product has been added!');</script>");
        } else {
        	response.getWriter().println("<script>alert('Error ! The product has not been added');</script>");
        }
    }

}
