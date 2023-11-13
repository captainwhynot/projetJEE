<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
<%@ include file="header.jsp" %>
<body>

    <div class="d-flex justify-content-center align-items-center page-container">
        <div class="interieurAddProduct">
            <div class="centered">
                <h1>Credit Card</h1>
                <form method="POST" action="Basket">
                <table class="table">
                        <tr>
	               			<td>Card Number : </td>
	               			<td><input type="text" pattern="\d*" name="cardNumber"></td>
                        </tr>
                        <tr>
	               			<td>CVV : </td>
	               			<td><input type="text" pattern="\d*" name="cvv" maxlength="3"></td>
                        </tr>
                        <tr>
	               			<td>Expiration Date : </td>
	               			<td><input type="date" name="expirationDate"></td>
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
	<script src="https://code.jquery.com/jquery-3.3.1.min.js">
	</script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.7/dist/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous">
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous">
    </script>
    <script>
	    function updateAction(action) {
	        document.getElementById('action').value = action;
	        document.getElementById('submitButton').click();
	    }
	</script>

</body>

</html>