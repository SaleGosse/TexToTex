<?php 
	
	include 'checkCookie.php';
	include 'connectionDB.php';
	if(isset($_POST['cookie']))
	{	
		$dataBase =  connectionDB();
		$idUserSource = (int)$_POST['idUser'];
		$cookie = (string)$_POST['cookie'];
		if(!checkCookie($dataBase, $idUserSource, $cookie))
		{
			//Printing the error
			echo "false";

			//Closing the db and exiting
			exit();
		}

		if(isset($_POST['idUser']) && isset($_POST['friendName']))
		{
			
			$login = (string)$_POST['friendName'];
			
			$result = $dataBase->prepare("SELECT idUser FROM User WHERE login = :login");
			$result->bindParam(':login', $login, PDO::PARAM_STR);
			$result->execute();
			$idUser = $result->fetchAll(PDO::FETCH_ASSOC);
			$a = $idUser[0]['idUser'];
			$idFriend = $a;

			$rq_insert_friend = "INSERT INTO Friend (idUser, idUserFriend) VALUES (:idUserSource, :idFriend)";
			$request = $dataBase->prepare($rq_insert_friend);
			$request->bindParam(':idUserSource', $idUserSource, PDO::PARAM_INT);
			$request->bindParam(':idFriend', $idFriend, PDO::PARAM_INT);
			$request->execute();
			
			echo "true";
			$db = null;   // Disconnect
		}
	}
	else 
	{
		echo "false";
	}

?>