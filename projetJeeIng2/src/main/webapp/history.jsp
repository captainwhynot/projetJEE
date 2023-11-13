<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
                <h1>Formulaire pour gérer l'historique d'achat dans la DB</h1>
        </div>
       
    </div>
    </div>


    <%@ include file="footer.jsp"%>

</body>
<%
} else {
%><script type="text/javascript">showAlert();</script>
<%
}
%>

</html>