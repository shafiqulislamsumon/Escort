<?php 
	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		require_once('db_config.php');
		
		$postdata = file_get_contents("php://input"); 
		$data = json_decode($postdata, true);

		if (is_array($data['LOCATION'])) {
			
			foreach ($data['LOCATION'] as $record) {
				$time = $record['time'];
				$address = $record['address'];
				$city = $record['city'];
				$state = $record['state'];
				$postalCode = $record['postalCode'];
				$country = $record['country'];
				$feature = $record['feature'];
				
				$sql = "INSERT INTO location_table (time, address, city, state, postal_code, country, feature) VALUES ('$time', '$address', '$city', '$state', '$postalCode', '$country', '$feature')";

				if(mysqli_query($con,$sql)){
					echo 'Location Added Successfully';
				}else{
					echo 'Could Not Location';
				}
			}
		}
		
		//Closing the database 
		mysqli_close($con);
	}else{
		echo 'Not Post Request';
	}
?>