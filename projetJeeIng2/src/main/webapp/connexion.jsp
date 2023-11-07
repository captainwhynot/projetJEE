<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Se Connecter</title>
</head>
<body>
    <form method="post" action="connexion" >
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
</html>