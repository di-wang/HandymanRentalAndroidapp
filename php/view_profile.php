<?php

	/* view_profile.php: retrieve profile of a specified customer
     * $response["success"]: 0 or 1 indicates whether profile retrieve successfully
     * $response["message"]: contains the error message if failed
     * $response["customer"]: an array of Email, FirstName, LastName, HomePhoneAreaCode, HomePhoneLocalNumber, WorkPhoneAreaCode, WorkPhoneLocalNumber, 
     * Address of the specified customer
     * $response["reservation"]: an array of ReservationNumber, Tools, StartDate,EndDate, RentalPrice, Deposit, PickupClerk, DropoffClerk of each reservation
     * made by the customer
     */

	//Create a database connection
	require_once('db_connect.php');
	$connect = db_connect();
	mysql_select_db("HandymanTools") or die( "Unable to select database");

	//array for JSON response
	$response = array();

	//get email
	if (!empty($_GET["Email"])) {
		$Email = mysql_real_escape_string($_GET["Email"]);

		//get customer information from customer table
		$query = "SELECT Email, FirstName, LastName, HomePhoneAreaCode, HomePhoneLocalNumber, WorkPhoneAreaCode, WorkPhoneLocalNumber, Address ";
		$query.= "FROM Customer WHERE Email = '$Email' ";
		$result = mysql_query($query);

		if (!$result) {
			$response["success"] = 0;
			$response["message"] = "An error occured: ".mysql_error();
			die(json_encode($response));

		} elseif (mysql_num_rows($result) > 0) {
			$result = mysql_fetch_array($result);
			//get personal information
			$customer = array();
			$customer["Email"] = $result["Email"];
			$customer["FirstName"] = $result["FirstName"];
			$customer["LastName"] = $result["LastName"];
			$customer["HomePhoneAreaCode"] = $result["HomePhoneAreaCode"];
			$customer["HomePhoneLocalNumber"] = $result["HomePhoneLocalNumber"];
			$customer["WorkPhoneAreaCode"] = $result["WorkPhoneAreaCode"];
			$customer["WorkPhoneLocalNumber"] = $result["WorkPhoneLocalNumber"];
			$customer["Address"] = $result["Address"];

			$response["success"] = 1;
			$response["customer"] = array();
			array_push($response["customer"], $customer);

			//get reservations and reservation details
			$response["reservations"] = array();

			$query = "SELECT ReservationNumber, GROUP_CONCAT(AbbrDescription SEPARATOR ', ') AS Tools, StartDate, EndDate, ";
			$query.= "SUM(DailyRentalPrice*DATEDIFF(EndDate, StartDate)) AS RentalPrice, ";
			$query.= "SUM(Deposit) AS Deposit, P.FirstName AS PickupClerk, D.FirstName AS DropoffClerk ";
			$query.= "From Reservation NATURAL JOIN ReservationReservesTool NATURAL JOIN Tool ";
			$query.= "LEFT OUTER JOIN Clerk AS P ON PickupClerkLogin = P.Login ";
			$query.= "LEFT OUTER JOIN Clerk AS D ON DropoffClerkLogin = D.Login ";
			$query.= "WHERE CustomerLogin = '$Email' ";
			$query.= "GROUP BY ReservationNumber ORDER BY StartDate DESC";
			$result = mysql_query($query);

			if (!$result) {
				$response["success"] = 0;
				$response["message"] = "An error occured: ".mysql_error();
				die(json_encode($response));
		    }

			while ($row = mysql_fetch_array($result)){
				//temp reservation array
				$reservation = array();
				$reservation["ReservationNumber"] = $row["ReservationNumber"];
				$reservation["Tools"] = $row["Tools"];
				$reservation["StartDate"] = $row["StartDate"];
				$reservation["EndDate"] = $row["EndDate"];
				$reservation["RentalPrice"] = $row["RentalPrice"];
				$reservation["Deposit"] = $row["Deposit"];
				$reservation["PickupClerk"] = $row["PickupClerk"];
				$reservation["DropoffClerk"] = $row["DropoffClerk"];
				//push single reservation array into final response array;
				array_push($response["reservations"], $reservation);
			}
			 
		} else {
			//no customer found
			$response["success"] = 0;
			$response["message"] = "No customer found";
		}

	} else {
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing";
	}

	//echoing JSON response
	echo json_encode($response);
?>
