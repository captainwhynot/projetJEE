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
			//Finalize paiement via mail click
			this.getServletContext().getRequestDispatcher("/header.jsp").include(request, response);

			int cardNumber = Integer.parseInt(request.getParameter("cardNumber"));
			int userId = Integer.parseInt(request.getParameter("userId"));
			
			if (basketDao.finalizePaiement(userId, cardNumber, basketDao.totalPrice(userId))) {
				response.getWriter().println("<script>showAlert('Payment completed successfully.', 'success', './Basket');</script>");
			} else {
				response.getWriter().println("<script>showAlert('Payment failed.', 'error', './Basket');</script>");
			}
			return;
		}

		if (!ServletIndex.isLogged(request, response)) {
			response.sendRedirect("./Index");
			return;
		}

		if (action != null) {
			if (action.equals("checkStock")) {
				//Check the stock to update quantity
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
			} else if (action.equals("confirmOrder")) {
				//Confirm the basket to pay
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
				//Enter the credit card to pay with
				this.getServletContext().getRequestDispatcher("/checkCreditCard.jsp").include(request, response);
			} else if (action.equals("sendConfirmationMail")) {
				//Send mail to finalize paiement
				this.getServletContext().getRequestDispatcher("/basket.jsp").include(request, response);
				List<Basket> basketList = basketDao.getBasketList(ServletIndex.loginUser(request, response).getId());
				request.setAttribute("basketList", basketList);

				int cardNumber = Integer.parseInt(request.getParameter("cardNumber"));
				int cvv = Integer.parseInt(request.getParameter("cvv"));
				String expirationDateString = request.getParameter("expirationDate");
				
				String[] expirationDateArray = expirationDateString.split("-");
				int year = Integer.parseInt(expirationDateArray[0]) - 1900;
				int month = Integer.parseInt(expirationDateArray[1]) - 1;
				int day = Integer.parseInt(expirationDateArray[2]);

				Date expirationDate = new Date(year, month, day);
				CreditCardDao creditCardDao = new CreditCardDao(sessionFactory);

				if (creditCardDao.checkCreditCard(cardNumber, cvv, expirationDate)) {
					if (creditCardDao.checkBalance(cardNumber, basketDao.totalPrice(loginUser.getId()))) {
						//Mail's container
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
								+ String.format("%.2f", totalOrderPrice) + "</td>" + "</tr>" + "</tbody>" + "</table>";
						container += "<span style='color: black'>Click below here to confirm your paiement :</span><br>";
						container += "<form method=\"POST\" action=\"http://localhost:8080/projetJeeIng2/Basket\">"
								+ "<input type=\"hidden\" id=\"action\" name=\"action\" value=\"finalizePaiement\">"
								+ "<input type=\"hidden\" id=\"userId\" name=\"userId\" value=\"" + loginUser.getId()
								+ "\">" + "<input type=\"hidden\" id=\"cardNumber\" name=\"cardNumber\" value=\""
								+ cardNumber + "\">" + "<button type=\"submit\">" + "Confirm Paiement" + "</button>"
								+ "</form>";
						
						if (userDao.sendMail(loginUser.getEmail(), "MANGASTORE : Confirm paiement", container)) {
							response.getWriter().println("<script>showAlert('A confirmation mail has been sent.', 'info', './Basket');</script>");
						} else {
							response.getWriter().println("<script>showAlert('Confirmation mail didn't send well.', 'warning', './Basket');</script>");
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
