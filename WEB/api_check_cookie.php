<?php
	
	include 'checkCookie.php';

	if(isset($_POST["userID"]) && isset($_POST["cookie"]))
	{
		include 'connectionDB.php';
	
		$dataB = connectionDB();
	
		$login = $_POST["userID"];
		$cookie = $_POST["cookie"];
	
		if(checkCookie($dataB, $login, $cookie))
			echo "true\n"; 
		else
			echo "false\n";
	
		$dataB = null;
	}
	else
		echo "false\n" . "error: Missing POST parameters.\n";

?>