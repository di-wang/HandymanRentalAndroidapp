<?php
	/* generate_report3.php: a list of names, pick-ups, drop-offs and the sum of two of each clerk, ordered by total numbers of pickups and drop-offs
	 * $response["success"]: 0 or 1 indicates whether report generates successfully
	 * $response["message"]: contains the error message if failed
	 * $response["clerks"]: an array contains Name, Pickups, Dropoffs, Total of each clerk
	 */

	//set default timezone
    date_default_timezone_set('America/New_York');

	//Create a database connection
	require_once('db_connect.php');
	$connect = db_connect();
	mysql_select_db("HandymanTools") or die( "Unable to select database");

	//array for JSON response
	$response = array();

	if (!empty($_GET["MonthStart"]) && !empty($_GET["MonthEnd"])){
		//check if provided dates are valid
		$MonthStart = date($_GET["MonthStart"]);
		$MonthEnd = date($_GET["MonthEnd"]);
		if (date('Y-m-d', strtotime($MonthStart)) != $MonthStart or date('Y-m-d', strtotime($MonthEnd)) != $MonthEnd){
	        //ReportDate is invaild
	        $response["success"] = 0;
			$response["message"] = "Please provide a valid date(s).";
	    } elseif (strtotime($MonthStart) < strtotime($MonthEnd)){
	    	$query = "SELECT CONCAT(FirstName,' ', LastName) AS Name, COALESCE(Pickups, 0) AS Pickups, COALESCE(Dropoffs, 0) AS Dropoffs, COALESCE(Pickups, 0) + COALESCE(Dropoffs, 0) AS Total ";
	    	$query.= "FROM Clerk AS C ";
	    	$query.= "LEFT JOIN ";
	    	$query.= "(SELECT PickupClerkLogin AS Login, COUNT(PickupClerkLogin) AS Pickups ";
	    	$query.= "FROM Reservation ";
	    	$query.= "WHERE PickupDate >= '$MonthStart' and PickupDate <= '$MonthEnd' ";
	    	$query.= "GROUP BY PickupClerkLogin) AS P ";
	    	$query.= "ON C.Login = P.Login ";
	    	$query.= "LEFT JOIN ";
	    	$query.= "(SELECT DropoffClerkLogin AS Login, COUNT(DropoffClerkLogin) AS Dropoffs ";
	    	$query.= "FROM Reservation ";
	    	$query.= "WHERE DropoffDate >= '$MonthStart' and DropoffDate <= '$MonthEnd' ";
	    	$query.= "GROUP BY DropoffClerkLogin) AS D ";
	    	$query.= "ON C.Login = D.Login ";
	    	$query.= "ORDER BY Total DESC ";
		    $result = mysql_query($query);
	    	if ($result) {
	    		$response["success"] = 1;
				$response["clerks"] = array();
				while ($row = mysql_fetch_array($result)) {
					$clerk = array();
					$clerk["Name"] = $row["Name"];
					$clerk["Pickups"] = $row["Pickups"];
					$clerk["Dropoffs"] = $row["Dropoffs"];
					$clerk["Total"] = $row["Total"];
					array_push($response["clerks"], $clerk);
			    }
	    	} else {
				$response["success"] = 0;
				$response["message"] = "An error occured: ".mysql_error();
			}
	    } else {
	    	//start date is larger than end date
	    	$response["success"] = 0;
			$response["message"] = "An error occured: Start date should be less than end date.";
	    }
	} else {
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing or date(s) provided is invalid.";
	}

	//assume $MonthStart and $MonthEnd is managed by application


	//echoing JSON response
	echo json_encode($response);
?>