<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.1/css/all.min.css"/>
<%@ include file="header.jsp" %>
<body>

    <div class="content" id="min-taille">
        <div class="centered">
            <form method="post" action="Market">
            <br>
                <div class="mb-3 centered2">
                    <input type="text" class="form-control" id="search" name="search" placeholder="Search...">
                    <button type="submit" id="SR">Search</button>
                </div>
            </form>        
            <br>
            <% List<Product> productList = (List<Product>) request.getAttribute("productList");
            int size = productList.size();%>
            <% if (request.getAttribute("search") != null) { %>
	            <div> <%= size %> result<%= (size > 1 ? "s" : "") %> for "<%= request.getAttribute("search") %>".
	            </div>
            <% }
            else if (request.getAttribute("sellerId") != null) {
            	int sellerId = (int) request.getAttribute("sellerId");
            	UserDao userDao = new UserDao(HibernateUtil.getSessionFactory());
	            User user = userDao.getUser(sellerId);%>
	            <div> <%= size %> result<%= (size > 1 ? "s" : "") %> for <%= user.getUsername() %>'s products.
	            </div>
            <% } else { %>
            	<div> All products
	            </div>
            <% } %>
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
                    <% for (Product product : productList) { %>
                        <tr>
                            <td><img src="<%= product.getImg() %>" style="height: 28px;"></img></td>
                            <td><form method="POST" action="Product">
                                <input type="hidden" name="productId" value="<%= product.getId() %>">
                            	<button type="submit" style="background: none; padding: 0; font-size: 16px; color: #007bff;"><%= product.getName() %></button>
                           	</form></td>
                            <td><%= product.getPrice() %></td>
                            <td><%= (product.getStock()==0?"Out of Stock.":product.getStock()) %></td>
                            <td><form method="POST" action="Market">
                                <input type="hidden" name="sellerId" value="<%= product.getUser().getId() %>">
                            	<button type="submit" style="background: none; padding: 0; font-size: 16px; color: #007bff;"><%= product.getUser().getUsername() %></button>
                           	</form></td>
                            <td>
                            <form method="POST" action="Product">
                            	<input type="hidden" name="productId" value="<%= product.getId() %>">
                            	<button type="submit" style="padding: 0; background: none;" ><i class="fas fa-eye"></i></button>
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