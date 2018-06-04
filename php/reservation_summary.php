<?php

    /* Reservation summary
     * $response["success"]: 0 or 1 
     * $response["message"]: contains the error message if failed
     * $response["tool"]: an array contains ToolID and AbbrDescription of each reserved tool
     * $response["reservation"]: an array contains ReservationNumber, StartDate, EndDate, TotalRentalPrice, TotalDepositRequired of each reservation
     */

    //Create a database connection
	require_once('db_connect.php');
	$connect = db_connect();
	mysql_select_db("HandymanTools") or die( "Unable to select database");

	//array for JSON response
	$response = array();

	//check for required fields
	if (!empty($_GET['ReservationNumber'])){
		//display Tools Rented
		$ReservationNumber = mysql_real_escape_string($_GET['ReservationNumber']);
		$query = "SELECT ToolID, AbbrDescription ";
		$query.= "FROM ReservationReservesTool Natural Join Tool ";
		$query.= "WHERE ReservationNumber = '$ReservationNumber'";
		$result = mysql_query($query);

		if (mysql_num_rows($result) > 0){
			$response["tool"] = array();
			while ($row = mysql_fetch_array($result)){
				$tool = array();
				$tool["ToolID"] = $row["ToolID"];
				$tool["AbbrDescription"] = $row["AbbrDescription"];
				array_push($response["tool"], $tool);
			}
			//display Reservation Details
			$query = "SELECT ReservationNumber, StartDate, EndDate, SUM(DailyRentalPrice)*DATEDIFF(EndDate, StartDate) + SUM(Deposit) AS TotalRentalPrice, SUM(Deposit) AS TotalDepositRequired ";
			$query.= "FROM Reservation Natural Join ReservationReservesTool Natural Join Tool ";
			$query.= "WHERE ReservationNumber = '$ReservationNumber' ";
			$query.= "GROUP BY ReservationNumber";
			$result = mysql_query($query);

			if (!$result) {
				$response["success"] = 0;
				$response["message"] = "An error occured: ".mysql_error();
			    die(json_encode($response));
			}
			
			if (mysql_num_rows($result) > 0){
				$result = mysql_fetch_array($result);
				$response["reservation"] = array();
				//temp reservation array
				$reservation = array();
				$reservation["ReservationNumber"] =  $result["ReservationNumber"];
				$reservation["StartDate"] = $result["StartDate"];
				$reservation["EndDate"] = $result["EndDate"];
				$reservation["TotalRentalPrice"] = $result["TotalRentalPrice"];
				$reservation["TotalDepositRequired"] = $result["TotalDepositRequired"];
				//push reservation to response
				$response["success"] = 1;
				array_push($response["reservation"], $reservation); 
			} else {
				$response["success"] = 0;
		        $response["message"] = "No reservation found.";
			}
		} else {
			$response["success"] = 0;
		    $response["message"] = "No reservation found.";
		}
    } else {
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing.";
    }

	//echoing JSON response
	echo json_encode($response);
?>