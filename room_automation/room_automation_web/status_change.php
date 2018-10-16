<?php
session_start();
include('auth.php');
include('database.php');
$access_by = $_SESSION['user_id'];
$user_id = $_GET['user_id'];
$grant_query = "UPDATE users SET access_status=2 , granted_by='$access_by' , sent_request=0 WHERE user_id='$user_id'";
$result_grant_query = mysqli_query($con , $grant_query);
//$logged_in_query = "UPDATE users SET logged_in=1 WHERE user_id='$user_id'";
//$result_logged_in_query = mysqli_query($con , $logged_in_query);
if($result_grant_query)
	header('Location: admin.php');
?>