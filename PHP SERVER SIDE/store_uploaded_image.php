<?php
    $crop=$_GET['crop'];
    $farmer_no=$_GET['farmer_no'];
    $file_name=$_GET['file_name'];
	$content = file_get_contents("http://www.pythonanywhere.com/user/SAFWAN/files/home/SAFWAN/mysite/upload/".$file_name);
    $fp = fopen("./uploaded_images/".$file_name, "w");
    fwrite($fp, $content);
    fclose($fp);
    include("db.php");
    $q="insert into detection values ('$file_name','$farmer_no',null,null,'$crop')";
	$result = $mysqli->query($q);
	if($result==1)
	    echo json_encode(array('Status'=>'Success'));
	else
	    echo json_encode(array('Status'=>'Faild'));
	$mysqli->close();
?>