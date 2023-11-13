<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%@ include file="header.jsp" %>
<body>

    <div class="content" id="min-taille">
        <div class="col-4 offset-4">
            <form method="post" action="Market">
            <br>
                <div class="mb-3 centered2">
                    <input type="text" class="form-control" id="category" name="category" placeholder="Produit...">
                    <button type="submit" id="SR">Rechercher</button>
                </div>
                
            </form>           
            <br>

            <!-- Insérer sélection de produit -->
            

        </div>
    </div>

    <%@ include file="footer.jsp"%>
</body>
</html>