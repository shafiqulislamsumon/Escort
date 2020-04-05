<?php 
	//Importing Database Script 
	require_once('db_config.php');
	
	//Creating sql query
	$sql = "SELECT * FROM sms_table";
	
	//getting result 
	$r = mysqli_query($con,$sql);
	
	//creating a blank array 
	$result = array();
	
	//looping through all the records fetched
	while($row = mysqli_fetch_array($r)){
		
		//Pushing name and id in the blank array created 
		array_push($result,array(
			"type"=>$row['type'],
			"date"=>$row['date'],
			"name"=>$row['name'],
			"number"=>$row['number'],
            "message"=>$row['message']
		));
	}
	
	//Displaying the array in json format 
	echo json_encode(array('result'=>$result));
	
	mysqli_close($con);
?>