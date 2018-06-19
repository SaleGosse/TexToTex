<?php
	
	include 'checkCookie.php';

	if(isset($_POST['userID']) && isset($_POST['cookie']))
	{
		include 'connectionDB.php';

		//Getting the POST params
		$userID = (int)$_POST["userID"];	
		$cookie = (string)$_POST['cookie'];

		$dataBase =  connectionDB();
		
		if(!checkCookie($dataBase, $userID, $cookie))
		{
			//Printing the error
			echo "error: Invalid cookie.\n";

			//Closing the db and exiting
			$dataBase = null;
			exit();
		}

		//Now we can get the messages
		if(isset($_POST['conversationID'])) 
		{
			$conversationID = $_POST['conversationID'];

			//Check if it's an invitation
			$rq_check_invit = "SELECT i.idInvitation,p.firstName,p.lastName,u.login FROM Invitation i LEFT JOIN Profile p ON p.idUser = i.idUser JOIN User u ON p.idUser = u.idUser WHERE i.idTarget = :userID AND i.idConversation = :conversationID AND isOK = 0";
			$request = $dataBase->prepare($rq_check_invit);
			$request->bindParam(":userID", $userID, PDO::PARAM_INT);
			$request->bindParam(":conversationID", $conversationID, PDO::PARAM_INT);
			$request->execute();

			$invitation = $request->fetch();

			if(!empty($invitation))
			{
				echo "true\n" . "type: 2\n" . 
					"invitationID: " . $invitation['idInvitation'] . "\n" .
					"firstName: " . $invitation['firstName'] . "\n" .
					"lastName: " . $invitation['lastName'] . "\n" .
					"login: " . $invitation['login'] . "\n";

				$dataBase = null;

				exit();
			}

			//Else, get the messages
			$rq_get_msg = "SELECT m.idMessage,m.idUser,p.lastName,p.firstName,m.content,m.date FROM Message m Left JOIN (SELECT idUser FROM linkConversation WHERE idConversation = :idConversation) c ON m.idUser = c.idUSer JOIN Profile p  ON m.idUser = p.idUser WHERE idConversation = :idConversation ORDER BY m.date ASC";
			$resultConv = $dataBase->prepare($rq_get_msg);
			$resultConv->bindParam(':idConversation', $conversationID, PDO::PARAM_INT);
			$resultConv->execute();
			$resultMessage = $resultConv->fetchAll(PDO::FETCH_ASSOC);

			$rq_update_unread = "DELETE FROM MessageStatus WHERE idMessageStatus IN (SELECT idMessageStatus FROM (SELECT ms.idMessageStatus FROM MessageStatus ms JOIN Message m ON ms.idMessage = m.idMessage WHERE m.idConversation = :conversationID AND m.idUser = :userID) subquery)";

			$request = $dataBase->prepare($rq_update_unread);
			$request->bindParam(':conversationID', $idConversation, PDO::PARAM_INT);
			$request->bindParam(':userID', $userID, PDO::PARAM_INT);
			$request->execute();

			echo "true\n" . "type: 1\n";
			if(!empty($resultMessage))	
			{			
				echo json_encode($resultMessage) . "\n";
			}
		}
		else
			echo "error: Invalid or missing POST parameters.\n";
	}
	else
		echo "error: Invalid or missing cookie.\n";
?>