<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%@ include file="header.jsp" %>
<%
if (isLogged) {
%>
<body>
	<div class="content" id="min-taille">
	<br>
		<div class="card centered" style="width: 18rem;">
			<img src="./img/profilePicture.png" class="card-img-top">
			<div class="card-body">
				<p class="card-text">E-mail : <%= loginUser.getEmail() %></p>
				<p class="card-text">Username : <%= loginUser.getUsername() %></p>
				<br>
			</div>
		</div>
	</div>
    <%@ include file="footer.jsp"%>
</body>
<%
} else {
%><script type="text/javascript">window.location.replace("./Index");</script>
<%
}
%>
</html>