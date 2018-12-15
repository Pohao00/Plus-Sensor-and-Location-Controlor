<?php
	$host='127.0.0.1';
	$uname='root';
	$pwd='';
	$db="android";

	$con = mysql_connect($host,$uname,$pwd) or die("connection failed");
	mysql_select_db($db,$con) or die("db selection failed");
	 
	$latit=$_REQUEST['latit'];
	$longt=$_REQUEST['longt'];
	$time=$_REQUEST['time'];
	$numb=$_REQUEST['numb'];
	$customer_id=$_REQUEST['customer_id'];

	$flag['code']=0;

	if($r=mysql_query("UPDATE sample
SET latit='$latit', longt='$longt', time='$time'
WHERE customer_id='$customer_id'",$con))
	{
		$flag['code']=1;
		
	}

	print(json_encode($flag));
	mysql_close($con);
?>