<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
<%@ include file="header.jsp" %>
<%
if (isLogged && loginUser.getTypeUser().equals("Customer")) {
%>
<body>

    <div class="d-flex justify-content-center align-items-center page-container">
        <div class="interieurAddProduct">
            <div class="centered">
                <h1>Basket</h1>
                <form method="POST" action="Basket">
                	<% double totalOrderPrice = 0; %>
	                <table class="table">
	                    <thead>
	                        <tr>
	                        	<th>Id</th>
	                        	<th>Image</th>
	                            <th>Product</th>
	                            <th>Price</th>
	                            <th>Quantity</th>
	                            <th>Seller</th>
	                            <th>Total Price</th>
	                        </tr>
	                    </thead>
	                    <tbody>
	                        <% List<Basket> basketList = (List<Basket>) request.getAttribute("basketList");
	                            for (Basket basket : basketList) {
	                        %>
	                            <tr>
	                            	<td><%= basket.getId() %></td>
	                            	<td><img src="<%= basket.getProduct().getImg()%>" style="width: 28px;"></td>
	                                <td><%= basket.getProduct().getName() %></td>
	                                <td><%= basket.getProduct().getPrice() %></td>
	                                <td><%= basket.getQuantity() %></td>
	                                <td><%= basket.getProduct().getModerator().getUsername() %></td>
	                                <% double totalPrice = basket.getProduct().getPrice() * basket.getQuantity(); 
	                                String totalPriceString = String.format("%.2f", totalPrice);
	                                totalOrderPrice += totalPrice;
	                                %>
	                                <td><span class="totalPrice"><%= totalPriceString %></span></td>
	                            </tr>
	                        <%
	                            }
	                        %>
	                        <tr>
		                        <td colspan=6>Total Order before discount</td>
                                <% String totalOrderPriceString = String.format("%.2f", totalOrderPrice); %>
		                        <td><%= totalOrderPriceString %></td>
	                        </tr>
	                        <tr>
		                        <td colspan=6>Discount</td>
		                        <% CustomerDao customerDao = new CustomerDao(HibernateUtil.getSessionFactory());
		                        Customer customer = customerDao.getCustomer(loginUser.getId());
		                        double fidelityPoint = customer.getFidelityPoint();
                                String fidelityPointString = String.format("%.2f", fidelityPoint);
                                String discount = (fidelityPoint > totalOrderPrice) ? totalOrderPriceString : fidelityPointString;
                                %>
		                        <td><%= discount %></td>
	                        </tr>
	                        <tr>
		                        <td colspan=6>Total Order Price :</td>
		                        <% BasketDao basketDao = new BasketDao(HibernateUtil.getSessionFactory());
		                        totalOrderPrice = basketDao.totalPrice(loginUser.getId());
                                totalOrderPriceString = String.format("%.2f", totalOrderPrice); 
                                %>
		                        <td><%= totalOrderPriceString %></td>
	                        </tr>
	                    </tbody>
	                </table>
	                <input type="hidden" id="action" name="action" value="confirmCreditCard">
				    <div style="display: flex;flex-direction: row; column-gap: 10px;">
					    <button type="button" onclick="updateAction('');">Cancel</button>
		              	<button type="button" onclick="updateAction('confirmCreditCard');">Confirm Order</button>
	              	</div>
	              	<button type="submit" id="submitButton" style="display: none">Submit</button>
	          </form>
        </div>
       
    </div>
    </div>


    <%@ include file="footer.jsp"%>
    <script>
	    function updateAction(action) {
	        document.getElementById('action').value = action;
	        document.getElementById('submitButton').click();
	    }
	</script>

</body>
<%
} else {
%><script type="text/javascript">showAlert();</script>
<%
}
%>
</html>