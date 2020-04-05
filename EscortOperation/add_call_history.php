<?php 
	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		require_once('db_config.php');
		
		$postdata = file_get_contents("php://input"); 
		$data = json_decode($postdata, true);

		if (is_array($data['CALLHISTORY'])) {
			
			foreach ($data['CALLHISTORY'] as $record) {
				$type = $record['type'];
				$number = $record['number'];
				$date = $record['date'];
				$duration = $record['duration'];
				
				$sql = "INSERT INTO call_history_table (type, number, date, duration) VALUES ('$type', '$number', '$date', '$duration')";

				if(mysqli_query($con,$sql)){
					echo 'Call history Added Successfully';
				}else{
					echo 'Could Not Call History';
				}
			}
		}
		
		//Closing the database 
		mysqli_close($con);
	}else{
		echo 'Not Post Request';
	}
?>