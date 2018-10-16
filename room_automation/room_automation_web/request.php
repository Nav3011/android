<?php  
	include('database.php');
	session_start();
	$_SESSION['message'] = "";
	if(isset($_POST['user_id']) && isset($_POST['password']) && isset($_POST['time']))
	{
		$user_id = mysqli_real_escape_string($con , stripslashes($_REQUEST['user_id']));
		$password = mysqli_real_escape_string($con , stripslashes($_REQUEST['password']));
		$time = mysqli_real_escape_string($con , stripslashes($_REQUEST['time']));
		$login_query = "SELECT * FROM users WHERE user_id = '$user_id'";
		$result = mysqli_query($con , $login_query);
		if(mysqli_num_rows($result) == 1)
		{
			$row = mysqli_fetch_array($result,MYSQLI_ASSOC);
			if($password == $row['password'])
			{
				//time from which user needs access
				$time_query = "UPDATE users SET access_time='$time' , sent_request=1 , access_status=1 WHERE user_id='$user_id'";
				$time_query_result = mysqli_query($con , $time_query);
				if($time_query_result)
					$_SESSION['message'] = 'Submitted';

			}
			else
				$_SESSION['error_msg'] = 'Invalid username/password';
		}
	}
?>
<!DOCTYPE html>
<html>
<head>
	<title>room automation</title>
</head>
<body>
	<form method="POST" action="request.php">
		<span><p>username : <input type="text" name="user_id"></p></span>
		<span><p>password : <input type="password" name="password"></p></span>
		<span><p>time : <input type="text" name="time"></p></span>
		<p><input type="submit" value="submit_request"></p>
	</form>
	<?php  echo $_SESSION['message']?>
	<a href="index.php"><h3>back</h3><a>
</body>
</html>