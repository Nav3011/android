<?php
session_start();
$_SESSION['error_msg']="";
include('database.php');
if(isset($_POST['user_id']) && isset($_POST['password']))
{

	$user_id = mysqli_real_escape_string($con , stripslashes($_REQUEST['user_id']));
	$password = mysqli_real_escape_string($con , stripslashes($_REQUEST['password']));
	$login_query = "SELECT * FROM users WHERE user_id = '$user_id'";
	$result = mysqli_query($con , $login_query);
	$rows = mysqli_num_rows($result);

	if($rows==1)
	{
		$row = mysqli_fetch_array($result,MYSQLI_ASSOC);
		if($password == $row['password'])
		{
			$_SESSION['user_id'] = $row['user_id'];
			$admin_logged_in_query = "UPDATE users SET logged_in=1 WHERE user_id='".$_SESSION['user_id']."'";
			$admin_logged_in_query_result = mysqli_query($con, $admin_logged_in_query);
			//echo $admin_logged_in_query_result;
			if($admin_logged_in_query_result)
				header('Location: admin.php');
		}
	}
	else
		$_SESSION['error_msg'] = 'Invalid username/password';
}
?>
<!DOCTYPE html>
<html>
<head>
	<title>admin</title>
</head>
<body>
	<h2>Admin login</h2>
	<form method="POST" action="admin_login.php">
		<span><p>username : <input type="text" name="user_id"></p></span>
		<span><p>password : <input type="password" name="password"></p></span>
		<p><input type="submit" value="login"></p>
	</form>
	<?php  echo $_SESSION['error_msg']?>
</body>
</html>