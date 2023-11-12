<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%@ include file="header.jsp" %>
<body>

	<div
		class="d-flex justify-content-center align-items-center login-container">
		<form method="post" class="login-form text-center" action="Login">
			<div class="form-group">
                <label for="email">E-mail :</label>
                <input type="text" id="email" name="email" class="form-control" required>
            </div>

            <div class="form-group">
                <label for="password">Password :</label>
                <input type="text" id="password" name="password" class="form-control" required>
            </div>

			<button type="submit"
				class="btn-lg btn-custom btn-block text-uppercase">Connexion</button>
			<p class="mt-3 font-weight-normal">
				Vous ne possédez pas de compte ? <a href="./Registration"><strong>Inscrivez-vous</strong></a>
			</p>

		</form>
	</div>

	<%@ include file="footer.jsp"%>
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
		integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
		crossorigin="anonymous">
		
	</script>
	<script
		src="https://cdn.jsdelivr.net/npm/popper.js@1.14.7/dist/umd/popper.min.js"
		integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
		crossorigin="anonymous">
		
	</script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/js/bootstrap.min.js"
		integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
		crossorigin="anonymous">
		
	</script>
</body>