<?php

  /* create_profile.php:
   * $response["success"]: 0 or 1 indicates whether profile insert successfully
   * $response["message"]: contains the error message if failed
   */

  //Create a database connection
  require_once('db_connect.php');
  $connect = db_connect();
  mysql_select_db("HandymanTools") or die( "Unable to select database");

  //array for JSON response
  $response = array();

  //check for required fields
  if (!empty($_GET['Email']) && !empty($_GET['Password']) && !empty($_GET['FirstName']) && !empty($_GET['LastName']) 
    && !empty($_GET['HomePhoneAreaCode']) && !empty($_GET['HomePhoneLocalNumber']) && !empty($_GET['WorkPhoneAreaCode']) 
    && !empty($_GET['WorkPhoneLocalNumber']) && !empty($_GET['Address'])){
    $Email = mysql_real_escape_string($_GET['Email']);
    $Password = mysql_real_escape_string($_GET['Password']);
    $FirstName = mysql_real_escape_string($_GET['FirstName']);
    $LastName = mysql_real_escape_string($_GET['LastName']);
    $HomePhoneAreaCode = mysql_real_escape_string($_GET['HomePhoneAreaCode']);
    $HomePhoneLocalNumber = mysql_real_escape_string($_GET['HomePhoneLocalNumber']);
    $WorkPhoneAreaCode = mysql_real_escape_string($_GET['WorkPhoneAreaCode']);
    $WorkPhoneLocalNumber = mysql_real_escape_string($_GET['WorkPhoneLocalNumber']);
    $Address = mysql_real_escape_string($_GET['Address']);
    //insert a new customer
    $result = mysql_query("INSERT INTO Customer VALUES ('$Email', '$Password', '$FirstName', '$LastName', '$HomePhoneAreaCode', '$HomePhoneLocalNumber', '$WorkPhoneAreaCode', '$WorkPhoneLocalNumber', '$Address')");
    if ($result){
      //successfully inserted into database
      $response["success"] = 1;
      $response["message"] = "Sign up successfully.";
    } else {
      //failed to insert row
      $response["success"] = 0;
      $response["message"] = "An error occured: ".mysql_error();
    }
  } else {    
    //required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing.";
  } 

  //echoing JSON response
  echo json_encode($response);
?>