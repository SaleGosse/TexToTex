

<?php 
	
	include 'connectDB.php';

	if(isset($_POST['Message']) &&  isset($_POST['userSource']) &&  isset($_POST['UserDestination'])) 
	{
		$dataBase =  connectionDB();

		// define username,password with post 
		$message = $_POST["Message"];
		$userSource = $_POST["userSource"];
		$UserDestination = $_POST['UserDestination'];

		$result = $dataB->prepare("SELECT * FROM  linkConversation WHERE  idUser = :userSource ");
		$result->bindParam(':userSource', $userSource, PDO::PARAM_STR);
		$result->execute();
		$resultUser = $result->fetchAll(PDO::FETCH_ASSOC);

		// check if you have somethings inside array
		if($idUser)
		{
			$idSource = $idUser[0]["idUser"];
			$idDestination = $idUser[0]["idUser"];
			$result = $dataB->prepare("INSERT INTO Message (content, idUserSource, idUserDestinatin) VALUES ( :message, :idSource, :idDestination)");
			$result->bindParam(':message', $message, PDO::PARAM_STR);
			$result->bindParam(':idSource', $idSource, PDO::PARAM_STR);
			$result->bindParam(':idDestination', $idDestination, PDO::PARAM_STR);	
			$result->execute();
		}
		
		$dataBase = null;   // Disconnect

	}
?>