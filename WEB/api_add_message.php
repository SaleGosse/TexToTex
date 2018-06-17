<?php 
	
	//The function to check the cookie
	include 'checkCookie.php';

	if(isset($_POST['userID']) && isset($_POST['cookie']))
	{	
		//The function to connect to the DB
		include 'connectionDB.php';
		
		//Getting the POST params
		$userID = (int)$_POST['userID'];
		$cookie = $_POST['cookie'];

		//Connection to the DB
		$db = connectionDB();
		
		if(!checkCookie($db, $userID, $cookie))
		{
			//Printing the error
			echo "false\n" . "error: Invalid cookie.\n";

			//Closing the db and exiting
			$db = null;
			exit();
		}
		
		if(isset($_POST['content']) && isset($_POST['conversationID']))
		{
			//Getting the POST params
			$content = $_POST['content'];
			$conversationID = $_POST['conversationID'];

			//The current date in an accurate format
			$currentDate = date('Y-m-d H:i:s', time());

			// Inserting the message in server 
			$rq_insert_message = "INSERT INTO Message (idConversation,  idUser, content,date) VALUES (:conversationID, :userID, :content, :currentDate)";
			$request = $db->prepare($rq_insert_message);
			$request->bindParam(':conversationID', $conversationID, PDO::PARAM_INT);
			$request->bindParam(':userID', $userID, PDO::PARAM_INT);
			$request->bindParam(':content', $content, PDO::PARAM_STR);
			$request->bindParam(':currentDate', $currentDate, PDO::PARAM_STR);
			$request->execute();

			//Updating the last date in the conv
			$rq_update_date = "UPDATE Conversation SET lastDate = :dateDB WHERE idConversation = :conversationID";
			$dateDB = date('d/m/Y H:i', time());

			$request = $db->prepare($rq_update_date);
			$request->bindParam(':dateDB', $dateDB, PDO::PARAM_STR);
			$request->bindParam(':conversationID', $conversationID, PDO::PARAM_INT);
			$request->execute();

			// take id Message
			$rq_get_message_ID = "SELECT idMessage FROM Message WHERE idConversation = :conversationID AND idUser = :userID AND date = :currentDate";
			$request = $db->prepare($rq_get_message_ID);
			$request->bindParam(':conversationID', $conversationID, PDO::PARAM_INT);
			$request->bindParam(':userID', $userID, PDO::PARAM_INT);
			$request->bindParam(':currentDate', $currentDate, PDO::PARAM_STR);
			$request->execute();

			$messageID = $request->fetch();
			$messageID = $messageID['idMessage'];

			//Get all the users in a conv
			$rq_get_conversation_users = "SELECT idUser FROM linkConversation l WHERE l.idConversation = :conversationID AND l.idUser != :userID";
			$request = $db->prepare($rq_get_conversation_users);
			$request->bindParam(':conversationID', $conversationID, PDO::PARAM_INT);
			$request->bindParam(':userID', $userID, PDO::PARAM_INT);
			$request->execute();

			$users = $request->fetchAll(PDO::FETCH_ASSOC);
			
			//Inserting the notification
			$rq_add_notif = "INSERT INTO MessageStatus (idUser,idMessage,unread,notified) VALUES (:userID,:messageID,1,0)";
			$request = $db->prepare($rq_add_notif);

			for ($i=0; $i < count($users); $i++) {
				$request->bindParam(':userID', $users[$i]['idUser'], PDO::PARAM_INT);
				$request->bindParam(':messageID', $messageID, PDO::PARAM_INT);

				$request->execute();
			}

			echo "true";

			$db = null;   // Disconnect
		}
		else 
			echo "error: Invalid or missing POST parameters.\n";
	}
	else
		echo "error: Missing cookie.\n";
?>