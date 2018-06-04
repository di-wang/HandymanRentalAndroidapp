<?php 
  function db_connect(){
    //database connection variables
  	$dbhost = "localhost"; //database server
    $dbuser = "root"; //database user
    $dbpass = ""; //database password (mention your database password here)
    $connect = mysql_connect($dbhost, $dbuser, $dbpass);

    if (!$connect) {
  	  die('Unable to connect to MySQL: ' . mysql_error());
    }

    return $connect;
  }
?>