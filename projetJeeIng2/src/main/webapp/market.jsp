<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.1/css/all.min.css"/>
<%@ include file="header.jsp" %>
<body>

    <div class="content" id="min-taille">
        <div class="col-4 offset-4">
            <form method="post" action="Market">
            <br>
                <div class="mb-3 centered2">
                    <input type="text" class="form-control" id="category" name="category" placeholder="Produit...">
                    <button type="submit" id="SR">Rechercher</button>
                </div>
            </form>        
            <br>
           	<table class="table">
            	<thead>
                    <tr>
                        <th>Image</th>
                        <th>Name</th>
                        <th>Price</th>
                        <th>Stock</th>
                        <th>Seller</th>
                        <th>See details</th>
                    </tr>
                </thead>
                <tbody>
                    <% List<Product> productList = (List<Product>) request.getAttribute("productList");
                        for (Product product : productList) {
                    %>
                        <tr>
                            <td><img src="<%= product.getImg() %>" style="width: 28px;" alt="img/ProductImage.png"></img>
                            <td><%= product.getName() %></td>
                            <td><%= product.getPrice() %></td>
                            <td><%= product.getStock() %></td>
                            <td><form method="POST" action="Market">
                                <input type="hidden" name="sellerId" value="<%= product.getModerator().getId() %>">
                            	<button type="submit" style="background: none; padding: 0; font-size: 16px; color: red;"><%= product.getModerator().getUsername() %></button>
                           	</form></td>
                            <td>
                            <form method="POST" action="Product">
                            	<input type="hidden" name="productId" value="<%= product.getId() %>">
                            	<button type="submit" style="padding : 0; background : none;" ><i class="fas fa-eye"></i></button>
                           	</form></td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>

    <%@ include file="footer.jsp"%>
</body>
</html>