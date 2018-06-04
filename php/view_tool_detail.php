<?php

	/* view_tool_detail.php: retrieve detail information of a specified tool
     * $response["success"]: 0 or 1 indicates whether tool detail retrieve successfully
     * $response["message"]: contains the error message if failed
     * $response["tool"]: an array of ToolID, AbbrDescription, FullDescription, PurchasePrice, DailyRentalPrice, Deposit of the specified tool
     */

	//Create a database connection
	require_once('db_connect.php');
	$connect = db_connect();
	mysql_select_db("HandymanTools") or die( "Unable to select database");

	//array for JSON response
	$response = array();

	if (!empty($_GET["ToolID"])){
		$ToolID = mysql_real_escape_string($_GET["ToolID"]);
		$tool = array();
		$query = "SELECT ToolID, AbbrDescription, FullDescription, PurchasePrice, DailyRentalPrice, Deposit, ToolType FROM Tool WHERE ToolID = '$ToolID'";
		$result = mysql_query($query);

		if (!$result) {
			$response["success"] = 0;
			$response["message"] = "An error occured: ".mysql_error();
		    die(json_encode($response));

		} elseif (mysql_num_rows($result) > 0) {
			$result = mysql_fetch_array($result);
			$tool["ToolID"] = $result["ToolID"];
			$tool["AbbrDescription"] = $result["AbbrDescription"];
			$tool["FullDescription"] = $result["FullDescription"];
			$tool["PurchasePrice"] = $result["PurchasePrice"];
			$tool["DailyRentalPrice"] = $result["DailyRentalPrice"];
			$tool["Deposit"] = $result["Deposit"];
			$tool["ToolType"] = $result["ToolType"];
			$tool["Accessories"] = null;
			if ($result["ToolType"] == "Power Tool"){
				$query = "SELECT GROUP_CONCAT(Accessory SEPARATOR ', ') AS Accessories ";
				$query.= "FROM PowerToolAccessories ";
				$query.= "WHERE ToolID = '$ToolID' ";
				$query.= "GROUP BY ToolID ";
				$result = mysql_query($query);
				if (!$result){
					$response["success"] = 0;
					$response["message"] = "An error occured: ".mysql_error();
		    		die(json_encode($response));
				} else {
					$result = mysql_fetch_array($result);
					$tool["Accessories"] = $result["Accessories"];
				}
			}
			$response["success"] = 1;
			$response["tool"] = array();
			array_push($response["tool"], $tool);

		} else {
			$response["success"] = 0;
			$response["message"] = "No tool found.";
		}
	} else {
	    //required field is missing
		$response["success"] = 0;
		$response["message"] = "Required field is missing.";
	} 
	//echoing JSON response
	echo json_encode($response);
?>
