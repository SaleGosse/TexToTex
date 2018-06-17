<?php 
	
	include 'connectionDB.php';

	if(isset($_POST['username']) && isset($_POST['password']) && isset($_POST['lastName']) && isset($_POST['firstName'])) 
	{
		$dataBase =  connectionDB();
		$lastName = $_POST['lastName'];
		$firstName = $_POST['firstName'];
		$username = $_POST['username'];
		$password =  hash("sha256",$_POST["password"]);
		
		$request = $dataBase->prepare("SELECT login FROM User WHERE login = :username");
		$request->bindParam(':username', $username, PDO::PARAM_STR);
		$request->execute();

		//check if usernam existe in BDD
		if($request->fetch(PDO::FETCH_ASSOC))
		{
			echo "false\n" . "error: Someone stole your username.\n";

			$dataBase = null;

			exit();
		}

		$request = $dataBase->prepare("INSERT INTO User (login, password,cookie, pub_key) VALUES (:username , :password, 'NULL','NULL')");
		$request->bindParam(':username', $username, PDO::PARAM_STR);
		$request->bindParam(':password', $password, PDO::PARAM_STR);
		$request->execute();

		$request = $dataBase->prepare("SELECT idUser FROM User WHERE login = :login");
		$request->bindParam(':login', $username, PDO::PARAM_STR);
		$request->execute();

		$idUser = $request->fetchAll(PDO::FETCH_ASSOC);
		$idUser = $idUser[0]['idUser'];

		$resultConv = $dataBase->prepare("INSERT INTO Profile (idUser,lastName,firstName,phoneNumber) VALUES (:idUser,:lastName,:firstName,'NULL')");
		$resultConv->bindParam(':idUser', $idUser, PDO::PARAM_INT);
		$resultConv->bindParam(':lastName', $lastName, PDO::PARAM_STR);
		$resultConv->bindParam(':firstName', $firstName, PDO::PARAM_STR);
		$resultConv->execute();

		echo "true";
	}
	else
	{
		// Disconnect
		$dataBase = null;   

		echo "false\n" . "error: All fields are required.\n";

		exit();
	}
?>