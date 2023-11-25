package servlet;

import java.io.IOException;
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
import entity.Basket;
import entity.Customer;
import entity.User;

/**
 * Servlet implementation class ServletBasket
 *
 * This servlet handles operations related to the user's shopping basket, such as viewing, updating, and confirming orders.
 */
@SuppressWarnings("deprecation")
@WebServlet("/Basket")
public class ServletBasket extends HttpServlet {
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
		BasketDao basketDao = new BasketDao(sessionFactory);
		List<Basket> basketList = basketDao.getBasketList(ServletIndex.loginUser(request, response).getId());

		request.setAttribute("basketList", basketList);
		this.getServletContext().getRequestDispatcher("/basket.jsp").include(request, response);
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
        // Get the action from the request (checkStock, deleteOrder, confirmOrder, confirmCreditCard, finalizePaiement)
		String action = request.getParameter("action");

		User loginUser = ServletIndex.loginUser(request, response);
		BasketDao basketDao = new BasketDao(sessionFactory);

		if (action != null) {
			if (action.equals("checkStock")) {
				// Check the stock of the product to update quantity
				try {
					int basketId = Integer.parseInt(request.getParameter("basketId"));
					int quantity = Integer.parseInt(request.getParameter("quantity"));

					Basket basket = basketDao.getBasket(basketId);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					
					if (basketDao.checkStock(basketId, quantity)) {
						if (basketDao.updateQuantity(basket.getId(), quantity - basket.getQuantity())) {
							response.setStatus(HttpServletResponse.SC_OK); // 200 OK
							response.getWriter().write("{\"status\": \"Enough stock.\"}");
						} else {
							response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
							response.getWriter().write("{\"status\": \"Update failed.\"}");
						}
					} else {
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
						response.getWriter().write("{\"status\": \"Insufficient stock.\"}");
					}
				} catch (Exception e) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
					response.getWriter().write("{\"status\": \"Internal Server Error.\"}");
				}
			} else if (action.equals("deleteOrder")) {
				// Delete the order specified
				try {
					int basketId = Integer.parseInt(request.getParameter("basketId"));

					Basket basket = basketDao.getBasket(basketId);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					
					if (basketDao.deleteOrder(basket.getId())) {
						response.setStatus(HttpServletResponse.SC_OK); // 200 OK
						response.getWriter().write("{\"status\": \"Order n°"+basketId+" deleted successfully.\"}");
					} else {
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
						response.getWriter().write("{\"status\": \"Failed to delete order.\"}");
					}
				} catch (Exception e) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
					response.getWriter().write("{\"status\": \"Internal Server Error.\"}");
				}
			} else if (action.equals("confirmOrder")) {
				// Confirm the basket to pay
				List<Basket> basketList = basketDao.getBasketList(ServletIndex.loginUser(request, response).getId());
				request.setAttribute("basketList", basketList);
				
				if (basketList.isEmpty()) {
					this.getServletContext().getRequestDispatcher("/basket.jsp").include(request, response);
					response.getWriter().println("<script>showAlert('Your basket is empty.', 'warning', './Basket');</script>");
				} else {
					basketList = basketDao.confirmOrder(loginUser.getId());
					request.setAttribute("basketList", basketList);
					
					if (basketList.isEmpty()) {
						this.getServletContext().getRequestDispatcher("/basket.jsp").include(request, response);
						response.getWriter().println("<script>showAlert('Your basket is empty.', 'warning', './Basket');</script>");
					} else {
						this.getServletContext().getRequestDispatcher("/confirmOrder.jsp").include(request, response);
					}
				}
			} else if (action.equals("confirmCreditCard")) {
				// Enter the credit card to pay with
				this.getServletContext().getRequestDispatcher("/checkCreditCard.jsp").include(request, response);
			} else if (action.equals("finalizePaiement")) {
				// Finalize paiement
				List<Basket> basketList = basketDao.getBasketList(ServletIndex.loginUser(request, response).getId());
				request.setAttribute("basketList", basketList);
				this.getServletContext().getRequestDispatcher("/basket.jsp").include(request, response);

				// Get the credit card's information from the request
				int cardNumber = Integer.parseInt(request.getParameter("cardNumber"));
				int cvv = Integer.parseInt(request.getParameter("cvv"));
				String expirationDateString = request.getParameter("expirationDate");
				
				String[] expirationDateArray = expirationDateString.split("-");
				int year = Integer.parseInt(expirationDateArray[0]) - 1900;
				int month = Integer.parseInt(expirationDateArray[1]) - 1;
				int day = Integer.parseInt(expirationDateArray[2]);

				Date expirationDate = new Date(year, month, day);
				CreditCardDao creditCardDao = new CreditCardDao(sessionFactory);

				// Verify that the credit card exists, is not expired, and have enough credit to pay
				if (creditCardDao.checkCreditCard(cardNumber, cvv, expirationDate)) {
					if (creditCardDao.checkBalance(cardNumber, basketDao.totalPrice(loginUser.getId()))) {
						// Mail's content
						double totalOrderPrice = 0;
						String container = "<span style='color: black'>Here is your paiement recapitulation :</span><br>";
						container += "<table style='border-collapse: collapse; color: black; text-align: center;' border=1>"
								+ "<thead>" + "<tr>" + "<th>Id</th>" + "<th>Product</th>" + "<th>Price</th>"
								+ "<th>Quantity</th>" + "<th>Seller</th>" + "<th>Total Price</th>" + "</tr>"
								+ "</thead>" + "<tbody>";

						for (Basket basket : basketList) {
							container += "<tr>" + "<td> " + basket.getId() + " </td>" + "<td>"
									+ basket.getProduct().getName() + "</td>" + "<td>" + basket.getProduct().getPrice()
									+ "</td>" + "<td>" + basket.getQuantity() + "</td>" + "<td>"
									+ basket.getProduct().getUser().getUsername() + "</td>";

							double totalPrice = basket.getProduct().getPrice() * basket.getQuantity();
							totalOrderPrice += totalPrice;

							container += "<td>" + String.format("%.2f", totalPrice) + " </td>" + "</tr>";
						}
						CustomerDao customerDao = new CustomerDao(HibernateUtil.getSessionFactory());
						Customer customer = customerDao.getCustomer(loginUser.getId());
						double fidelityPoint = customer.getFidelityPoint();
						String discount = (fidelityPoint > totalOrderPrice) ? String.format("%.2f", totalOrderPrice) : String.format("%.2f", fidelityPoint);
						
						container += "<tr>" + "<td colspan='5'>Total Order before discount</td>" + "<td>"
								+ String.format("%.2f", totalOrderPrice) + "</td>" + "</tr>" + "<tr>"
								+ "<td colspan='5'>Discount</td>" + "<td>" + discount + "</td>" + "</tr>" + "<tr>"
								+ "<td colspan='5'>Total Order Price :</td>" + "<td>"
								+ String.format("%.2f", totalOrderPrice) + "</td>" + "</tr>" + "</tbody>" + "</table><br>";
						container += "<span style='color: black'>Click here to access the site : </span>";
						container += "<a href=\"http://localhost:8080/projetJeeIng2/Index\">MANGASTORE</a>";

						// Finalize the paiement and send the recapitulation mail of paiement
						if (basketDao.finalizePaiement(loginUser.getId(), cardNumber, basketDao.totalPrice(loginUser.getId()), container)) {
							response.getWriter().println("<script>showAlert('Payment completed successfully.', 'success', './Basket');</script>");
						} else {
							response.getWriter().println("<script>showAlert('Payment failed.', 'error', './Basket');</script>");
						}
					} else {
						response.getWriter().println("<script>showAlert('Not enough credit on the credit card.', 'error', './Basket');</script>");
					}
				} else {
					this.getServletContext().getRequestDispatcher("/checkCreditCard.jsp").include(request, response);
					response.getWriter().println("<script>showAlert('The credit card is invalid, please check the informations.', 'error', '');</script>");
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
