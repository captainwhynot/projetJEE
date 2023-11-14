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
                <h1>Credit Card</h1>
                <form method="POST" action="Basket">
                <table class="table">
                        <tr>
	               			<td>Card Number : </td>
	               			<td><input type="text" pattern="\d*" name="cardNumber" required></td>
                        </tr>
                        <tr>
	               			<td>CVV : </td>
	               			<td><input type="text" pattern="\d*" name="cvv" maxlength="3" required></td>
                        </tr>
                        <tr>
	               			<td>Expiration Date : </td>
	               			<td><input type="date" name="expirationDate" required></td>
                        </tr>
	                </table>
	                <input type="hidden" id="action" name="action" value="finalizePaiement">
				    <div style="display: flex;flex-direction: row; column-gap: 10px;">
					    <button type="button" onclick="updateAction('confirmOrder');">Cancel</button>
		              	<button type="button" onclick="updateAction('finalizePaiement');">Confirm</button>
	              	</div>
	              	<button type="submit" id="submitButton" style="display: none">Submit</button>
	          </form>
        </div>
       
    </div>
    </div>

    <%@ include file="footer.jsp"%>
    <script>
	    function updateAction(action) {
	        document.getElementById('action').value = action;
	        document.getElementById('submitButton').click();
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