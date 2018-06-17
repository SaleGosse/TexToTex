<?php 
	
	include 'connectionDB.php';

	if(isset($_POST['username']) && isset($_POST['password']) && isset($_POST['lastName']) && isset($_POST['firstName'])) 
	{
		$dataBase =  connectionDB();
		$lastName = $_POST['lastName'];
		$firstName = $_POST['firstName'];
		$username = $_POST['username'];//(int)$_POST["idSource"];
		$password =  hash("sha256",$_POST["password"]);//(int)$_POST['idConversation'];
		$mail = $_POST['mail'];

		$resultConv = $dataBase->prepare("SELECT login FROM User WHERE login = :username");
		$resultConv->bindParam(':username', $username, PDO::PARAM_STR);
		$resultConv->execute();

		//check if usernam existe in BDD
		if($resultConv->fetch(PDO::FETCH_ASSOC))
		{
			echo "false";
		}
		else
		{
			$resultConv = $dataBase->prepare("INSERT INTO User (login, password,cookie, pub_key) VALUES (:username , :password, 'NULL','NULL')");
			$resultConv->bindParam(':username', $username, PDO::PARAM_STR);
			$resultConv->bindParam(':password', $password, PDO::PARAM_STR);
			$resultConv->execute();

			$result = $dataBase->prepare("SELECT idUser FROM User WHERE login = :login");
			$result->bindParam(':login', $username, PDO::PARAM_STR);
			$result->execute();
			$idUser = $result->fetchAll(PDO::FETCH_ASSOC);
			$a = $idUser[0]['idUser'];
			$idUser = $a;

			$resultConv = $dataBase->prepare("INSERT INTO Profile (idUser,lastName,firstName,phoneNumber) VALUES (:idUser,:lastName,:firstName,'NULL')");
			$resultConv->bindParam(':idUser', $idUser, PDO::PARAM_INT);
			$resultConv->bindParam(':lastName', $lastName, PDO::PARAM_STR);
			$resultConv->bindParam(':firstName', $firstName, PDO::PARAM_STR);
			$resultConv->execute();

			echo "true";
		}
	}
	else
	{
		$dataBase = null;   // Disconnect
		echo "false";
	}
?>