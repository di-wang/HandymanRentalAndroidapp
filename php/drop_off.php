<?php
    /* drop_off.php: 
     * update DropoffClerkLogin, DropoffDate of a reservation
     * $response["success"]: 0 or 1 indicates whether the reservation update successfully
	 * $response["message"]: contains the error message if update failed
	 * $response["success2"]: 0 or 1 indicates whether rental receipt retrieve successfully
	 * $response["message2"]: contains the error message if rental receipt retrieve failed
	 * if retrieve successfully, $response will contain DropoffClerk, Customer, ReservationNumber, CreditCardNumber, StartDate, EndDate, RentalPrice,
     * DepositHeld, Total;
     */

	//Create a database connection
	require_once('db_connect.php');
	$connect = db_connect();
	mysql_select_db("HandymanTools") or die( "Unable to select database");

	//set default timezone
    date_default_timezone_set('America/New_York');

	//array for JSON response
	$response = array();

	if (!empty($_GET["ReservationNumber"]) && !empty($_GET["DropoffDate"]) && !empty($_GET["DropoffClerkLogin"])){
		$ReservationNumber = mysql_real_escape_string($_GET["ReservationNumber"]);
		$DropoffClerkLogin = mysql_real_escape_string($_GET["DropoffClerkLogin"]);
		$DropoffDate = date($_GET["DropoffDate"]);
        //check if DropoffDate is valid
		if ((date('Y-m-d', strtotime($DropoffDate)) != $DropoffDate)){
	        $response["success"] = 0;
		    $response["message"] = "Please provide a valid drop-off date.";

		} else {
			//update reservation
			$query = "UPDATE Reservation ";
			$query.= "SET DropoffClerkLogin = '$DropoffClerkLogin', DropoffDate = '$DropoffDate' ";
			$query.= "WHERE ReservationNumber = '$ReservationNumber' and DropoffClerkLogin is NULL";
			$result = mysql_query($query);

			if (!$result) {
				//failed to update reservation
				$response["success"] = 0;
				$response["message"] = "An error occured: ".mysql_error();
			    die(json_encode($response));

			} elseif (mysql_affected_rows() > 0){
		        //successfully updated
		        $response["success"] = 1;
		        $response["message"] = "Drop off successfully.";

		        //Rental Receipt
		        //clerk on duty
		        $query = "SELECT CONCAT(FirstName,' ', LastName) AS Name ";
		        $query.= "FROM Clerk ";
		        $query.= "WHERE Login = '$DropoffClerkLogin' ";
		        $result = mysql_query($query);
		        if (!$result) {
					$response["success2"] = 0;
					$response["message2"] = "An error occured when displaying rental receipt: ".mysql_error();
				    die(json_encode($response));
				}
		        $response["DropoffClerk"] = mysql_fetch_array($result)["Name"];

		        //customer
		        $query = "SELECT CONCAT(FirstName,' ', LastName) AS Name ";
		        $query.= "FROM Customer AS C, Reservation AS R ";
		        $query.= "WHERE C.Email = R.CustomerLogin and R.ReservationNumber = '$ReservationNumber' ";
		        $result = mysql_query($query);
		        if (!$result) {
					$response["success2"] = 0;
					$response["message2"] = "An error occured when displaying rental receipt: ".mysql_error();
				    die(json_encode($response));
				}
		        $response["Customer"] = mysql_fetch_array($result)["Name"];

		        //reservation
		        $query = "SELECT ReservationNumber, CreditCardNumber, StartDate, EndDate, SUM(DailyRentalPrice)*DATEDIFF(EndDate, StartDate) + SUM(Deposit) AS RentalPrice, -SUM(Deposit) AS DepositHeld, SUM(DailyRentalPrice)*DATEDIFF(EndDate, StartDate) As Total ";
		        $query.= "FROM Reservation Natural Join ReservationReservesTool Natural Join Tool ";
		        $query.= "WHERE ReservationNumber = '$ReservationNumber' ";
		        $result = mysql_query($query);

		        if (!$result) {
					$response["success2"] = 0;
					$response["message2"] = "An error occured when displaying rental receipt: ".mysql_error();
				    die(json_encode($response));
				}
				
		        $row = mysql_fetch_array($result);
		        $response["ReservationNumber"] = $row["ReservationNumber"];
		        $response["CreditCardNumber"] = $row["CreditCardNumber"];
		        $response["StartDate"] = $row["StartDate"];
		        $response["EndDate"] = $row["EndDate"];
		        $response["RentalPrice"] = $row["RentalPrice"];
		        $response["DepositHeld"] = $row["DepositHeld"];
		        $response["Total"] = $row["Total"];
		        $response["success2"] = 1;

		    } else {
		        // reservation has already been dropped off.
				$response["success"] = 0;
				$response["message"] = "The reservation has already been dropped off.";
			    die(json_encode($response));
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