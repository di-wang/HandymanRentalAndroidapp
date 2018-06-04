<?php

    /* insert_reservation.php: insert a new reservation 
     * $response["success"]: 0 or 1 indicates whether reservation insert successfully
	 * $response["message"]: contains the error message if failed
     */

	//Create a database connection
	require_once('db_connect.php');
	$connect = db_connect();
	mysql_select_db("HandymanTools") or die( "Unable to select database");

	//array for JSON response
	$response = array();

	//check for required fields
	if (!empty($_GET['ReservationNumber']) && !empty($_GET['StartDate']) && !empty($_GET['EndDate']) && !empty($_GET['CustomerLogin']) && !empty($_GET['Tools'])){
		$ReservationNumber = mysql_real_escape_string($_GET['ReservationNumber']);
		$StartDate = mysql_real_escape_string($_GET['StartDate']);
		$EndDate = mysql_real_escape_string($_GET['EndDate']);
		$CustomerLogin = mysql_real_escape_string($_GET['CustomerLogin']);
		$Tools = array();
		$Tools = $_GET['Tools'];

		$query = "INSERT INTO Reservation(ReservationNumber, StartDate, EndDate, CustomerLogin, PickupClerkLogin, DropoffClerkLogin, PickupDate, DropoffDate, CreditCardNumber, CreditCardExpirationDate)
	    VALUES('$ReservationNumber', '$StartDate', '$EndDate', '$CustomerLogin', NULL, NULL, NULL, NULL, NULL, NULL)";
	    $result = mysql_query($query);
	    if ($result) {
	    	foreach ($Tools as $tool){
	    		$query = "INSERT INTO ReservationReservesTool(ReservationNumber, ToolID) VALUES('$ReservationNumber', '$tool')";
	    		$result = mysql_query($query);
	    		if (!$result){
	    			$response["success"] = 0;
			        $response["message"] = "Failed to add tools.";
			        mysql_query("DELETE FROM ReservationReservesTool WHERE ReservationNumber = '$ReservationNumber'");
			        mysql_query("DELETE FROM Reservation WHERE ReservationNumber = '$ReservationNumber'");
	    			break;
	    		}
	    		$response["success"] = 1;
	    		$response["message"] = "Reservation is successfully added.";
	    	}
	    } else {
	    	$response["success"] = 0;
			$response["message"] = "Failed to add reservation: ".mysql_error();
	    }
	} else {
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing.";
	}

	//echoing JSON response
	echo json_encode($response);
?>