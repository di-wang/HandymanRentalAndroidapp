<?php

  /* login.php: 
   * $response["success"]: 0 or 1 indicates whether login successfully
   * $response["message"]: contains the error message if failed
   */

  //Create a database connection
  require_once('db_connect.php');
  $connect = db_connect();
  mysql_select_db("HandymanTools") or die( "Unable to select database");
  
  //array for JSON response
  $response = array();

  //check for required fields
	if (!empty($_GET['Login']) && !empty($_GET['Password']) && !empty($_GET['UserType'])){
		$Login = mysql_real_escape_string($_GET['Login']);
		$Password = mysql_real_escape_string($_GET['Password']);
		$UserType = mysql_real_escape_string($_GET['UserType']);
    
		if ($UserType == "Customer"){
			//check if user is registered
      $query = "SELECT Email FROM Customer WHERE Email = '$Login'";
      $result = mysql_query($query);

      if (!$result) {
        $response["success"] = 0;
        $response["message"] = "An error occured: ".mysql_error();
        die(json_encode($response));
      }
      
			if (mysql_num_rows($result)) { 
        //check if password is correct
        $query = "SELECT Email FROM Customer WHERE Email = '$Login' and Password = '$Password'";
        $result = mysql_query($query);

        if (!$result) {
          $response["success"] = 0;
          $response["message"] = "An error occured: ".mysql_error();
          die(json_encode($response));
        }
      
        if (mysql_num_rows($result)){
          //successfully login
          $response["success"] = 1;
          $response["message"] = "Login successfully.";
        } else {
          //password is incorrect, login failed
          $response["success"] = 0;
          $response["message"] = "Password incorrect. Please try again";            
        }
			} else{
        //user is not registered, login failed
        $response["success"] = 0;
        $response["message"] = "You are not register yet. Please signup.";
      }
		} elseif ($UserType == "Clerk") {
      //check if login and password is correct
      $result = mysql_query("SELECT Login FROM Clerk WHERE Login = '$Login' and Password = '$Password'");

      if (!$result) {
        $response["success"] = 0;
        $response["message"] = "An error occured: ".mysql_error();
        die(json_encode($response));
      }
      
      if (mysql_num_rows($result)){
        //successfully login
        $response["success"] = 1;
        $response["message"] = "Login successfully.";
      } else {
        //login or password incorrect, login failed
        $response["success"] = 0;
        $response["message"] = "Username or password is incorrect. Please try again";
      }
    }
	} else {
    //required fields missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing.";
  } 
  //echoing JSON response
  echo json_encode($response);
?>
