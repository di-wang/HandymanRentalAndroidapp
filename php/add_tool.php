<?php
    /* add_tool.php: 
     * $response["success"]: 1 -- successfully add tools
                             0 -- error
     * $response["message"]: contains the error message if failed
     */

    //Create a database connection
    require_once('db_connect.php');
    $connect = db_connect();
    mysql_select_db("HandymanTools") or die( "Unable to select database");

    //array for JSON response
    $response = array();
    
    if (!empty($_GET['ToolID']) && !empty($_GET['ToolType']) && !empty($_GET['AbbrDescription']) && !empty($_GET['FullDescription']) && !empty($_GET['PurchasePrice'])
     && !empty($_GET['DailyRentalPrice']) && !empty($_GET['Deposit']) && !empty($_GET['AddClerkLogin']) && !empty($_GET['Accessories'])){
        $ToolID = mysql_real_escape_string($_GET['ToolID']);
        $ToolType = mysql_real_escape_string($_GET['ToolType']);
        $AbbrDescription = mysql_real_escape_string($_GET['AbbrDescription']);
        $FullDescription = mysql_real_escape_string($_GET['FullDescription']);
        $PurchasePrice = mysql_real_escape_string($_GET['PurchasePrice']);
        $DailyRentalPrice = mysql_real_escape_string($_GET['DailyRentalPrice']);
        $Deposit = mysql_real_escape_string($_GET['Deposit']);
        $AddClerkLogin = mysql_real_escape_string($_GET['AddClerkLogin']);
        $Accessories = array();
        $Accessories = $_GET['Accessories'];

        $query = "INSERT INTO Tool ";
        $query.= "VALUES ('$ToolID', '$ToolType', '$AbbrDescription', '$FullDescription', '$PurchasePrice', '$DailyRentalPrice', '$Deposit', '$AddClerkLogin', NULL, NULL, NULL) ";
        $result = mysql_query($query);

        if ($result){
            if ($ToolType == "Power Tool"){
                foreach ($Accessories as $Accessary){
                    $query = "INSERT INTO PowerToolAccessories VALUES ('$ToolID', '$Accessary')";
                    $result = mysql_query($query);
                    if (!$result){
                        mysql_query("DELETE FROM PowerToolAccessories WHERE ToolID = '$ToolID'");
                        mysql_query("DELETE FROM Tool WHERE ToolID = '$ToolID'");
                        $response["success"] = 0;
                        $response["message"] = "Failed to add accessories.".mysql_error();
                        die(json_encode($response));
                    }
                }
            } 
            $response["success"] = 1;
            $response["message"] = "Tool is successfully added.";
        } else {
            $response["success"] = 0;
            $response["message"] = "Failed to add tool: ".mysql_error();
        }
    } else {
      $response["success"] = 0;
      $response["message"] = "Required field(s) is missing.";
    }
    
    //echoing JSON response
    echo json_encode($response);
?>