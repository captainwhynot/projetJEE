package servlet;

import java.io.File;
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
 */

@MultipartConfig
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
		Part filePart = request.getPart("img");
        String fileName = getSubmittedFileName(filePart);
		
		UserDao userDao = new UserDao(sessionFactory);
		User seller = userDao.getUser(sellerId);

		Product product = new Product();
		product.setName(name);
		product.setPrice(price);
		product.setStock(stock);
		product.setUser(seller);

		ProductDao productDao = new ProductDao(sessionFactory);

		if (productDao.addProduct(product)) {
			//Save the image in the folder
	        String savePath = this.getServletContext().getRealPath("/img/Product");
	        File saveDir = new File(savePath);
	        if (!saveDir.exists()) {
	            saveDir.mkdirs();
	        }
	        
	        fileName = product.getId() + "_" + fileName;
	        String filePath = savePath + File.separator + fileName;
	        filePart.write(filePath);
	        
	        //Save the image in the database
			String image = "img/Product/"+fileName;
			if (productDao.modifyProduct(product, name, price, stock, image)) {
				response.getWriter().println("<script>showAlert('The product has been added!', 'success', './ManageProduct');</script>");
			} else {
				response.getWriter().println("<script>showAlert('The product\'s image has not been saved', 'warning', './ManageProduct');</script>");
			}
		} else {
			response.getWriter().println("<script>showAlert('Error ! The product has not been added', 'error', '');</script>");
		}
	}
	
	private String getSubmittedFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

}
