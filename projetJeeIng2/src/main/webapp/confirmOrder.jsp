<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
<%@ include file="header.jsp" %>
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
	                                <td><%= basket.getProduct().getName() %></td>
	                                <td><%= basket.getProduct().getPrice() %></td>
	                                <td><%= basket.getQuantity() %></td>
	                                <td><%= basket.getProduct().getModerator().getUsername() %></td>
	                                <% double total = basket.getProduct().getPrice() * basket.getQuantity(); 
	                                String totalPrice = String.format("%.2f", total);
	                                totalOrderPrice += total;
	                                %>
	                                <td><span class="totalPrice"><%= totalPrice %></span></td>
	                            </tr>
	                        <%
	                            }
	                        %>
	                        <tr>
	                        <td colspan=4>Total Order Price :</td>
	                        <td><%= totalOrderPrice %></td>
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
	<script src="https://code.jquery.com/jquery-3.3.1.min.js">
	</script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.7/dist/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous">
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous">
    </script>
    <script>
	    function updateAction(action) {
	        document.getElementById('action').value = action;
	        document.getElementById('submitButton').click();
	    }
	</script>

</body>

</html>