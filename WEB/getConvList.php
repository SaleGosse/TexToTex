<?php
	
	include 'checkCookie.php';

	if(isset($_POST['userID']) && isset($_POST['cookie']))
	{
		include 'connectionDB.php';
		//Check the cookie
		$userID = (int)$_POST['userID'];
		$cookie = $_POST['cookie'];

		$dataB = connectionDB();
		
		if(!checkCookie($dataB, $userID, $cookie))
		{
			//Printing the error
			echo "false\n" . "error: Invalid cookie.";

			//Closing the db and exiting
			$dataB = null;
			exit();
		}

		//Check the user id and get his convs
		$rq = "SELECT L.idConversation, C.convName AS name, (SELECT content FROM Message WHERE idConversation = L.idConversation ORDER BY date DESC LIMIT 1) AS content, C.lastDate AS date, IF(EXISTS(SELECT ms.idMessageStatus FROM MessageStatus ms JOIN Message m ON m.idMessage = ms.idMessage WHERE ms.idUser = L.idUser AND m.idConversation = L.idConversation AND ms.unread = 1), 'true', 'false') unread  FROM linkConversation L JOIN Conversation C ON L.idConversation = C.idConversation WHERE L.idUSer = :idUser ORDER BY C.lastDate DESC";

		$request = $dataB->prepare($rq);
		$request->bindParam(":idUser", $userID, PDO::PARAM_INT);

		$request->execute();

		$result = $request->fetchAll();

		echo "true\n";

		if(!empty($result))
			echo json_encode($result) . "\n";
	}
	else
		echo "false\n" . "error: Missing POST parameters.\n";

?>
