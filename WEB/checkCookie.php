<?php
	
	function checkCookie($dataB, $userID, $cookie) {		
		$rq = "SELECT idUser FROM User WHERE User.idUser = :userID AND User.cookie = :cookie";

		$dataB = connectionDB();

		$request = $dataB->prepare($rq);
		$request->bindParam(":userID", $userID, PDO::PARAM_INT);
		$request->bindParam(":cookie", $cookie, PDO::PARAM_STR);
		$request->execute();

		$result = $request->fetch();

		if($result)
			return true;

		return false;
	}
?>
