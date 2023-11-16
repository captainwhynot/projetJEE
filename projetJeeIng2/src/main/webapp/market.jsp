<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="entity.Product" %>
<%@ page import="dao.UserDao" %>
<%@ page import="entity.User" %>
<%@ page import="conn.HibernateUtil" %>
<!DOCTYPE html>
<html>
<head>
    <title>Market</title>
    <link rel="stylesheet" href="path/to/your/css/style.css"/> <!-- Assurez-vous de lier votre CSS ici -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.1/css/all.min.css"/>
    <style>
    .product-grid {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-around;
    }
    .product-item {
        flex-basis: 16%; /* Ajustez cette valeur pour changer la largeur des vignettes */
        box-shadow: 0 0 10px rgba(0,0,0,0.1); /* Optionnel: pour ajouter une ombre */
        margin: 10px;
        padding: 15px;
        text-align: center;
    }
    .product-item img {
        max-width: 100%; /* Assure que l'image ne dépasse pas sa vignette */
        height: auto;
    }
    .pagination {
        display: flex;
        justify-content: center; /* Alignement horizontal au centre */
        align-items: center; /* Alignement vertical au centre */
        padding: 10px 0; /* Espace au-dessus et en dessous de la pagination */
        list-style: none; /* Enlève les puces pour les éléments de liste, si vous utilisez une liste */
    }

    .pagination a {
        margin: 0 5px; /* Espacement entre les liens */
        padding: 5px 10px; /* Padding à l'intérieur des liens */
        background-color: #000; /* Couleur de fond pour l'élément actif */
        color: white; /* Couleur du texte pour l'élément actif */
        border-color: #000; /* Couleur de la bordure pour l'élément actif */
        text-decoration: none; /* Enlève le soulignement des liens */
    }

    .pagination a.active {
        background-color: rgba(238, 224, 208);; /* Couleur de fond pour l'élément actif */
        color: black; /* Couleur du texte pour l'élément actif */
        border-color: rgba(238, 224, 208);; /* Couleur de la bordure pour l'élément actif */
    }
</style>

</head>
<body>
    <%@ include file="header.jsp" %>

    <div class="content" id="min-taille">
        <div class="centered">
            <!-- Formulaire de recherche -->
            <form method="post" action="Market">
                <div class="mb-3 centered2">
                    <input type="text" class="form-control" id="search" name="search" placeholder="Search...">
                    <button type="submit" id="SR">Search</button>
                </div>
            </form>        

            <!-- Résultats de recherche ou filtre par vendeur -->
            <% 
            List<Product> productList = (List<Product>) request.getAttribute("productList");
            int size = productList.size();
            if (request.getAttribute("search") != null) {
            %>
                <div> <%= size %> result<%= (size > 1 ? "s" : "") %> for "<%= request.getAttribute("search") %>".
                </div>
            <% } else if (request.getAttribute("sellerId") != null) {
                int sellerId = (int) request.getAttribute("sellerId");
                UserDao userDao = new UserDao(HibernateUtil.getSessionFactory());
                User user = userDao.getUser(sellerId);%>
                <div> <%= size %> result<%= (size > 1 ? "s" : "") %> for <%= user.getUsername() %>'s products.
                </div>
            <% } else { %>
                <div> All products
                </div>
            <% } %>

            <!-- Affichage des produits en vignettes -->
            <div class="product-grid">
                <% 
                for (Product product : productList) { 
                %>
                    <div class="product-item">
                        <img src="<%= product.getImg() %>" alt="<%= product.getName() %>" class="product-image">
                        <h3><%= product.getName() %></h3>
                        <p>Price: <%= product.getPrice() %></p>
                        <p>Stock: <%= product.getStock() == 0 ? "Out of Stock" : product.getStock() %></p>
                        <p>Seller: <%= product.getUser().getUsername() %></p>
                        <!-- Bouton pour voir les détails du produit -->
                        <form method="POST" action="Product">
                            <input type="hidden" name="productId" value="<%= product.getId() %>">
                            <button type="submit" style="padding: 0; background: none;" >
                                <i class="fas fa-eye" style="color: #007bff;"></i> See details
                            </button>
                        </form>
                    </div>
                <% } %>
            </div>

            <!-- Pagination -->
            <div class="pagination">
                <% 
                int totalPages = (int) request.getAttribute("totalPages");
                int currentPage = (int) request.getAttribute("currentPage");
                for (int i = 1; i <= totalPages; i++) { 
                %>
                    <a href="Market?page=<%= i %>" class="<%= i == currentPage ? "active" : "" %>"><%= i %></a>
                <% } %>
            </div>
        </div>
    </div>

    <%@ include file="footer.jsp"%>
</body>
</html>
