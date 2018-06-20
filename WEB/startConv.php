<?php
	include 'connectionDB.php';

	if(isset($_POST["userID"]) && isset($_POST["cookie"]))
	{
		include 'checkCookie.php';

		$cookie = $_POST["cookie"];
		$userID = $_POST["userID"];
		
		$dataB =  connectionDB();

		if(!checkCookie($dataB, $userID, $cookie))
		{
			echo "false\n" . "error: Invalid cookie.\n";

			$dataB = null;

			exit();
		}

		if(isset($_POST["target"]) && isset($_POST["convName"]) && isset($_POST["modulus"]) && isset($_POST["exponent"]))
		{
			$targetLogin = $_POST["target"];
			$convName = $_POST["convName"];
			$modulus = $_POST["modulus"];
			$exponent = $_POST["exponent"];

			//Check if user exists
			$rq_check_user = "SELECT idUser FROM User WHERE User.login = :target";

			$request = $dataB->prepare($rq_check_user);
			$request->bindParam(":target", $targetLogin, PDO::PARAM_STR);
			$request->execute();

			$result = $request->fetchAll();

			//If we didnt find the user
			if(!$result)
			{
				echo "false\n" . "error: User " . $targetLogin . " doesn't exist.";

				$dataB = null;
				
				exit();
			}

			//Check if it's not ourself
			if($result[0]["idUser"] === $userID)
			{
				echo "false\n" . "error: You can't chat with yourself.";

				$dataB = null;

				exit();
			}

			$targetID = $result['0']['idUser'];

			//Checking if we dont allready have exactly the same conv
			$rq_check_conv = "SELECT C.idConversation FROM linkConversation l LEFT JOIN Conversation C ON l.idConversation = C.idConversation WHERE C.convName = :convName AND l.idUSer = :userID";
			$request = $dataB->prepare($rq_check_conv);
			$request->bindParam(":convName", $convName, PDO::PARAM_STR);
			$request->bindParam(":userID", $userID, PDO::PARAM_INT);
			$request->execute();

			$result = $request->fetchAll();

			if($result)
			{	
				echo "false\n" . "error: You are allready in a conversation with this name.\n";

				$dataB = null;

				exit();
			}

			$creationDate = date("d/m/Y", time());
			$lastDate = date('d/m/Y H:i', time());

			//Creating the conversation
			$rq_insert_conv = "INSERT INTO Conversation (convName, creationDate, lastDate) VALUES (:convName, :creationDate, :lastDate)";

			$request = $dataB->prepare($rq_insert_conv);
			$request->bindParam(":convName", $convName, PDO::PARAM_STR);
			$request->bindParam(":creationDate", $creationDate, PDO::PARAM_STR);
			$request->bindParam(":lastDate", $lastDate, PDO::PARAM_STR);

			$request->execute();

			//Retrieve the conversation id
			$rq_get_convID = "SELECT idConversation FROM Conversation WHERE convName = :convName AND creationDate = :creationDate AND lastDate = :lastDate";
			
			$request = $dataB->prepare($rq_get_convID);
			
			$request->bindParam(":convName", $convName, PDO::PARAM_STR);
			$request->bindParam(":creationDate", $creationDate, PDO::PARAM_STR);
			$request->bindParam(":lastDate", $lastDate, PDO::PARAM_STR);

			$request->execute();

			$convID = $request->fetch();
			$convID = $convID['idConversation'];

			//Adding the links for this conversation
			$rq_insert_link = "INSERT INTO linkConversation (idUser, idConversation, pubExp, modulus) VALUES (:userID, :conversationID, :exponent, :modulus)";

			//Creating the request, we'll use it twice
			$request = $dataB->prepare($rq_insert_link);
			$request->bindParam(":conversationID", $convID, PDO::PARAM_INT);
			
			//For us
			$request->bindParam(":userID", $userID, PDO::PARAM_INT);
			$request->bindParam(":exponent", $exponent, PDO::PARAM_STR);
			$request->bindParam(":modulus", $modulus, PDO::PARAM_STR);
			$request->execute();

			//For the target
			$exponent = $modulus = "";
			$request->bindParam(":userID", $targetID, PDO::PARAM_INT);
			$request->bindParam(":exponent", $exponent, PDO::PARAM_STR);
			$request->bindParam(":modulus", $modulus, PDO::PARAM_STR);
			$request->execute();

			//Inserting the invitation
			$rq_insert_invit = "INSERT INTO Invitation (idUser,idConversation,isOK,idTarget,notified) VALUES (:userID, :conversationID, 0, :targetID, 0)";

			$request = $dataB->prepare($rq_insert_invit);
			$request->bindParam(":userID", $userID, PDO::PARAM_INT);
			$request->bindParam(":conversationID", $convID, PDO::PARAM_INT);
			$request->bindParam(":targetID", $targetID, PDO::PARAM_INT);
			$request->execute();

			echo "true\n";
			echo "convID: " . $convID . "\n";
		}
		else
			echo "false.\n" . "error: Missing POST parameter.";

		$dataB = null;

		exit();
	}
	else
		echo "false\n" . "error: Missing cookie.\n";

?>