<?php
    $phone_no=$_GET['phone_no'];
    $otp = $_GET['otp'];
	include("db.php");
	$q="select count(*)as cnt from farmer where phone_no='$phone_no' and otp='$otp'";
	$result = $mysqli->query($q);
	$cnt= mysqli_fetch_assoc($result);
	$cnt= $cnt['cnt'];
	if($cnt==1)
	    echo json_encode(array('Status'=>'Success'));
	else
	    echo json_encode(array('Status'=>'Faild')); 
	$mysqli->close();
?>