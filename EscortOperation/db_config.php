 <?php
 
 define('HOST','localhost');
 define('USER','tech2vie_android');
 define('PASS','androidDB2018');
 define('DB','tech2vie_android');
 
 //Connecting to Database
 $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');
 
 ?>