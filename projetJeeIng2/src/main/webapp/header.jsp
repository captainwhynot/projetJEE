<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<header>
	<!-- navbar top -->
	<div class="container">
		<div class="navbar-top">
			<div class="social-link">
				<a href="./Index"><i><img src="img/logo.png" alt=""
						width="90"></i></a>
			</div>
			<div class="logo">
				<h3>MANGASTORE</h3>
			</div>
			<div class="icons">
				<i><img src="" alt="" width="20px"></i>
				<div class="input-box">
					<form method="post" onsubmit="ServletProduct" action="Product" class="mb-3">
						<input type="text" id="category" name="category" placeholder="Rechercher..."> 
						<span class="icon"> <i class="uil uil-search search-icon"></i>
							<%-- SI UTILISATEUR CONNECTER --%>
                			<% if (session.getAttribute("user") != null) { %>
							<a href="../deliveries/php/history.php"><i><img src="img/historique.png" alt="" width="24px"></i></a>
							<%--<a id="decal" href="basket.php"><i><img src="img/shopping-cart.png" alt="" width="32px"></i></a> --%> 
							<a id="decal2" href="basket.php"><i><img src="img/shopping-cart.png" alt="" width="32px"></i></a>
							<% } %>
						</span> <i class="uil uil-times close-icon"></i>
					</form>
				</div>
		</div>
	</div>
	</div>
	<!-- navbar top -->

	<!-- main content -->

	<nav class="navbar navbar-expand-md" id="navbar-color">
		<div class="container">
			<!-- Toggler/collapsible Button -->
			<button class="navbar-toggler" type="button" data-toggle="collapse"
				data-target="#collapsibleNavbar">
				<span><i><img src="img/menu.png" alt="" width="30px"></i></span>
			</button>

			<!-- Navbar links -->
			<div class="collapse navbar-collapse" id="collapsibleNavbar">
				<ul class="navbar-nav">
					<li class="nav-item"><a class="nav-link" href="./Index">Accueil</a>
					</li>
					<li class="nav-item"><a class="nav-link" href="./Market">Produits</a>
					</li>
					<%-- SI UTILISATEUR DÉCONNECTÉ --%>
                <% if (session.getAttribute("user") == null) { %>
                    <li><a class="nav-link" aria-current="page" href="./Login">Connexion</a></li>
                <% } %>

                <%-- SINON AFFICHER PANIER + BOUTON DÉCONNEXION --%>
                <% if (session.getAttribute("user") != null) { %>
                    <li><a class="nav-link" aria-current="page" href="./Logout">Déconnexion</a></li>
                <% } %>
				</ul>
			</div>
		</div>
	</nav>

	<script>
        let inputBox = document.querySelector(".input-box"),
            searchIcon = document.querySelector(".icon"),
            closeIcon = document.querySelector(".close-icon");
        searchIcon.addEventListener("click", () => inputBox.classList.add("open"));
        closeIcon.addEventListener("click", () => inputBox.classList.remove("open"));
    </script>
</header>