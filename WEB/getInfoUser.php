<?php 
	
	include 'connectionDB.php';

	if(isset($_POST['cookie']))
	{	

			if(isset($_POST['idUser']) || isset($_POST['friendName']))
		 	{
				$dataBase =  connectionDB();
				$cookie = (string)$_POST['cookie'];
		 		$rq_check_cookie = "SELECT idUser FROM User WHERE cookie = :cookie AND idUser = :userID";

				//Checking the cookie, we're never too sure.
				$request = $dataBase->prepare($rq_check_cookie);
				$request->bindParam(':cookie', $cookie, PDO::PARAM_STR);
				$request->bindParam(':userID', $userID, PDO::PARAM_INT);
				$request->execute();
				///$result = $request->fetch();

				if($request->fetch())
				{
					//Printing the error
					echo "false\n";
					echo "error: Invalid cookie.";

					//Closing the database
					$dataBase = null;

					//Exiting
					exit();
				}
			
		 		if(isset($_POST['friendName']))
		 		{
		 			$login = (string)$_POST['friendName'];
					$result = $dataBase->prepare("SELECT idUser FROM User WHERE login = :login");
					$result->bindParam(':login', $login, PDO::PARAM_STR);
					$result->execute();
					$idUser = $result->fetchAll(PDO::FETCH_ASSOC);
					$a = $idUser[0]['idUser'];
					$idUser = $a;

		 		}
		 		else 
		 		{
		 			$idUser = (int)$_POST['idUser'];		 			
		 		}
		 	
		 		$resultConv = $dataBase->prepare("SELECT ( SELECT count(*)  FROM Message WHERE idUser = :idUser) as 'NbMessage' ,(SELECT count(*) FROM linkConversation WHERE idUser = :idUser ) AS 'NbConversation' , (select count(idUserFriend) from Friend WHERE idUser = :idUser )as 'Friends' ");
				$resultConv->bindParam(':idUser', $idUser, PDO::PARAM_INT);
				$resultConv->execute();
				$array1 = $resultConv->fetchall(PDO::FETCH_OBJ);

				echo json_encode($array1);

		 	}
	}


?>