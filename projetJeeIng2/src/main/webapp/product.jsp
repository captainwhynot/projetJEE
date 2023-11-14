<%@page import="org.hibernate.internal.build.AllowSysOut"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%@ include file="header.jsp" %>
<body>

    <div class="d-flex justify-content-center align-items-center page-container">
           <div class="centered">
           <% if (request.getAttribute("productId") != null) {
            ProductDao productDao = new ProductDao(HibernateUtil.getSessionFactory());
            Product product = productDao.getProduct((int) request.getAttribute("productId"));%>
                <div class="card centered" style="width: 18rem;">
				  <img src="<%= product.getImg() %>" class="card-img-top">
				  <div class="card-body">
				    <h5 class="card-title"><%= product.getName() %></h5>
				    <p class="card-text">Price: <%= product.getPrice() %></p>
				    <p class="card-text">Stock: <%= (product.getStock()==0?"Out of Stock.":product.getStock()) %></p>
				    <form method="POST" action="Market" class="card-text">
                           <input type="hidden" name="sellerId" value="<%= product.getModerator().getId() %>">
                       	<button type="submit" style="background: none; padding: 0; font-size: 16px;">Seller: <%= product.getModerator().getUsername() %></button>
                      	</form>
				    <br>
				    <div class="d-flex justify-content-center">
					    <form method="POST" action="Product" class="mr-2">
					      <input type="hidden" name="action" value="addOrder">
					      <input type="hidden" name="productId" value="<%= product.getId() %>">
					      <button type="submit" class="btn btn-success">Add to Basket</button>
					    </form>
					    <form method="POST" action="Market">
					      <button type="submit" class="btn btn-primary">Return to Market</button>
					    </form>
				    </div>
				  </div>
				</div>    
           <% } else { %>
           	<script>showAlert("This product does not exist.", "error", "./Market")</script>
           <% } %>
        </div>
    </div>
    <%@ include file="footer.jsp"%>
</body>

</html>