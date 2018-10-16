<?php
include('database.php');
$rfid = $_GET['id'];
$query = "SELECT user_id FROM users WHERE rfid = '$rfid'";
$result = mysqli_query($con , $query);
$rows = mysqli_num_rows($result);
if($rows)
	echo 'ACCESS=1';
else 
	echo 'ACCESS=0';
?>