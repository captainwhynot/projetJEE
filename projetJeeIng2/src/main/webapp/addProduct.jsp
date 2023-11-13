<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%@ include file="header.jsp" %>
<%
if ((loginUser != null && loginUser.getTypeUser().equals("Administrator")) || (loginUser != null && loginUser.getTypeUser().equals("Moderator"))) {
%>
<body>

    <div class="d-flex justify-content-center align-items-center page-container">
        <div class="interieurAddProduct">
            <div class="centered">
                <h1>Formulaire pour ajouter des produits dans la DB</h1>
        </div>
       
    </div>
    </div>


    <%@ include file="footer.jsp"%>
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous">
    </script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.7/dist/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous">
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous">
    </script>

</body>
<%
} else {
%><script type="text/javascript">window.location.replace("./Login");</script>
<%
}
%>
</html>