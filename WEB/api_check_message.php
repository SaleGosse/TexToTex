<?php

include 'connectionDB.php';

if(isset($_POST['userID']))
{
	$userID = (int)$_POST['userID'];

	$db = connectionDB();

	//Getting all the unread messages and not notified
	$rq_get_notif = "SELECT ms.idMessageStatus AS id,c.convName,p.firstName,m.content,m.date,m.idConversation AS conversationID FROM MessageStatus ms JOIN Message m ON m.idMessage = ms.idMessage JOIN Conversation c ON m.idConversation = c.idConversation JOIN Profile p ON m.idUser = p.idUser WHERE ms.unread = 1 AND ms.notified = 0 AND ms.idUser = :userID";
	
	$request = $db->prepare($rq_get_notif);
	$request->bindParam(":userID", $userID, PDO::PARAM_INT);
	$request->execute();

	$unread = $request->fetchAll();

	echo "true\n";

	if(!empty($unread))
	{
		//Updating the status of the message
		$rq_update_notif = "UPDATE MessageStatus SET notified = 1 WHERE idMessageStatus = :id";

		$request = $db->prepare($rq_update_notif);

		for ($i=0; $i < count($unread); $i++) { 
			$request->bindParam(":id", $unread[0]['id'], PDO::PARAM_INT);
			$request->execute();
		}

		echo "msg: " . json_encode($unread) . " :msg\n";
	}

	//Checking if gotta send aes keys
	$rq_get_inv = "SELECT i.idInvitation,i.idConversation,i.idTarget,l.pubExp,l.modulus FROM Invitation i LEFT JOIN linkConversation l ON i.idTarget = l.idUser WHERE i.idUser = :userID AND i.isOK = 1 AND i.tmpAes IS NULL";

	$request = $db->prepare($rq_get_inv);
	$request->bindParam(":userID", $userID, PDO::PARAM_INT);
	$request->execute();

	$invits = $request->fetchAll();

	if(!empty($invits))
	{
		echo "inv: " . json_encode($invits) . " :inv\n";
	}

	//Checking if we received aes keys
	$rq_get_aes = "SELECT idInvitation,idUser,idConversation,tmpAes AS \"key\" FROM Invitation WHERE idTarget = :userID AND isOK = 1 AND tmpAes IS NOT NULL";

	$request = $db->prepare($rq_get_aes);
	$request->bindParam(":userID", $userID, PDO::PARAM_INT);
	$request->execute();

	$newkey = $request->fetchAll();

	if(!empty($newkey))
	{
		$rq_del_aes = "DELETE FROM Invitation WHERE idInvitation = :invitationID";
		
		$request = $db->prepare($rq_del_aes);
		
		for ($i=0; $i < count($newkey); $i++) { 
			$request->bindParam(":invitationID", $newkey[$i]["idInvitation"], PDO::PARAM_INT);
			$request->execute();
		}

		echo "aes: " . json_encode($newkey) . " :aes\n";
	}

	$db = null;

}
else 
	echo "error: Missing POST parameters.";


?>