<?php
    $disease_id=$_GET['disease_id'];
    $lang = $_GET['lang'];
	include("db.php");
//	mysql_set_charset('utf-8');
	if($lang=='hi')
	    $q="select name_hi,cause_hi,precaution_hi,symptoms_hi,natural_treatment_hi,pesticide_hi,crop_hi,clear_image from disease_info where id=$disease_id";
	else
	    $q="select name,cause,precaution,symptoms,natural_treatment,pesticide,crop,clear_image from disease_info where id=$disease_id";
	$result = $mysqli->query($q);
	$rows = array();
	while($r = mysqli_fetch_assoc($result)) {
		$rows[] = $r;
	}
	print json_encode($rows,JSON_UNESCAPED_UNICODE);
	$result->close();
	$mysqli->close();
?>