<?php 
	
	include 'connectionDB.php';

	if(isset($_POST['userID'])) // && isset($_POST['idConversation'])) 
	{
		$dataBase =  connectionDB();

		$userID = (int)$_POST["userID"];
		//$idConversation = 1;//(int)$_POST['idConversation'];

		$status = "true";
		$resultConv = $dataBase->prepare("SELECT lastName, firstName,c.idUser,c.content,c.date, c.idMessage FROM (SELECT m.idUser,m.content,m.date,id.idConversation, m.idMessage FROM Message m JOIN (SELECT * FROM statusMessage WHERE idConversation = :idConversation AND idUser = :idUserSource AND status = 'true') id ON m.idMessage = id.idMessage ) c JOIN Profile p  ON c.idUser = p.idUser WHERE c.idConversation = :idConversation ORDER BY c.date ASC");
		$resultConv->bindParam(':idConversation', $idConversation, PDO::PARAM_INT);
		$resultConv->bindParam(':idUserSource', $idUserSource, PDO::PARAM_INT);
		$resultConv->bindParam(':status', $status, PDO::PARAM_STR);
		$resultConv->execute();
		$resultMessage = $resultConv->fetchAll(PDO::FETCH_ASSOC);

		//var_dump($resultMessage);
		
		// check if you have somethings inside array
		if(!empty($resultMessage))	
		{
			foreach ($resultMessage as $key => $arr) 
			{
				// $arr[3] will be updated with each value from $arr...
	    		//echo "{$key} => {}";
	    		//var_dump($arr);
	    		$ouais =  date('m/d/Y H:i:s', $arr["date"]);
	    		$idMessage = $arr["idMessage"];
	    	
	    		$resultMessage[$key]["date"] = $ouais;

				$result = $dataBase->prepare("UPDATE statusMessage SET status = 'flase' WHERE idConversation = :idConversation AND idUser = :idUserSource AND  idMessage = :idMessage");
				$result->bindParam(':idConversation', $idConversation, PDO::PARAM_INT);
				$result->bindParam(':idUserSource', $idUserSource, PDO::PARAM_INT);
				$result->bindParam(':idMessage', $idMessage, PDO::PARAM_INT);
				$result->execute();
	    	}
			//echo json_encode($resultMessage);
			echo "true";
		}
		else
		{

			$dataBase = null;   // Disconnect
			echo "false";
		}
	}
	else
	{
		$dataBase = null;   // Disconnect
		exit ();
		echo "false";
	}
?>