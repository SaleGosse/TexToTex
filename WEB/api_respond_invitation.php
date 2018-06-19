<?php

	include 'checkCookie.php';

	if(isset($_POST['userID']) && isset($_POST['cookie']))
	{
		include 'connectionDB.php';

		//Getting the POST params
		$userID = (int)$_POST["userID"];	
		$cookie = (string)$_POST['cookie'];

		$db =  connectionDB();
		
		if(!checkCookie($db, $userID, $cookie))
		{
			//Printing the error
			echo "false\n" . "error: Invalid cookie.\n";

			//Closing the db and exiting
			$db = null;
			exit();
		}

		if(isset($_POST["conversationID"]) && isset($_POST["invitationID"]) && isset($_POST["action"]))
		{
			$action = (string)$_POST["action"];
			$conversationID = (int)$_POST["conversationID"];
			$invitationID = (int)$_POST["invitationID"];
			
			//Getting the id of the invitation submitter
			$rq_get_fromID = "SELECT idUser FROM Invitation WHERE idInvitation = :invitationID";
			$request = $db->prepare($rq_get_fromID);

			$request->bindParam("invitationID", $invitationID, PDO::PARAM_INT);
			$request->execute();

			$fromID = $request->fetch();
			$fromID = $fromID['idUser'];

			//Accepting the invitation
			if($action == "accept")
			{
				if(isset($_POST["modulus"]) && isset($_POST["exponent"]))
				{
					$modulus = $_POST["modulus"];
					$exponent = $_POST["exponent"];

					//Setting isOK to 1
					$rq_update_invit = "UPDATE Invitation SET isOK = 1 WHERE idInvitation = :invitationID";
					$request = $db->prepare($rq_update_invit);

					$request->bindParam(":invitationID", $invitationID, PDO::PARAM_INT);
					
					$request->execute();

					//Inserting our rsa key in linkConversation
					$rq_insert_modulus = "UPDATE linkConversation SET pubExp = :exponent, modulus = :modulus WHERE idUser = :userID AND idConversation = :conversationID";
					$request = $db->prepare($rq_insert_modulus);

					$request->bindParam(":exponent", $exponent, PDO::PARAM_STR);
					$request->bindParam(":modulus", $modulus, PDO::PARAM_STR);
					$request->bindParam(":userID", $userID, PDO::PARAM_INT);
					$request->bindParam(":conversationID", $conversationID, PDO::PARAM_INT);

					$request->execute();

					//Debug:
					$rq_del_invit = "DELETE FROM Invitation WHERE idInvitation = :invitationID";
					$request = $db->prepare($rq_del_invit);
					$request->bindParam(":invitationID", $invitationID, PDO::PARAM_INT);

					$request->execute();
				}
				else 
				{
					echo "false\n" . "error: Missing POST parameters.\n";

					$db = null;
					exit();
				}
			}
			else if ($action == "decline")
			{
				//Deleting the links in linkConversation
				$rq_del_link = "DELETE FROM linkConversation WHERE idUser = :userID AND idConversation = :conversationID";
				$request = $db->prepare($rq_del_link);

				$request->bindParam(":conversationID", $conversationID, PDO::PARAM_INT);

				//For us
				$request->bindParam(":userID", $userID, PDO::PARAM_INT);
				$request->execute();

				//For the issuer of the invit
				$request->bindParam(":userID", $fromID, PDO::PARAM_INT);
				$request->execute();

				//Deleting the conversation
				$rq_del_conv = "DELETE FROM Conversation WHERE idConversation = :conversationID";
				$request = $db->prepare($rq_del_conv);
				$request->bindParam(":conversationID", $conversationID, PDO::PARAM_INT);
				$request->execute();

				//Deleting the invitation
				$rq_del_invit = "DELETE FROM Invitation WHERE idInvitation = :invitationID";
				$request = $db->prepare($rq_del_invit);
				$request->bindParam(":invitationID", $invitationID, PDO::PARAM_INT);
				$request->execute();

				echo "true\n";
			}
			else
				echo "false\n" . "error: Unhandled action.\n";


		}
		else
			echo "error: Missing POST parameters.\n";

		$db = null;
	}
	else
		echo "false\n" . "error: Invalid or missing cookie.\n";


?>