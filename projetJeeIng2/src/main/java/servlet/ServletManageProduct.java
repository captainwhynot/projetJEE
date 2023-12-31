package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import conn.HibernateUtil;
import dao.ProductDao;
import entity.Product;
import entity.User;

/**
 * Servlet implementation class ServletManageProduct
 *
 * This servlet manages products, providing functionality to view, delete, and update product information. It processes both GET and POST requests, allowing administrators and moderators to interact with the list of products.
 */
@SuppressWarnings({"rawtypes", "deprecation", "unchecked"})
@MultipartConfig
@WebServlet("/ManageProduct")
public class ServletManageProduct extends HttpServlet {
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
		ProductDao productDao = new ProductDao(sessionFactory);

   		User loginUser = ServletIndex.loginUser(request, response);
   		if (loginUser.getTypeUser().equals("Administrator")) {
   			// Get the list of all products for administrators
			List<Product> productList = productDao.getProductList();
			
		    request.setAttribute("productList", productList);
   		} else if (loginUser.getTypeUser().equals("Moderator")) {
   			// Get the list of products of the logged in moderator
   			Session session = sessionFactory.openSession();
   			
   			String sql = "SELECT * FROM Product WHERE sellerId="+loginUser.getId()+";";
   			SQLQuery query = session.createSQLQuery(sql).addEntity(Product.class);
   			List<Product> productList = query.list();
   			
   			session.close();
   			
		    request.setAttribute("productList", productList);
   		}
   		this.getServletContext().getRequestDispatcher("/manageProduct.jsp").include(request, response);
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
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        // Get the action from the request (deleteProduct, updateProduct)
        String action = request.getParameter("action");
        
        if (action != null) {
            ProductDao productDao = new ProductDao(sessionFactory);
        	if (action.equals("deleteProduct")) {
                // Delete product
	            try {
	                String productIdString = request.getParameter("productId");
	                int productId = Integer.parseInt(productIdString);

	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");

        	        String savePath = this.getServletContext().getRealPath("/img/Product");
	                if (productDao.deleteProduct(productId, savePath)) {
	                    response.setStatus(HttpServletResponse.SC_OK); // 200 OK
	                    response.getWriter().write("{\"status\": \"Product deleted successfully.\"}");
	                } else {
	                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
	                    response.getWriter().write("{\"status\": \"Failed to delete product.\"}");
	                }
	            } catch (Exception e) {
	                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
	                response.getWriter().write("{\"status\": \"Internal Server Error.\"}");
	            }
        	} else if (action.equals("updateProduct")){
        		doGet(request, response);
        		// Update product's informations
                Collection<Part> fileParts = request.getParts();
                List<Part> filePartsString = new ArrayList<>();
                List<String> fileNameString = new ArrayList<>();
                // Get all the uploaded files' data from the request
                for (Part filePart : fileParts) {
                    if (filePart.getName().equals("imgFile")) {
                        String fileName = ServletIndex.getSubmittedFileName(filePart);
                        filePartsString.add(filePart);
                        fileNameString.add(fileName);
                    }
                }
                // Get all the products' informations from the request
                String[] imgString = request.getParameterValues("img");
                String[] nameString = request.getParameterValues("name");
                String[] priceString = request.getParameterValues("price");
                String[] stockString = request.getParameterValues("stock");

                List<Product> productLists;
                if (ServletIndex.loginUser(request, response).getTypeUser().equals("Administrator")) productLists = productDao.getProductList();
                else {
           			Session session = sessionFactory.openSession();
           			
           			String sql = "SELECT * FROM Product WHERE sellerId="+ServletIndex.loginUser(request, response).getId()+";";
           			SQLQuery query = session.createSQLQuery(sql).addEntity(Product.class);
           			productLists = query.list();
           			
           			session.close();
                }
                if (productLists != null && imgString != null && nameString != null && priceString != null && stockString != null && fileNameString != null) {
                	Product product = null;
                	String name = null;
                	double price = 0;
                	int stock = 0;

        	        String savePath = this.getServletContext().getRealPath("/img/Product");
                	String fileName = null;
        	        Part filePart = null;
        	        
                	for (int i = 0; i < productLists.size(); i++) {
                		product = productLists.get(i);
                		name = nameString[i];
                		price = Double.parseDouble(priceString[i]);
                		stock = Integer.parseInt(stockString[i]);
                		// If no file has been uploaded : get the old fileName
                		fileName = fileNameString.get(i);
                		if (fileName == null || fileName.equals("")) {
                			fileName = imgString[i];
                		}

                		filePart = filePartsString.get(i);
                		// Update all the product's information if there is a change
                		if (!product.getName().equals(name) || Double.compare(product.getPrice(), price) != 0 || Integer.compare(product.getStock(), stock) != 0) {
            				if (!productDao.modifyProduct(product, name, price, stock)) {
                   				response.getWriter().println("<script>showAlert('An error has occured updating the product.', 'error', './ManageProduct')</script>");
                			}
                		}
                		// Update the product's image if this is a new image
                		if (!fileName.contains("img/Product/")) {
                			if (!productDao.updateProductImg(product, filePart, fileName, savePath)) {
                   				response.getWriter().println("<script>showAlert('An error has occured updating the product\\'s image.', 'error', './ManageProduct')</script>");
                			}
                		}
                	}
                	response.getWriter().println("<script>showAlert('All products have been updated successfully.', 'success', './ManageProduct')</script>");
                } 
        	}
        } else {
    		doGet(request, response);
        }
    }
}
