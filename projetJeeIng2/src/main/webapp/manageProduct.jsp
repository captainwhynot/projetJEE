<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.1/css/all.min.css"/>
<%@ include file="header.jsp" %>
<%
if (isLogged && (loginUser.getTypeUser().equals("Administrator") || loginUser.getTypeUser().equals("Moderator")) ) {
%>
<body>
    <div class="d-flex justify-content-center align-items-center page-container">
        <div class="interieurAddProduct">
            <div class="centered">
                <h1>Product List</h1>
                <form method="post" action="./ManageProduct">
	                <table class="table">
	                    <thead>
	                        <tr>
	                            <th>Image</th>
	                            <th>Name</th>
	                            <th>Price</th>
	                            <th>Stock</th>
	                            <th>Seller</th>
	                            <th>See details</th>
	                            <th>Delete Product</th>
	                        </tr>
	                    </thead>
	                    <tbody>
	                        <% List<Product> productList = (List<Product>) request.getAttribute("productList");
	                            for (Product product : productList) {
	                        %>
	                            <tr>
	                                <td><img src="<%= product.getImg() %>" style="width: 28px;" alt="img/ProductImage.png"></img>
	                                <input type="hidden" name="img" value="<%= product.getImg() %>"></td>
	                                <td><input type="text" name="name" value="<%= product.getName() %>"></td>
	                                <td><input type="number" name="price" value="<%= product.getPrice() %>" step="0.01"></td>
	                                <td><input type="number" name="stock" value="<%= product.getStock() %>"></td>
	                                <td>
	                                    <a href="./Market"><%= product.getModerator().getUsername() %></a>
	                                    <input type="hidden" name="sellerId" value="<%= product.getModerator().getId() %>">
	                                </td>
	                                <td><button type="button" style="padding : 0; background : none;" onclick="seeDetails(<%= product.getId() %>)"><i class="fas fa-eye"></i></button></td>
	                                <td><button type="button" style="padding : 0; background : none;" onclick="deleteProduct(<%= product.getId() %>)"><i class="fas fa-trash"></i></button></td>
	                            </tr>
	                        <% } %>
	                    </tbody>
	                </table>
	                <input type="hidden" name="action" value="updateProduct">
	                <button type="submit">Confirm</button>
                </form>
            </div>
        </div>
    </div>
    <%@ include file="footer.jsp"%>
    <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
    <script>
	    function seeDetails(productId) {
	        var form = document.createElement('form');
	        form.method = 'post';
	        form.action = './Product';
	
	        var productIdInput = document.createElement('input');
	        productIdInput.type = 'hidden';
	        productIdInput.name = 'productId';
	        productIdInput.value = productId;
	
	        form.appendChild(productIdInput);
	
	        document.body.appendChild(form);
	        form.submit();
	    }
	    
	    function deleteProduct(productId) {
	    	$.ajax({
	            type: "POST",
	            url: "ManageProduct",
	            data: { productId: productId, action: "deleteProduct" },
	            dataType: "json",
	            success: function (response) {
	            	showAlert("Success : " + response.status, "success", "./ManageProduct");
	            },
	            error: function (jqXHR, textStatus, errorThrown) {
	            	showAlert("Error " + jqXHR.status + ": " + jqXHR.responseJSON.status, "error", "./ManageProduct");
	            }
	        });
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