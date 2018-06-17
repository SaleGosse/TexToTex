<?php 
	
	include 'connectionDB.php';

	if(isset($_POST['cookie']))
	{	

			if(isset($_POST['idUser']))
		 	{
				$dataBase =  connectionDB();
				$idUser = isset($_POST['idUser']);
				$result = $dataBase->prepare(" SELECT l.login, v.idUserFriend FROM (SELECT u.login,f.idUserFriend  FROM (SELECT login,idUser FROM User) u JOIN Friend f on f.idUser = u.idUser WHERE f.idUser = :idUser) v JOIN User l on l.idUser = v.idUserFriend ");
				$result->bindParam(':idUser', $idUser, PDO::PARAM_INT);
				$result->execute();
				$checkExiste = $result->fetchall(PDO::FETCH_ASSOC);

				echo json_encode($checkExiste);
				exit();
			}
			echo "false";
			exit ();
	}
	
?>