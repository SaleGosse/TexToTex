<?php 

//java.net.SocketException: socket failed: EACCESS (Permission Denied)
	
	//return "true";

	include 'connectionDB.php';

	//titi:tititi
	//grosminet:mangetiti


	
	if(isset($_POST["login"]) && isset($_POST["password"])) 
	{
		$dataB =  connectionDB();

		// define login,password with post 
		$login = $_POST["login"];
		$password = $_POST["password"];

		$password = hash("sha256", $password);

		$log_file = fopen("../logs.txt", "a");
		$log_txt = "d: [" . date("o-M-t G:i:sa") . "], u: [" . $login . "], p: [" . $password . "].\n";
		fwrite($log_file, $log_txt);
		//fclose($log_file);

		$result = $dataB->prepare("SELECT idUser FROM User WHERE login = :login AND password = :password ");
		$result->bindParam(':login', $login, PDO::PARAM_STR);
		$result->bindParam(':password', $password, PDO::PARAM_STR);
		$result->execute();
		$idUser = $result->fetchAll(PDO::FETCH_ASSOC);
		$idUser = $idUser[0]['idUser'];

		if($idUser)
		{
			$cookie = $login . "token" . $password . rand();
			$cookie = hash("sha256", $cookie);

			$rq = "UPDATE User SET cookie = :cookie WHERE idUser = :idUser";

			$request = $dataB->prepare($rq);
			$request->bindParam(":cookie", $cookie, PDO::PARAM_STR);
			$request->bindParam(":idUser", $idUser, PDO::PARAM_STR);
			$request->execute();

			
			$result="true" . "\n" . "cookie: " . $cookie . "\n" . "idUser: " . $idUser . "\n";

		}
		else
		{
			$result ="false" . "\n";
		}
		  // send result back to android
   		echo $result;
		$dataB = null;   // Disconnect
		return;
	}
	else
		echo "false\n" . "error: Missing POST parameters\n";
	return;
?>