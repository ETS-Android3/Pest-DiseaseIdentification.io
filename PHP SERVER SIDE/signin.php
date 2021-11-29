<?php
	include("db.php");
	$un=$_GET['un']; 
	$pw=$_GET['pw']; 
	$q="SELECT count(*) as cnt from expert where expert_un='$un' and expert_pw='$pw' ";
	$result = $mysqli->query($q);
	$cnt=$result->fetch_assoc()['cnt'];
	if($cnt==1)
	    print json_encode(array('Status'=>'Success'));
	 else
	    print json_encode(array('Status'=>'Faild'));
	$result->close();
	$mysqli->close();
?>