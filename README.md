PROJET CY TECH ING2 GSI 1 JEE 
CAI Christophe
HADDACHE Noamann
OUALI Abd-ennour 
LEMBA Mohamed

EXECUTER LE CODE:
-L'ensemble des données de la BDD (table + contenu) sont instanciées au démarrage du main
ces étapes sont nécessaires au bon fonctionnement du site, veuillez s'il vous plait bien les suivre:
	
	-Pour lancer le projet:
		-créer une BDD mySQL du nom de "bdd_projet_jee", la connexion se fait avec le nom d'utilisateur "root" et un mot de passe vide	
		-si jamais la connexion échoue, la configuration de la connexion à la BDD s'effectue dans le fichier HibernateUtil.java (dans le package conn du projet)
		-pour instancier la BDD + données, il y a besoin de lancer le fichier Main.java (une seule fois suffit)
		-exécuter index.jsp pour lancer le site (dans le package webapp, port 8080 de base) ou aller sur l'url : "localhost:8080/projetJeeIng2/Index"


-Pour se connecter en tant qu'admin, vous devez utiliser les identifiants suivants:
    -nom utilisateur: mailAdmin
    -mot de passe: password
Pour se connecter en tant que modo :
    -nom utilisateur: mailModo
    -mot de passe: password

-Pour tester les autres fonctionnalités des autres utilisateurs, vous pouvez directement créer un compte depuis le menu du site et vous connecter à votre nouveau compte.
-Un compte créé est de base un compte de client.
-Un Admin peut transformer un client en tant que modérateur, il n'y a pas de création de compte directe pour un modérateur.
-L'ensemble des fonctionnalités propre à chaque type d'utilisateur seront accessibles directement depuis la barre de menu dans le header du site.


SI VOUS AVEZ UN PROBLEME AVEC LE PROJET:
-contactez LEMBA Mohamed / CAI Christophe / HADDACHE Noaman / OUALI Abd-Ennour sur teams OU lembamoham@cy-tech.fr / caichristo@cy-tech.fr / haddacheno@cy-tech.fr / ouaaliabde@cy-tech.fr par mail
