<?php

    /* hold_tool_for_repair.php: insert service request if the tool is not reserved or held
     * $response["success"]: 0 or 1 indicates whether service request insert successfully
	 * $response["message"]: contains the error message if failed
     */

	//set default timezone
    date_default_timezone_set('America/New_York');

	//Create a database connection
	require_once('db_connect.php');
	$connect = db_connect();
	mysql_select_db("HandymanTools") or die( "Unable to select database");

	//array for JSON response
	$response = array();

	//check for required fields
    if (!empty($_GET["ToolID"]) && !empty($_GET["EstimateRepairCost"]) && !empty($_GET["StartDate"]) && !empty($_GET["EndDate"]) && !empty($_GET["HoldClerkLogin"])){
		$EstimateRepairCost = mysql_real_escape_string($_GET["EstimateRepairCost"]);
		$ToolID = mysql_real_escape_string($_GET["ToolID"]);
		$HoldClerkLogin = mysql_real_escape_string($_GET["HoldClerkLogin"]);
		$StartDate = date($_GET["StartDate"]);
		$EndDate = date($_GET["EndDate"]);
		//check if startdate and enddate is vaild
		if ((date('Y-m-d', strtotime($StartDate)) != $StartDate) or date('Y-m-d', strtotime($EndDate)) != $EndDate){
	        $response["success"] = 0;
		    $response["message"] = "Please provide a valid date.";
		//check if startdate is before enddate
		} elseif (strtotime($StartDate) < strtotime($EndDate)){
			//check if tool has already been reserved or held
			$query = "SELECT ToolID ";
			$query.= "FROM Reservation NATURAL JOIN ReservationReservesTool ";
			$query.= "WHERE ToolID = '$ToolID' and EndDate > '$StartDate' ";
			$query.= "UNION ";
			$query.= "SELECT ToolID ";
			$query.= "FROM ServiceRequest ";
			$query.= "WHERE ToolID = '$ToolID' and EndDate > '$StartDate' AND StartDate < '$EndDate' ";
			$query.= "UNION ";
			$query.= "SELECT ToolID ";
			$query.= "FROM Tool ";
			$query.= "WHERE ToolID = '$ToolID' and SaleDate IS NOT NULL";
			$result = mysql_query($query);

			if (!$result) {
				$response["success"] = 0;
				$response["message"] = "An error occured: ".mysql_error();
			    die(json_encode($response));
			}
			
			if (mysql_num_rows($result) == 0){
			    //Insert service request
				$query = "INSERT ServiceRequest VALUES('$HoldClerkLogin', '$ToolID', '$StartDate', '$EndDate', '$EstimateRepairCost')";
				$result = mysql_query($query);
				if ($result){
					$response["success"] = 1;
					$response["message"] = "Service Order Request successfully added.";
				} else {
					$response["success"] = 0;
					$response["message"] = "An error occured: ".mysql_error();
				}
			} else {
				$response["success"] = 0;
				$response["message"] = "The requested tool is not available during the specific time period.";
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