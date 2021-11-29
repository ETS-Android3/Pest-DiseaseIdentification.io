<?php
    $image_id=$_GET['image_id'];
    $correct_choise=$_GET['correct_choise'];
	include("db.php");
	$q="update detection set correct_disease_id=$correct_choise where image_id='$image_id'";
	$result = $mysqli->query($q);
	if($result==1)
	    echo json_encode(array('Status'=>'Success'));
	else
	    echo json_encode(array('Status'=>'Faild'));
	if($correct_choise==1)
	    $disease_name='brown spot';
	else if($correct_choise==2)
	    $disease_name='false smut';
	else if($correct_choise==3)
	    $disease_name='planthoppoer';
	rename("uploaded_images/$image_id","dataset/rice/$disease_name/$image_id");
	$mysqli->close();
?>