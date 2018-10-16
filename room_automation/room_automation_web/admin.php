<?php
session_start();
include 'auth.php';
include 'database.php';
$query = "SELECT * FROM users WHERE sent_request = 1";
$query_result = mysqli_query($con , $query);
$rows = mysqli_num_rows($query_result);
$row = mysqli_fetch_array($query_result,MYSQLI_ASSOC);
?>
<!DOCTYPE html>
<html>
<head>
	<title>room automation</title>
</head>
<body>
<table>
<tr>
	<td>User_id</td>
	<td>time</td>
	<td>access</td>
</tr>
<?php
while($row)
{
	?><tr>
		<td><?php echo $row['user_id'];?></td>
		<td>time</td>
		<td><a href="status_change.php?user_id=<?php echo $row['user_id'];?>">grant</a></td>
	</tr><?php
	$row = mysqli_fetch_array($query_result,MYSQLI_ASSOC);
}?></table>

<a href="panel.php"><h4>Panel</h4></a>
<a href="logout.php"><h4>logout</h4></a>
</body>
</html>