<?php
	
	function connectionDB()
	{
		//connection with data base
		$host_name = "localhost";
		$admin_name = "root";
		$admin_pass = "pass";
		$db_name = "privacyDB";

		try
		{
			//creat data base connection with pdo
			$dataDB = new PDO("mysql:host=$host_name;dbname=$db_name", $admin_name, $admin_pass);

			// set the PDO error mode to exception
			$dataDB->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
			//echo "Connected successfully";
		}
		catch(PDOException $e)
		{
			echo "Connection failed: " . $e->getMessage();
		}
		return $dataDB;
	}
?>