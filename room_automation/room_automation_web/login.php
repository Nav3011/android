<?php  
	session_start();
	include('database.php');
	$_SESSION['message'] = "";
	if(isset($_POST['user_id']) && isset($_POST['password']))
	{
		$user_id = mysqli_real_escape_string($con , stripslashes($_REQUEST['user_id']));
		$password = mysqli_real_escape_string($con , stripslashes($_REQUEST['password']));
		$login_query = "SELECT * FROM users WHERE user_id = '$user_id'";
		$result = mysqli_query($con , $login_query);
		if(mysqli_num_rows($result)==1)
		{
			$row = mysqli_fetch_array($result,MYSQLI_ASSOC);
			if($password == $row['password'])
			{
				$_SESSION['user_id'] = $row['user_id'];
				if($row['access_status'] == 0)  //try to login without requesting
					$_SESSION['message'] = "Please request the admin for permission";
				if($row['access_status'] == 2)   // sent a request and  admin has granted
				{
					$logged_in_query = "UPDATE users SET logged_in=1 WHERE user_id='".$_SESSION['user_id']."'";
					$request_query_result = mysqli_query($con , $logged_in_query);
					if($request_query_result)
						header('Location: panel.php');
				}
				if($row['access_status'] == 1)  // sent a request but admin didn't granted still
					$_SESSION['message'] = "Your response has been submitted...Please wait for the response";
			}
		}
		else
			$_SESSION['message'] = 'Invalid username/password';	
	}
?>
<!DOCTYPE html>
<html>
<head>
	<title>room automation</title>
</head>
<body>
	<form method="POST" action="login.php">
		<span><p>username : <input type="text" name="user_id"></p></span>
		<span><p>password : <input type="password" name="password"></p></span>
		<p><input type="submit" value="login"></p>
	</form>
	<a href="admin_login.php"><h4>Admins</h4></a>
	<a href="index.php"><h4>back</h4><a>
	<?php  echo $_SESSION['message']?>
</body>
</html>