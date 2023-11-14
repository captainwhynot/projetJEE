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
import dao.CustomerDao;
import dao.UserDao;
import entity.Basket;
import entity.Customer;
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
   		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
   		String action = request.getParameter("action");
   		
   		User loginUser = ServletIndex.loginUser(request, response);
		BasketDao basketDao = new BasketDao(sessionFactory);
		UserDao userDao = new UserDao(sessionFactory);
		
		if (action != null && action.equals("finalizePaiement")) {
			this.getServletContext().getRequestDispatcher("/header.jsp").include(request, response);
			PrintWriter out = response.getWriter();

   	   		String cardNumberString = request.getParameter("cardNumber");
   	   		int cardNumber = Integer.parseInt(cardNumberString);
   	   		String userIdString = request.getParameter("userId");
   	   		int userId = Integer.parseInt(userIdString);
   			if (basketDao.finalizePaiement(userId, cardNumber, basketDao.totalPrice(userId))) {
	   	         out.println("<script>");
	   	         out.println("showAlert('Payment completed successfully.', 'success', './Basket');");
	   	         out.println("</script>");
	   	         return;
   			}
   			else {
	   	         out.println("<script>");
	   	         out.println("showAlert('Payment failed.', 'error', './Basket');");
	   	         out.println("</script>");
	   	         return;
   			}
        } 
		
		if (!ServletIndex.isLogged(request, response)) {
			response.sendRedirect("./Index");
			return;
		}
		
   		if (action != null) {
			PrintWriter out = response.getWriter();
	   	    if (action.equals("checkStock")) {
	   	        try {
	   	        	String basketIdString = request.getParameter("basketId");
		   	        String quantityString = request.getParameter("quantity");
		   	        
		   	        int basketId = Integer.parseInt(basketIdString);
		   	        int quantity = Integer.parseInt(quantityString);
		   	        
		            Basket basket = basketDao.getBasket(basketId);
		   	        response.setContentType("application/json");
		            response.setCharacterEncoding("UTF-8");
	
			   	    if (basketDao.checkStock(basketId, quantity)) {
			   	    	basketDao.updateQuantity(basket.getId(), quantity - basket.getQuantity());
			   	    	response.setStatus(HttpServletResponse.SC_OK); // 200 OK
			            response.getWriter().write("{\"status\": \"Enough stock.\"}");
			        } else {
			            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
			            response.getWriter().write("{\"status\": \"Insufficient stock.\"}");
			        }
	   	        } catch (Exception e) {
		   	         response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
		   	         response.getWriter().write("{\"status\": \"Internal Server Error.\"}");
	   	     }
	   	    } else if (action.equals("confirmOrder")){
	   	    	List<Basket> basketList = basketDao.getBasketList(ServletIndex.loginUser(request, response).getId());
	   	    	request.setAttribute("basketList", basketList);
	   	    	if (basketList.isEmpty()) {
	   	    		
	   	    		this.getServletContext().getRequestDispatcher("/basket.jsp").include(request, response);
	   	    		out.println("<script>");
   			        out.println("showAlert('Your basket is empty.', 'warning', './Basket');");
   			        out.println("</script>");
	   	    	} else {
		   			basketList = basketDao.confirmOrder(loginUser.getId());
		   			request.setAttribute("basketList", basketList);
		   			if (basketList.isEmpty()) {
		   				this.getServletContext().getRequestDispatcher("/basket.jsp").include(request, response);
		   	    		out.println("<script>");
	   			        out.println("showAlert('Your basket is empty.', 'warning', './Basket');");
	   			        out.println("</script>");
		   	    	} else {
			   	    	this.getServletContext().getRequestDispatcher("/confirmOrder.jsp").include(request, response);
		   	    	}
	   	    	}
	   			
	   	    } else if (action.equals("confirmCreditCard")) {
	   	    	this.getServletContext().getRequestDispatcher("/checkCreditCard.jsp").include(request, response);
	   	    } else if (action.equals("sendConfirmationMail")) {
				List<Basket> basketList = basketDao.getBasketList(ServletIndex.loginUser(request, response).getId());
			    request.setAttribute("basketList", basketList);
	   	    	this.getServletContext().getRequestDispatcher("/basket.jsp").include(request, response);
   				
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
		   			if (creditCardDao.checkBalance(cardNumber, basketDao.totalPrice(loginUser.getId()))) {
		   				String container = "<span style='color: black'>Here is your paiement recapitulation :</span><br>";
		   				double totalOrderPrice = 0;
		   				container += "<table style='border-collapse: collapse; color: black; text-align: center;' border=1>" +
		   				                "<thead>" +
		   				                    "<tr>" +
		   				                        "<th>Id</th>" +
		   				                        "<th>Product</th>" +
		   				                        "<th>Price</th>" +
		   				                        "<th>Quantity</th>" +
		   				                        "<th>Seller</th>" +
		   				                        "<th>Total Price</th>" +
		   				                    "</tr>" +
		   				                "</thead>" +
		   				                "<tbody>";

		   				for (Basket basket : basketList) {
		   				    container += "<tr>" +
		   				                    "<td> " + basket.getId() + " </td>" + 
		   				                    "<td>" + basket.getProduct().getName() + "</td>" +
		   				                    "<td>" + basket.getProduct().getPrice() + "</td>" +
		   				                    "<td>" + basket.getQuantity() + "</td>" +
		   				                    "<td>" + basket.getProduct().getModerator().getUsername() + "</td>";

		   				    double totalPrice = basket.getProduct().getPrice() * basket.getQuantity();
		   				    totalOrderPrice += totalPrice;

		   				    container += "<td>" + String.format("%.2f", totalPrice) + " </td>" +
		   				                "</tr>";
		   				}
		   				CustomerDao customerDao = new CustomerDao(HibernateUtil.getSessionFactory());
                        Customer customer = customerDao.getCustomer(loginUser.getId());
                        double fidelityPoint = customer.getFidelityPoint();
                        String discount = (fidelityPoint > totalOrderPrice) ? String.format("%.2f", totalOrderPrice) : String.format("%.2f", fidelityPoint);
		   				container += "<tr>" +
		   				                "<td colspan='5'>Total Order before discount</td>" +
		   				                "<td>" + String.format("%.2f", totalOrderPrice) + "</td>" +
		   				            "</tr>" +
		   				            "<tr>" +
		   				                "<td colspan='5'>Discount</td>" +
		   				                "<td>" + discount + "</td>" +
		   				            "</tr>" +
		   				            "<tr>" +
		   				                "<td colspan='5'>Total Order Price :</td>" +
		   				                "<td>" + String.format("%.2f", totalOrderPrice) + "</td>" +
		   				            "</tr>" +
		   				        "</tbody>" +
		   				    "</table>";
		   				container += "<span style='color: black'>Click below here to confirm your paiement :</span><br>";
		   				container += "<form method=\"POST\" action=\"http://localhost:8080/projetJeeIng2/Basket\">" +
		   								"<input type=\"hidden\" id=\"action\" name=\"action\" value=\"finalizePaiement\">" +
		   								"<input type=\"hidden\" id=\"userId\" name=\"userId\" value=\""+ loginUser.getId() +"\">" +
		   								"<input type=\"hidden\" id=\"cardNumber\" name=\"cardNumber\" value=\""+ cardNumber +"\">" +
		   								"<button type=\"submit\">" +
		   									"Confirm Paiement" +
		   								"</button>" +
		   							 "</form>";
		   				if (userDao.sendMail(loginUser.getEmail(), "MANGASTORE : Confirm paiement", container)) {
				   	         out.println("<script>");
				   	         out.println("showAlert('A confirmation mail has been sent.', 'info', './Basket');");
				   	         out.println("</script>");
				   	         return;
		   				} else {
		   			        out.println("<script>");
		   			        out.println("showAlert('Confirmation mail didn't send well.', 'warning', './Basket');");
		   			        out.println("</script>");
		   	   	         return;
		   				}
		   			} else {
			   	         out.println("<script>");
			   	         out.println("showAlert('Not enough credit on the credit card.', 'error', './Basket');");
			   	         out.println("</script>");
			   	         return;
		   			}
		   	    } else {
		   	    	 this.getServletContext().getRequestDispatcher("/checkCreditCard.jsp").include(request, response);
		   	         out.println("<script>");
		   	         out.println("showAlert('The credit card is invalid, please check the informations.', 'error', '');");
		   	         out.println("</script>");
		   	         return;
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
