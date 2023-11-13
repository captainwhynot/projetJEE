package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;

import conn.HibernateUtil;
import dao.BasketDao;
import dao.CreditCardDao;
import entity.Basket;
import entity.User;

/**
 * Servlet implementation class ServletBasket
 */

@SuppressWarnings("deprecation")
   
@WebServlet("/Basket")
public class ServletBasket extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletBasket() {
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
		BasketDao basketDao = new BasketDao(sessionFactory);
		List<Basket> basketList = basketDao.getBasketList(ServletIndex.loginUser(request, response).getId());
		
	    request.setAttribute("basketList", basketList);
   		this.getServletContext().getRequestDispatcher("/basket.jsp").include(request, response);
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
   		
   		User loginUser = ServletIndex.loginUser(request, response);
   		if (action != null) {
	   	    if (action.equals("checkStock")) {
	   	        try {
	   	        	String basketIdString = request.getParameter("basketId");
		   	        String quantityString = request.getParameter("quantity");
		   	        
		   	        int basketId = Integer.parseInt(basketIdString);
		   	        int quantity = Integer.parseInt(quantityString);
		   	        
		   	        BasketDao basketDao = new BasketDao(sessionFactory);
		            Basket basket = basketDao.getBasket(basketId);
		   	        response.setContentType("application/json");
		            response.setCharacterEncoding("UTF-8");
	
			   	    if (basketDao.checkStock(basket.getProduct().getId(), quantity)) {
			   	    	basketDao.updateQuantity(basket.getId(), quantity - basket.getQuantity());
			   	    	response.setStatus(HttpServletResponse.SC_OK); // 200 OK
			            response.getWriter().write("{\"status\": \"Enough stock\"}");
			        } else {
			            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
			            response.getWriter().write("{\"status\": \"Insufficient stock\"}");
			        }
	   	        } catch (Exception e) {
		   	         response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
		   	         response.getWriter().write("{\"status\": \"Internal Server Error\"}");
	   	     }
	   	    } else if (action.equals("confirmOrder")){
	   			BasketDao basketDao = new BasketDao(sessionFactory);
	   			List<Basket> basketList = basketDao.confirmOrder(loginUser.getId());
	   			
	   		    request.setAttribute("basketList", basketList);
	   	    	this.getServletContext().getRequestDispatcher("/confirmOrder.jsp").include(request, response);
	   			
	   	    } else if (action.equals("confirmCreditCard")) {
	   	    	this.getServletContext().getRequestDispatcher("/checkCreditCard.jsp").include(request, response);
	   	    } else if (action.equals("finalizePaiement")) {
   				PrintWriter out = response.getWriter();
   				
	   	   		String cardNumberString = request.getParameter("cardNumber");
	   	   		String cvvString = request.getParameter("cvv");
	   	   		String expirationDateString = request.getParameter("expirationDate");

	   	        int cardNumber = Integer.parseInt(cardNumberString);
	   	        int cvv = Integer.parseInt(cvvString);
	   	        String[] expirationDateArray = expirationDateString.split("-");
		   	    int year = Integer.parseInt(expirationDateArray[0]) - 1900;
			   	int month = Integer.parseInt(expirationDateArray[1]) - 1;
			   	int day = Integer.parseInt(expirationDateArray[2]);
			   	
				Date expirationDate = new Date(year, month, day);
	   	        CreditCardDao creditCardDao = new CreditCardDao(sessionFactory);

	   	        if (creditCardDao.checkCreditCard(cardNumber, cvv, expirationDate)) {
		   			BasketDao basketDao = new BasketDao(sessionFactory);
		   		
		   			if (basketDao.finalizePaiement(loginUser.getId(), cardNumber, basketDao.totalPrice(loginUser.getId()))) {
			   	         out.println("<script>");
			   	         out.println("alert('Successfully paid.');");
			   	         out.println("window.location.href = '/projetJeeIng2/Basket';");
			   	         out.println("</script>");
		   			}
		   			else {
			   	         out.println("<script>");
			   	         out.println("alert('Payment failed.');");
			   	         out.println("window.location.href = '/projetJeeIng2/Basket';");
			   	         out.println("</script>");
		   			}
	   	        } else {
		   	    	 this.getServletContext().getRequestDispatcher("/checkCreditCard.jsp").include(request, response);
		   	         out.println("<script>");
		   	         out.println("alert('The credit card is invalid, please check the informations.');");
		   	         out.println("</script>");
	   	        }
	   	    } else {
		   		// No action : display basket
	   			doGet(request, response);
	   	    }
   		}
   		// No action : display basket
   		else {	      
   			doGet(request, response);
   		}
   	}

}
