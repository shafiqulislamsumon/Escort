<?php 
	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		require_once('db_config.php');
		
		$postdata = file_get_contents("php://input"); 
		$data = json_decode($postdata, true);

		if (is_array($data['SMS'])) {
			
			foreach ($data['SMS'] as $record) {
				$type = $record['type'];
				$date = $record['date'];
				$name = $record['name'];
				$number = $record['number'];
				$message = $record['message'];
				
				$sql = "INSERT INTO sms_table (type, date, name, number, message) VALUES ('$type', '$date', '$name', '$number','$message')";

				if(mysqli_query($con,$sql)){
					echo 'SMS Added Successfully';
				}else{
					echo 'Could Not Add SMS';
				}
			}
		}
		
		//Closing the database 
		mysqli_close($con);
	}else{
		echo 'Not Post Request';
	}
?>