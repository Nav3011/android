<?php
session_start();
include('auth.php');
include('database.php');
$logout_query = "UPDATE users SET logged_in=0 , access_time='0000-00-00 00:00:00' , access_status=0 , granted_by='' WHERE user_id='".$_SESSION['user_id']."'";
$logout_query_result = mysqli_query($con , $logout_query);
if($logout_query_result)
{
unset($_SESSION['user_id']);
session_destroy();
header("Location: index.php");
exit;
}
?>