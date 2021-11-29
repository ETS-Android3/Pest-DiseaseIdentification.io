<?php
    $crop=$_POST['crop'];
    $farmer_no=$_POST['farmer_no'];
    $disease_id=$_POST['disease_id'];
    $file_name = $_FILES['image']['name'];
//    echo $file_name;
	$temp_file = $_FILES['image']['tmp_name'];
	$data = base64_encode(file_get_contents($file_name));//base64 encoded data
	$file_basename = substr($file_name, 0, strripos($file_name, '.')); // get file name
	$file_ext = substr($file_name, strripos($file_name, '.')); // get file extention
	$path = 'uploaded_images/'.$file_name;
	move_uploaded_file($temp_file, $path);
    include("db.php");
    $q="insert into detection values ('$file_name','$farmer_no',$disease_id,null,'$crop')";
	$result = $mysqli->query($q);
	if($result==1)
	    echo json_encode(array('Status'=>'Success'));
	else
	    echo json_encode(array('Status'=>'Faild'));
	$mysqli->close();
?>