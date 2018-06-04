<?php

    /* check_availability.php: 
     * $response["success"]: 1 -- successfully find tools that are available during specified dates
                             0 -- error
                             -1 -- no tool found
	 * $response["message"]: contains the error message if failed
	 * $response["tools"]: an array contains ToolID, AbbrDescription, Deposit, DailyRentalPrice of each tool
     */

    //set default timezone
    date_default_timezone_set('America/New_York');
	//Create a database connection
	require_once('db_connect.php');
	$connect = db_connect();
	mysql_select_db("HandymanTools") or die( "Unable to select database");

	//array for JSON response
	$response = array();

	if (!empty($_GET["ToolType"]) && !empty($_GET["StartDate"]) && !empty($_GET["EndDate"])){
		$ToolType = mysql_real_escape_string($_GET["ToolType"]);
		$StartDate = date($_GET["StartDate"]);
		$EndDate = date($_GET["EndDate"]);
		//check if startdate and enddate is vaild
		if ((date('Y-m-d', strtotime($StartDate)) != $StartDate) or date('Y-m-d', strtotime($EndDate)) != $EndDate){
	        $response["success"] = 0;
		    $response["message"] = "Please provide a valid date(s).";
		//check if startdate is before enddate
		} elseif (strtotime($StartDate) < strtotime($EndDate)){
			//find available tools
			$query = "SELECT ToolID, AbbrDescription, Deposit, DailyRentalPrice ";
			$query.= "FROM Tool ";
			$query.= "WHERE ToolType = '$ToolType' and SaleDate is NULL and ToolID NOT IN ";
			$query.= "(SELECT ToolID ";
			$query.= "FROM Reservation NATURAL JOIN ReservationReservesTool ";
			$query.= "WHERE EndDate > '$StartDate' AND StartDate < '$EndDate' ";
			$query.= "UNION ";
			$query.= "SELECT ToolID ";
			$query.= "FROM ServiceRequest ";
			$query.= "Where EndDate > '$StartDate' AND StartDate < '$EndDate')";
			$result = mysql_query($query);

			if (!$result) {
				$response["success"] = 0;
				$response["message"] = "An error occured: ".mysql_error();
			    die(json_encode($response));

			} elseif ($result && mysql_num_rows($result) > 0){
				$response["success"] = 1;
			    $response["tools"] = array();
				while ($row = mysql_fetch_array($result)){
					//temp tool array
					$tool = array();
					$tool["ToolID"] = $row["ToolID"];
					$tool["AbbrDescription"] = $row["AbbrDescription"];
					$tool["Deposit"] = $row["Deposit"];
					$tool["DailyRentalPrice"] = $row["DailyRentalPrice"];
					//push single tool into final tools array
					array_push($response["tools"], $tool);
			    }
			} else {
				$response["success"] = -1;
				$response["message"] = "No tool found.";
			}
		} else {
			$response["success"] = 0;
			$response["message"] = "An error occured: Start date should be less than end date.";
		}
	} else {
	    //required field is missing
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing.";
	} 

	//echoing JSON response
	echo json_encode($response);
?>