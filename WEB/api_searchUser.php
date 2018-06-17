<?php 
	
	include 'connectionDB.php';

	if(isset($_POST['cookie']))
	{	

			if(isset($_POST['idUser']) && isset($_POST['friendName']))
		 	{
				$dataBase =  connectionDB();

				$cookie = (string)$_POST['cookie'];
		 		$rq_check_cookie = "SELECT idUser FROM User WHERE cookie = :cookie AND idUser = :userID";

				$request = $dataBase->prepare($rq_check_cookie);
				//Checking the cookie, we're never too sure.
				$request->bindParam(':cookie', $cookie, PDO::PARAM_STR);
				$request->bindParam(':userID', $userID, PDO::PARAM_INT);
				$request->execute();

				if($request->fetch())
				{
					//Printing the error
					echo "false";

					//Closing the database
					$dataBase = null;

					//Exiting
					exit();
				}
	
	 			$loginFriend = (string)$_POST['friendName'];
	 			$idUserSource = (int)$_POST['idUser'];
	 			
	 			// take id user with username
				$result = $dataBase->prepare("SELECT idUser FROM User WHERE login = :login");
				$result->bindParam(':login', $loginFriend, PDO::PARAM_STR);
				$result->execute();
				$idUser = $result->fetchAll(PDO::FETCH_ASSOC);
				$a = $idUser[0]['idUser'];
				$idUserFriend = $a;	

	 			// set id friend
				if($idUserSource == $idUserFriend)
				{
					echo "false";
					exit ();
				}

				$result = $dataBase->prepare("SELECT idUserFriend FROM Friend WHERE idUser = :idUser");
				$result->bindParam(':idUser', $loginFriend, PDO::PARAM_STR);
				$result->execute();
				$checkExiste = $result->fetchall();
				if (empty($checkExiste))
				{
					$resultConv = $dataBase->prepare("SELECT ( SELECT count(*)  FROM Message WHERE idUser = :idUserSource) as 'NbMessage' ,(SELECT count(*) FROM linkConversation WHERE idUser = :idUserSource ) AS 'NbConversation' , (select count(idUserFriend) from Friend WHERE idUser = :idUserSource )as 'Friends' ");
					$resultConv->bindParam(':idUserSource', $idUserFriend, PDO::PARAM_INT);
					$resultConv->execute();
					$array1 = $resultConv->fetchall(PDO::FETCH_OBJ);

					//echo json_encode($array1);
					/*$arr = json_decode(json_encode($array1));
 					$arr[1]['code'] = "myProfile";
					$array1 = json_encode($arr);*/

 					echo json_encode($array1); 
				}
				else
				{
		 			// check if already friend
					echo "false";
					exit ();
				}

		 		/*$resultConv = $dataBase->prepare("SELECT ( SELECT count(*)  FROM Message WHERE idUser = :idUser) as 'NbMessage' ,(SELECT count(*) FROM linkConversation WHERE idUser = :idUser ) AS 'NbConversation' , (select count(idUserFriend) from Friend WHERE idUser = :idUser )as 'Friends' ");
				$resultConv->bindParam(':idUser', $idUser, PDO::PARAM_INT);
				$resultConv->execute();
				$array1 = $resultConv->fetchall(PDO::FETCH_OBJ);

				echo json_encode($array1);
				*/
		 	}
	}


?>