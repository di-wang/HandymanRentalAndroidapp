<?php
    /* generate_report1.php: output a list of every item in inventory
	 * $response["success"]: 0 or 1 indicates whether report generates successfully
	 * $response["message"]: contains the error message if failed
	 * $response["tools"]: an array contains ToolID, AbbrDescription, RentalProfit, CostOfTool, TotalProfit of each tool
	 */

    //set default timezone
    date_default_timezone_set('America/New_York');
	//Create a database connection
	require_once('db_connect.php');
	$connect = db_connect();
	mysql_select_db("HandymanTools") or die( "Unable to select database");
	
	//array for JSON response
	$response = array();
    if (!empty($_GET["ReportDate"])){
    	$ReportDate = date($_GET["ReportDate"]);
    	if (date('Y-m-d', strtotime($ReportDate)) != date($ReportDate)){
	        //ReportDate is invaild
	        $response["success"] = 0;
			$response["message"] = "Please provide a valid date.";
	    } else {
	    	
	    	$query = "SELECT T.ToolID AS ToolID, AbbrDescription, COALESCE(RentalProfit, 0) AS RentalProfit, COALESCE(RepairCost, 0) + PurchasePrice AS CostOfTool, COALESCE(RentalProfit, 0) - COALESCE(RepairCost, 0) - PurchasePrice As TotalProfit ";
			$query.= "FROM ";
			$query.= "Tool AS T ";
			$query.= "LEFT JOIN ";
		    $query.= "(SELECT ToolID, SUM(DailyRentalPrice * DATEDIFF(EndDate, StartDate)) AS RentalProfit ";
		    $query.= "From Reservation NATURAL JOIN ReservationReservesTool NATURAL JOIN Tool ";
		    $query.= "WHERE EndDate <= '$ReportDate' ";
		    $query.= "GROUP BY ToolID) AS R ";
			$query.= "ON T.ToolID = R.ToolID ";
			$query.= "LEFT JOIN  ";
		    $query.= "(SELECT ToolID, SUM(EstimateRepairCost) AS RepairCost ";
		    $query.= "From ServiceRequest ";
		    $query.= "WHERE EndDate <= '$ReportDate' ";
		    $query.= "GROUP BY ToolID) AS S ";
			$query.= "On T.ToolID = S.ToolID ";
			$query.= "WHERE SaleDate is NULL ";
			$query.= "ORDER BY TotalProfit DESC";
			$result = mysql_query($query);

			if (!$result) {
				$response["success"] = 0;
				$response["message"] = "An error occured: ".mysql_error();
			    die(json_encode($response));

			} elseif (mysql_num_rows($result) > 0){
				$response["success"] = 1;
				$response["tools"] = array();
				while ($row = mysql_fetch_array($result)) {
					$tool = array();
					$tool["ToolID"] = $row["ToolID"];
					$tool["AbbrDescription"] = $row["AbbrDescription"];
					$tool["RentalProfit"] = $row["RentalProfit"];
					$tool["CostOfTool"] = $row["CostOfTool"];
					$tool["TotalProfit"] = $row["TotalProfit"];
					array_push($response["tools"], $tool);
				}
			} else {
				$response["success"] = 0;
				$response["message"] = "No tool found.";
			}
		}
	} else {
	    //required field is missing
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing or date provided is invalid.";
    } 
	//echoing JSON response
	echo json_encode($response);
?>