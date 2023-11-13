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
                <h1>History</h1>
                <table class="table">
                    <thead>
                        <tr>
                        	<th>Id</th>
                            <th>Product</th>
                            <th>Price</th>
                            <th>Quantity</th>
                            <th>Seller</th>
                            <th>Total Price</th>
                            <th>Purchase Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% List<Basket> basketList = (List<Basket>) request.getAttribute("basketList");
                            for (Basket basket : basketList) {
                        %>
                            <tr>
                            	<td><%= basket.getId() %></td>
                                <td><%= basket.getProduct().getName() %></td>
                                <% double price = basket.getProduct().getPrice();
                                String priceString = String.format("%.2f", price);
                                %>
                                <td><%= priceString %></td>
                                <td><%= basket.getQuantity() %></td>
                                <td><%= basket.getProduct().getModerator().getUsername() %></td>
                                <% double total = basket.getProduct().getPrice() * basket.getQuantity(); 
                                String totalString = String.format("%.2f", total);
                                %>
                                <td><%= totalString %></td>
                                <td><%= basket.getPurchaseDate() %></td>
                            </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
                <input type="hidden" name="action" value="confirmOrder">
        </div>
       
    </div>
    </div>

    <%@ include file="footer.jsp"%>
</body>
<%
} else {
%><script type="text/javascript">showAlert();</script>
<%
}
%>
</html>