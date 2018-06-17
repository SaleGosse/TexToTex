<?php

include 'connectionDB.php';

if(isset($_POST['userID']))
{
	$userID = $_POST['userID'];

	$db = connectionDB();

	$rq_get_notif = "SELECT ms.idMessageStatus AS id,c.convName,p.firstName,m.content,m.date,m.idConversation AS conversationID FROM MessageStatus ms JOIN Message m ON m.idMessage = ms.idMessage JOIN Conversation c ON m.idConversation = c.idConversation JOIN Profile p ON m.idUser = p.idUser WHERE ms.unread = 1 AND ms.notified = 0 AND ms.idUser = :userID";
	$rq_update_notif = "UPDATE MessageStatus SET notified = 1 WHERE idMessageStatus = :id";

	$request = $db->prepare($rq_get_notif);
	$request->bindParam(":userID", $userID, PDO::PARAM_INT);
	$request->execute();

	$unread = $request->fetchAll();
	$request = $db->prepare($rq_update_notif);


	for ($i=0; $i < count($unread); $i++) { 
		$request->bindParam(":id", $unread[0]['id'], PDO::PARAM_INT);
		$request->execute();
	}

	echo "true\n" . json_encode($unread) . "\n";

}
else 
	echo "error: Missing POST parameters.";


?>