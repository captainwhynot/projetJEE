package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;

import conn.HibernateUtil;
import dao.ProductDao;
import entity.Product;

/**
 * Servlet implementation class ServletManageProduct
 */

@WebServlet("/ManageProduct")
public class ServletManageProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletManageProduct() {
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
   		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		
		ProductDao productDao = new ProductDao(sessionFactory);
		List<Product> productList = productDao.getProductList();
		
	    request.setAttribute("productList", productList);
   		this.getServletContext().getRequestDispatcher("/manageProduct.jsp").include(request, response);
   	}

   	/**
   	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   	 */
   	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ServletIndex.isLogged(request, response)) {
            response.sendRedirect("./Index");
            return;
        }
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        String action = request.getParameter("action");

        if (action != null) {
            ProductDao productDao = new ProductDao(sessionFactory);
        	if (action.equals("deleteProduct")) {
	            try {
	                String productIdString = request.getParameter("productId");
	                int productId = Integer.parseInt(productIdString);

	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	
	                if (productDao.deleteProduct(productId)) {
	                    response.setStatus(HttpServletResponse.SC_OK); // 200 OK
	                    response.getWriter().write("{\"status\": \"Product deleted successfully\"}");
	                } else {
	                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
	                    response.getWriter().write("{\"status\": \"Failed to delete product\"}");
	                }
	            } catch (Exception e) {
	                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
	                response.getWriter().write("{\"status\": \"Internal Server Error\"}");
	            }
        	} else if (action.equals("updateProduct")){
                String[] imgString = request.getParameterValues("img");
                String[] nameString = request.getParameterValues("name");
                String[] priceString = request.getParameterValues("price");
                String[] stockString = request.getParameterValues("stock");
                
                List<Product> productList = productDao.getProductList();
                if (productList != null && imgString != null && nameString != null && priceString != null && stockString != null) {
                	Product product = null;
                	String img = null;
                	String name = null;
                	double price = 0;
                	int stock = 0;
                	
                	for (int i = 0; i < productList.size(); i++) {
                		product = productList.get(i);
                		img = imgString[i];
                		name = nameString[i];
                		price = Double.parseDouble(priceString[i]);
                		stock = Integer.parseInt(stockString[i]);

                		if (!product.getImg().equals(img) || !product.getName().equals(name) || Double.compare(product.getPrice(), price) != 0 || Integer.compare(product.getStock(), stock) != 0) {
            				if (!productDao.modifyProduct(product, name, price, stock, img)) {
            					doGet(request, response);
                   				PrintWriter out = response.getWriter();
                   				out.println("<script>");
                   				out.println("showAlert(\"An error has occured updating the product.\", \"error\", \"./ManageProduct\")");
                   				out.println("</script>");
                   				return;
                			}
                		}
                	}
                	response.sendRedirect("./ManageProduct");
                } else {
                    doGet(request, response);
                }
        	} else {
                doGet(request, response);
        	}
        } else {
            doGet(request, response);
        }
    }

}
