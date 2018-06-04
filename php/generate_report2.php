<?php
	/* generate_report2.php: output a list all the rental customers who rented a tool over last month. 
	 * $response["success"]: 0 or 1 indicates whether report generates successfully
	 * $response["message"]: contains the error message if failed
	 * $response["customers"]: an array contains name, email and number of rentals of each customer
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
	    	$query = "SELECT CONCAT(FirstName,' ', LastName) AS Name, C.Email AS Email, SUM(Rentals) AS Rentals ";
	    	$query.= "FROM Customer AS C, ";
	    	$query.= "(SELECT CustomerLogin, DATEDIFF(EndDate, StartDate) AS Rentals ";
	    	$query.= "FROM Reservation NATURAL JOIN ReservationReservesTool ";
	    	$query.= "WHERE StartDate >= '$MonthStart' and EndDate <= '$MonthEnd' ";
	    	$query.= "UNION ALL ";
	    	$query.= "SELECT CustomerLogin, DATEDIFF(EndDate, '$MonthStart') AS Rentals ";
	    	$query.= "FROM Reservation NATURAL JOIN ReservationReservesTool ";
	    	$query.= "WHERE StartDate < '$MonthStart' and EndDate <= '$MonthEnd' and EndDate > '$MonthStart' ";
	    	$query.= "UNION ALL ";
	    	$query.= "SELECT CustomerLogin, DATEDIFF('$MonthEnd', StartDate) AS Rentals ";
	    	$query.= "FROM Reservation NATURAL JOIN ReservationReservesTool ";
	    	$query.= "WHERE StartDate >= '$MonthStart' and StartDate < '$MonthEnd' and EndDate > '$MonthEnd' ";
	    	$query.= "UNION ALL ";
	    	$query.= "SELECT CustomerLogin, DATEDIFF('$MonthEnd', '$MonthStart') AS Rentals ";
	    	$query.= "FROM Reservation NATURAL JOIN ReservationReservesTool ";
	    	$query.= "WHERE StartDate < '$MonthStart' and EndDate > '$MonthEnd') AS R ";
	    	$query.= "WHERE C.Email = R.CustomerLogin ";
	    	$query.= "GROUP BY C.Email ";
	    	$query.= "ORDER BY Rentals DESC, C.LastName";
	    	$result = mysql_query($query);
	    	if ($result) {
	    		$response["success"] = 1;
				$response["customers"] = array();
				while ($row = mysql_fetch_array($result)) {
					$customer = array();
					$customer["Name"] = $row["Name"];
					$customer["Email"] = $row["Email"];
					$customer["Rentals"] = $row["Rentals"];
					array_push($response["customers"], $customer);
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

	//echoing JSON response
	echo json_encode($response);
?>