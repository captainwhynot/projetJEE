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
	                            	<td><span class="id"><%= basket.getId() %></span></td>
	                                <td><a href="./Product"><%= basket.getProduct().getName() %></a>
                                	<input type="hidden" name="productId" value="<%= basket.getProduct().getId() %>"></td>
	                                <td><span class="price"><%= basket.getProduct().getPrice() %></span></td>
	                                <td><input type="number" onchange="checkStock(this)" class="quantity" value="<%= basket.getQuantity() %>">
	                                <input type="hidden" class="oldQuantity" value="<%= basket.getQuantity() %>"></td>
	                                <td><a href="./Market"><%= basket.getProduct().getModerator().getUsername() %></a>
                                	<input type="hidden" name="sellerId" value="<%= basket.getProduct().getModerator().getId() %>"></td>
	                                <% double total = basket.getProduct().getPrice() * basket.getQuantity(); 
	                                String totalString = String.format("%.2f", total);
	                                totalOrderPrice += total;
	                                %>
	                                <td><span class="totalPrice"><%= totalString %></span></td>
	                            </tr>
	                        <%
	                            }
	                        %>
	                        <tr>
		                        <td colspan=4>Total Order Price :</td>
		                        <% String totalOrderPriceString = String.format("%.2f", totalOrderPrice); %>
		                        <td><span class="totalOrderPrice"><%= totalOrderPriceString %></span></td>
	                        </tr>
	                    </tbody>
	                </table>
	                <input type="hidden" name="action" value="confirmOrder">
	              	<button type="submit">Pay</button>
	          </form>
        </div>
       
    </div>
    </div>


    <%@ include file="footer.jsp"%>
    <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
    <script>
	    function checkStock(input) {
	        var oldQuantity = input.closest('tr').querySelector('.oldQuantity').value;
	  	 	var quantity = input.value;
	    	if (quantity < 0) {
	    		input.value = oldQuantity
	    	}
	    	else {
		        var price = input.closest('tr').querySelector('.price').textContent;
		        var basketId = input.closest('tr').querySelector('.id').textContent;
	
		        $.ajax({
		            type: "POST",
		            url: "Basket",
		            data: { basketId: basketId, quantity: quantity, action: "checkStock" },
		            dataType: "json",
		            success: function (response) {
		            },
		            error: function (jqXHR, textStatus, errorThrown) {
		            	showAlert("Error " + jqXHR.status + ": " + jqXHR.responseJSON.status, "error", "./Basket");
		            	input.value = oldQuantity;
						quantity = oldQuantity;
		            }
		        });
		        input.closest('tr').querySelector('.totalPrice').textContent = (price * quantity).toFixed(2);
		        totalPrice();
	    	}
    	}
	    
	    function totalPrice() {
	        var totalOrderPrice = 0;
	        var totalPriceElements = document.querySelectorAll('.totalPrice');

	        totalPriceElements.forEach(function(element) {
	            totalOrderPrice += parseFloat(element.textContent);
	        });

	        document.querySelector('.totalOrderPrice').textContent = totalOrderPrice.toFixed(2);
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