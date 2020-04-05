<?php 
	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		require_once('db_config.php');
		
		$postdata = file_get_contents("php://input"); 
		$data = json_decode($postdata, true);

		if (is_array($data['CONTACT'])) {
			
			foreach ($data['CONTACT'] as $record) {
				$name = $record['name'];
				$number = $record['number'];
				
				$sql = "INSERT INTO contact_table (name, number) VALUES ('$name', '$number')";

				if(mysqli_query($con,$sql)){
					echo 'Contact Added Successfully';
				}else{
					echo 'Could Not Add Contact';
				}
			}
		}
		
		//Closing the database 
		mysqli_close($con);
	}else{
		echo 'Not Post Request';
	}
?>