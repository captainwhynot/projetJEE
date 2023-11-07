<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Creation compte client</title>
    <!-- Inclure Bootstrap CSS (assurez-vous d'avoir lié Bootstrap CSS dans votre projet) -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">

</head>
<body>
<h1>Connexion</h1>
    <div class="container">
        <form method="post" action="creationCompte" class="mt-4">
            <div class="form-group">
                <label for="nom">Nom :</label>
                <input type="text" id="nom" name="nom" class="form-control" required>
            </div>

            <div class="form-group">
                <label for="prenom">Prenom :</label>
                <input type="text" id="prenom" name="prenom" class="form-control" required>
            </div>

            <div class="form-group">
                <label>Type :</label>
                <div class="form-check">
                    <input type="radio" id="user1" name="typeUser" value="user1" class="form-check-input" checked>
                    <label for="user1" class="form-check-label">User1</label>
                </div>

                <div class="form-check">
                    <input type="radio" id="user2" name="typeUser" value="user2" class="form-check-input">
                    <label for="user2" class="form-check-label">User2</label>
                </div>
            </div>

            <button type="submit" class="btn btn-primary">Envoyer</button>
        </form>
    </div>
</body>
</html>


<%-- <%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Créer compte (client)</title>
</head>
<body>
    <form method="post" action="creationCompte" >
        <label for="nom">Nom :</label>
        <input type="text" id="nom" name="nom" required><br>

        <label for="age">Prenom :</label>
        <input type="text" id="prenom" name="prenom" required><br>

        <label>Type :</label><br>
        <input type="radio" id="user1" name="typeUser" value="user1" checked>
        <label for="user1">User1</label><br>

        <input type="radio" id="user2" name="typeUser" value="user2">
        <label for="user2">User2</label><br>

        <input type="submit" value="Envoyer">
    </form>
</body>
</html> --%>