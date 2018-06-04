<?php
    /* sell_tool.php: sell specified tool if the tool is not reserved or held
     * $response["success"]: 0 or 1 indicates whether tool information update successfully
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
    if (!empty($_GET["ToolID"]) && !empty($_GET["SellClerkLogin"]) && !empty($_GET["SaleDate"])){
		$ToolID = mysql_real_escape_string($_GET["ToolID"]);
		$SellClerkLogin = mysql_real_escape_string($_GET["SellClerkLogin"]);
		$SaleDate = date($_GET["SaleDate"]);
		//check if saledate is vaild
		if ((date('Y-m-d', strtotime($SaleDate)) != $SaleDate)){
	        $response["success"] = 0;
		    $response["message"] = "Please provide a valid date.";
		} else  {
			//check if tool has already been reserved or held or sold
			$query = "SELECT ToolID ";
			$query.= "FROM Reservation NATURAL JOIN ReservationReservesTool ";
			$query.= "WHERE ToolID = '$ToolID' and EndDate > '$SaleDate' ";
			$query.= "UNION ";
			$query.= "SELECT ToolID ";
			$query.= "FROM ServiceRequest ";
			$query.= "WHERE ToolID = '$ToolID' and EndDate > '$SaleDate' ";
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
			    //update tool information
				$query = "UPDATE Tool SET SellClerkLogin = '$SellClerkLogin', SaleDate = '$SaleDate', SalePrice = PurchasePrice/2 WHERE ToolID = '$ToolID'";
				$result = mysql_query($query);
				if ($result){
					$response["success"] = 1;
					$response["message"] = "Sale information successfully updated.";
					$query = "SELECT SalePrice FROM Tool WHERE ToolID = '$ToolID'";
					$result = mysql_fetch_array(mysql_query($query));
					$response["SalePrice"] = $result["SalePrice"];
				} else {
					$response["success"] = 0;
					$response["message"] = "An error occured: ".mysql_error();
				}
			} else {
				$response["success"] = 0;
				$response["message"] = "The requested tool is not available for sell.";
			}
		} 
	} else {
	    //required field is missing
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing.";
	}
	//echoing JSON response
	echo json_encode($response);
?>
