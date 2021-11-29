<?php
    $crop_type = $_GET['crop'];
	include("db.php");
	$q="select * from detection where detected_disease_id is not null and correct_disease_id is null and crop='".$crop_type."'";
	$result = $mysqli->query($q);
	$rows = array();
	while($r = mysqli_fetch_assoc($result)) {
		$rows[] = $r;
	}
	print json_encode($rows,JSON_UNESCAPED_UNICODE);
	$result->close();
	$mysqli->close();
?>