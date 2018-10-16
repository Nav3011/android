<?php
session_start();
include('auth.php');
include('database.php');
$device_query = "SELECT device FROM device_id";
$device_query_result = mysqli_query($con , $device_query);
$device_row = mysqli_fetch_array($device_query_result,MYSQLI_ASSOC);
if(isset($_GET["user"]) && isset($_GET["device"]) && isset($_GET["do"]))
    {
        $user_id = $_GET["user"];
        $device = $_GET["device"];
        $cmd = $_GET["do"];
        $device = trim($device,"'");
        $cmd = trim($cmd,"'");
        $permission_query = "SELECT devices FROM users WHERE user_id =$user_id";
        $file = mysqli_query($con , $permission_query);
        $row = mysqli_fetch_array($file , MYSQLI_ASSOC);
        $array = json_decode($row['devices'],true);
       		if($array[$device] == 1)
       			{
       				$status_query = "SELECT status FROM status";
       				$status_result = mysqli_query($con , $status_query);
       				$status_row = mysqli_fetch_array($status_result,MYSQLI_NUM);
       				$status = $status_row[0];
       				if($status == 0)
       				{
       					if ($status == $cmd)
       						echo 'device is already stopped';
       					else
       					{ 
       						$start = "UPDATE status SET status = '1' WHERE device = '$device'";
       						$res = mysqli_query($con , $start);
       						if($res)
       							echo "device started....";
       					}
       				}
       				if($status == 1)
       				{
       					if ($status == $cmd)
       						echo 'device is already running';
       					else
       					{
       						$stop = "UPDATE status SET status = '0' WHERE device = '$device'";
       						$res = mysqli_query($con , $stop);
                  if($res)
                     echo 'device stopped...';
       					}
       				}
       			}
       		else
       			echo "not permitted";
    }
?>
<!DOCTYPE html>
<html>
<head>
	<title>panel</title>
</head>
<body>
   <h1>Device Control Panel</h1>
   <h2>Welcome........<?php echo $_SESSION['user_id'];?></h2>
	<!-- <h3>light1</h3>
	<a href="panel.php?user='<?php echo $_SESSION['user_id'];?>'&device='light1'&do='1'"><button>ON</button></a>
	<a href="panel.php?user='<?php echo $_SESSION['user_id'];?>'&device='light1'&do='0'"><button>OFF</button></a>
	<h3>light2</h3>
	<a href="panel.php?user='<?php echo $_SESSION['user_id'];?>'&device='light2'&do='1'"><button>ON</button></a>
	<a href="panel.php?user='<?php echo $_SESSION['user_id'];?>'&device='light2'&do='0'"><button>OFF</button></a>
	<h3>light3</h3>
	<a href="panel.php?user='<?php echo $_SESSION['user_id'];?>'&device='light3'&do='1'"><button>ON</button></a>
	<a href="panel.php?user='<?php echo $_SESSION['user_id'];?>'&device='light3'&do='0'"><button>OFF</button></a> -->
  <?php 
  while($device_row)
    {
      ?>
      <h2><?php echo $device_row['device'];?></h2>
      <a href="panel.php?user='<?php echo $_SESSION['user_id'];?>'&device='<?php echo $device_row['device'];?>'&do='1'"><button>ON</button></a>
      <a href="panel.php?user='<?php echo $_SESSION['user_id'];?>'&device='<?php echo $device_row['device'];?>'&do='0'"><button>OFF</button></a>
      <?php
      $device_row = mysqli_fetch_array($device_query_result,MYSQLI_ASSOC);
    }?>
	<p><a href="logout.php"><button>logout</button></a></p>
</body>
</html>