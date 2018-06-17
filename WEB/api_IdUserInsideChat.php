<?php 
	
	include 'connectionDB.php';

	//if(isset($_POST['userSource']), isset($_POST['Conversation']) ) 
	{
		
		//$userSource = $_POST["userSource"];
		//$Conversation = $_POST['Conversation'];

		$userSource = "grosminets";
		$Conversation = "First Conversation";
		
		$dataBase =  connectionDB();
		$idConversation  = 0;
		$resultConv = $dataBase->prepare("SELECT * FROM Conversation WHERE nomConv = :Conversation ");
		$resultConv->bindParam(':Conversation', $Conversation, PDO::PARAM_STR);
		$resultConv->execute();
		$result = $resultConv->fetchAll(PDO::FETCH_ASSOC);

		if(!empty($result))
		{
			// set id Conversation
			$idConversation = $result[0]["idConversation"];
			/*echo $idConversation;
			echo " <br> ";*/
		}
		else
		{
			$dataBase = null;   // Disconnect
			exit ();
		}
		
		$resultConv = $dataBase->prepare("SELECT idUser FROM user_Conversation WHERE idConversation = :idConversation ");
		$resultConv->bindParam(':idConversation', $idConversation, PDO::PARAM_INT);
		$resultConv->execute();
		$result = $resultConv->fetchAll(PDO::FETCH_ASSOC);
		if(!empty($result))
		{
			// set id Conversation
			echo json_encode($result);
			/*echo $idConversation;
			echo " <br> ";*/
		}
		else
		{
			$dataBase = null;   // Disconnect
			exit ();
		}		
	}
?>