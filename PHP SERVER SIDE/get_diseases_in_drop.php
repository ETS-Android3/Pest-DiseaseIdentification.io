<?php
    $crop=$_GET['crop'];
    $lang = $_GET['lang'];
	include("db.php");
	if($lang!='hi')
	    $q="SELECT id, name FROM disease_info WHERE crop='$crop'";
	else
	    $q="SELECT id, name_hi FROM disease_info WHERE crop_hi='$crop'.'_hi'";
	$result = $mysqli->query($q);
	$rows = array();
	while($r = mysqli_fetch_assoc($result)) {
		$rows[] = $r;
	}
	print json_encode($rows,JSON_UNESCAPED_UNICODE);
	$result->close();
	$mysqli->close();
?>