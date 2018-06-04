<?php
	/* get_rentalprice_and_deposit.php: calculate total rental price(haven't multiplied by dates) and total deposit of selected tools
	 * $response["success"]: 0 or 1 indicates whether report generates successfully
	 * $response["message"]: contains the error message if failed
	 * $response["TotalRentalPrice"]: total rental price of selected tools(haven't multiplied by dates)
	 * $response["TotalDeposit"]: total deposit price of selected tools
     */

    //Create a database connection
    require_once('db_connect.php');
    $connect = db_connect();
    mysql_select_db("HandymanTools") or die( "Unable to select database");

    $response = array();

	if (!empty($_GET)){
		$TotalRentalPrice = 0.00;
		$TotalDeposit = 0.00;
		foreach ($_GET as $key => $ToolID){
			$query = "SELECT DailyRentalPrice, Deposit FROM Tool WHERE ToolID = '$ToolID'";
			$result = mysql_query($query);

			if (!$result) {
				$response["success"] = 0;
				$response["message"] = "An error occured: ".mysql_error();
			    die(json_encode($response));

			} elseif (mysql_num_rows($result) > 0) {
				$result = mysql_fetch_array($result);
				$TotalRentalPrice += $result["DailyRentalPrice"];
				$TotalDeposit += $result["Deposit"];

			} else {
				$response["success"] = 0;
				$response["message"] = "Cannot find tool #'$ToolID'";
				die(json_encode($response));
			}
	    }
	    $response["success"] = 1;
		$response["TotalRentalPrice"] = $TotalRentalPrice;
		$response["TotalDeposit"] = $TotalDeposit;
	} else {
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing.";
	}

    //echoing JSON response
    echo json_encode($response);
?>