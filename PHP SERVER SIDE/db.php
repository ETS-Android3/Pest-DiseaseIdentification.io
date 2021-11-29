<?php
	$mysqli = new mysqli("localhost", "id8830190_sihsg", "passwd", "id8830190_db");
	if (mysqli_connect_errno()) {
		printf("Connect failed: %s\n", mysqli_connect_error());
		exit();
	}
	$mysqli -> query("SET character_set_results = 'utf8', character_set_client = 'utf8', character_set_connection = 'utf8', character_set_database = 'utf8', character_set_server = 'utf8'");
?>