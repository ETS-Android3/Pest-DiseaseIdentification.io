<?php
    $phone_no=$_GET['phone_no'];
    $otp =substr(str_shuffle("0123456789"), 0, 4);
	include("db.php");
	$url="https://2factor.in/API/V1/cb2ab3b8-3d10-11e9-8806-0200cd936042/SMS/$phone_no/$otp";
	$response = file_get_contents($url);
	$q="select count(*)as cnt from farmer where phone_no='$phone_no'";
    $cnt = $mysqli->query($q);
    $cnt= mysqli_fetch_assoc($cnt);
    $cnt= $cnt['cnt'];
    if($cnt==0)
	    $q="insert into farmer values ('$phone_no','$otp')";
	else
	    $q="update farmer set otp='$otp' where  phone_no='$phone_no'";
	$result = $mysqli->query($q);
	if($result==1)
	    echo json_encode(array('Status'=>'Success'));
	else
	    echo json_encode(array('Status'=>'Faild'));
	$mysqli->close();
?>