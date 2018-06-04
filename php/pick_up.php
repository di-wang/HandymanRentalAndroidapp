<?php

    /* pick_up.php: 
     * update PickupClerkLogin, PickupDate, CreditCardNumber, CreditCardExpirationDate of a reservation
     * $response["success"]: 0 or 1 indicates whether the reservation update successfully
	 * $response["message"]: contains the error message if update failed
	 * $response["success2"]: 0 or 1 indicates whether rental contract retrieve successfully
	 * $response["message2"]: contains the error message if rental contract retrieve failed
	 * if retrieve successfully, $response will contain PickupClerk, Customer, ReservationNumber, CreditCardNumber, StartDate, EndDate, EstimateRental,
     * DepositHeld;
     */

    //set default timezone
    date_default_timezone_set('America/New_York');

	//Create a database connection
	require_once('db_connect.php');
	$connect = db_connect();
	mysql_select_db("HandymanTools") or die( "Unable to select database");

	//array for JSON response
	$response = array();

	if (!empty($_GET["PickupClerkLogin"]) && !empty($_GET["PickupDate"]) && !empty($_GET["CreditCardNumber"]) && !empty($_GET["CreditCardExpirationDate"]) && !empty($_GET["ReservationNumber"])){
		$PickupClerkLogin = mysql_real_escape_string($_GET["PickupClerkLogin"]);
		$CreditCardNumber = mysql_real_escape_string($_GET["CreditCardNumber"]);
		$ReservationNumber = mysql_real_escape_string($_GET["ReservationNumber"]);
		$PickupDate = date($_GET["PickupDate"]);
		$CreditCardExpirationDate = date($_GET["CreditCardExpirationDate"]);

		if ((date('Y-m-d', strtotime($PickupDate)) != $PickupDate)){
	        $response["success"] = 0;
		    $response["message"] = "Please provide a valid pick-up date.";

		} else {
			$query = "UPDATE Reservation ";
			$query.= "SET PickupClerkLogin = '$PickupClerkLogin', PickupDate = '$PickupDate', CreditCardNumber = '$CreditCardNumber', CreditCardExpirationDate = '$CreditCardExpirationDate' ";
			$query.= "WHERE ReservationNumber = '$ReservationNumber' and PickupClerkLogin is NULL";
			$result = mysql_query($query);

			if (!$result) {
				//failed to update reservation
				$response["success"] = 0;
				$response["message"] = "An error occured: ".mysql_error();
			    die(json_encode($response));

			} elseif (mysql_affected_rows() > 0){
		        //successfully updated
		        $response["success"] = 1;
		        $response["message"] = "Pick up successfully.";

		        //Rental Contract
		        //clerk on duty
		        $query = "SELECT CONCAT(FirstName,' ', LastName) AS Name ";
		        $query.= "FROM Clerk ";
		        $query.= "WHERE Login = '$PickupClerkLogin' ";
		        $result = mysql_query($query);
		        if (!$result) {
					$response["success2"] = 0;
					$response["message2"] = "An error occured when displaying rental contract: ".mysql_error();
				    die(json_encode($response));
				}
		        $response["PickupClerk"] = mysql_fetch_array($result)["Name"];

		        //customer
		        $query = "SELECT CONCAT(FirstName,' ', LastName) AS Name ";
		        $query.= "FROM Customer AS C, Reservation AS R ";
		        $query.= "WHERE C.Email = R.CustomerLogin and R.ReservationNumber = '$ReservationNumber' ";
		        $result = mysql_query($query);
		        if (!$result) {
					$response["success2"] = 0;
					$response["message2"] = "An error occured when displaying rental contract: ".mysql_error();
				    die(json_encode($response));
				}
		        $response["Customer"] = mysql_fetch_array($result)["Name"];

		        //reservation
		        $query = "SELECT ReservationNumber, CreditCardNumber, StartDate, EndDate, SUM(Deposit) AS DepositHeld, SUM(DailyRentalPrice)*DATEDIFF(EndDate, StartDate) + SUM(Deposit) AS EstimateRental ";
		        $query.= "FROM Reservation Natural Join ReservationReservesTool Natural Join Tool ";
		        $query.= "WHERE ReservationNumber = '$ReservationNumber' ";
		        $result = mysql_query($query);

		        if (!$result) {
					$response["success2"] = 0;
					$response["message2"] = "An error occured when displaying rental contract: ".mysql_error();
				    die(json_encode($response));
				}
				
		        $row = mysql_fetch_array($result);
		        $response["ReservationNumber"] = $row["ReservationNumber"];
		        $response["CreditCardNumber"] = $row["CreditCardNumber"];
		        $response["StartDate"] = $row["StartDate"];
		        $response["EndDate"] = $row["EndDate"];
		        $response["EstimateRental"] = $row["EstimateRental"];
		        $response["DepositHeld"] = $row["DepositHeld"];
		        $response["success2"] = 1;

		    } else {
		        //reservation has already been picked up
		        $response["success"] = 0;
		        $response["message"] = "The reservation has already been picked up";
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