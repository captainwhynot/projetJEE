<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="dao.*"%>
<%@ page import="entity.*"%>
<%@ page import="conn.*"%>

<%
User loginUser = null;
if (session.getAttribute("user") != null) {
	loginUser = (User) session.getAttribute("user");
}
%>
<head>
<title>MANGASTORE</title>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/productDetails.css">
<link rel="stylesheet" href="css/market.css">

<link href='https://unpkg.com/boxicons@2.1.2/css/boxicons.min.css'
	rel='stylesheet'>
<link rel="stylesheet"
	href="https://unicons.iconscout.com/release/v4.0.0/css/line.css">
<link rel="stylesheet" type="text/css"
	href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" />
</head>
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
					<form method="post" onsubmit="ServletProduct" action="Product"
						class="mb-3">
						<input type="text" id="category" name="category"
							placeholder="Rechercher..."> <span class="icon"> <i
							class="uil uil-search search-icon"></i> <%
 if (loginUser != null) {
 %>
							<a href="./History"><i><img src="img/historique.png"
									alt="" width="24px"></i></a> <%--<a id="decal" href="./Basket"><i><img src="img/shopping-cart.png" alt="" width="32px"></i></a> --%>
							<a id="decal2" href="./Basket"><i><img
									src="img/shopping-cart.png" alt="" width="32px"></i></a> <%
 }
 %>
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
					<li class="nav-item"><a class="nav-link" href="./Index">Home</a>
					</li>
					<li class="nav-item"><a class="nav-link" href="./Market">Market</a>
					</li>
					<%
					if (loginUser == null) {
					%>
					<li><a class="nav-link" aria-current="page" href="./Login">Login</a></li>
					<%
					} else {
					%>
					<%
					if (loginUser.getTypeUser().equals("Administrator")) {
					%>
					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle" href="#"
						id="navbarDropdownMenuLink" role="button" data-toggle="dropdown"
						aria-haspopup="true" aria-expanded="false">Moderator</a>
						<div class="dropdown-menu"
							aria-labelledby="navbarDropdownMenuLink">
							<a class="dropdown-item" href="./ManageModerator">Manage
								Rights</a> <a class="dropdown-item" href="./AddModerator">Add
								Moderator</a>
						</div></li>
					<li class="nav-item"><a class="nav-link"
						href="./ManageFidelityPoint">Manage Fidelity Points</a></li>
					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle" href="#"
						id="navbarDropdownMenuLink" role="button" data-toggle="dropdown"
						aria-haspopup="true" aria-expanded="false">Products</a>
						<div class="dropdown-menu"
							aria-labelledby="navbarDropdownMenuLink">
							<a class="dropdown-item" href="./ManageProduct">Manage
								Product</a> <a class="dropdown-item" href="./AddProduct">Add
								Product</a>
						</div></li>
					<%
					}
					%>
					<%
					if (loginUser.getTypeUser().equals("Moderator")) {
						ModeratorDao moderatorDao = new ModeratorDao(HibernateUtil.getSessionFactory());
						Moderator moderator = moderatorDao.getModerator(loginUser.getEmail());
					%>
					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle" href="#"
						id="navbarDropdownMenuLink" role="button" data-toggle="dropdown"
						aria-haspopup="true" aria-expanded="false">Products</a>
						<div class="dropdown-menu"
							aria-labelledby="navbarDropdownMenuLink">
							<%
							if (moderator.canModifyProduct() || moderator.canDeleteProduct()) {
							%>
							<a class="dropdown-item" href="./ManageProduct">Manage
								Product</a>
							<%
							}
							if (moderator.canAddProduct()) {
							%>
							<a class="dropdown-item" href="./AddProduct">Add Product</a>
							<%
							}
							%>
							<%
							}
							%>

						</div></li>

					<li><a class="nav-link" aria-current="page" href="./Logout">Logout</a></li>
					<%
					}
					%>
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