<?php 
    include_once 'functions.php';
    
    $response = array();
    if (isset($_POST["requestType"])) {
        switch ($_POST["requestType"]) {

            case "testConnexion" :
                if(isConnexionOk()) {
                    $response["status"] = "success";
                    $response["content"] = "OK";
                }
                else {
                    $response["status"] = "error";
                    $response["errorMsg"] = "Can't etablish a connexion with a database";
                }
            break; 

            case "getTrolleysSol" :
                if(isConnexionOk()) {
                    $response["status"] = "success";
                    $response["content"] = getTrolleysSol();
                }
            break;

            case "getBoxesSol":
                $idTrolley = $_POST["idTrolley"];

                if(isConnexionOk() && isset($idTrolley)) {
                    $response["status"] = "success";
                    $response["content"] = getBoxesSol($idTrolley);
                }
            break;

            case "getProductsSol":
                $idBox = $_POST["idBox"];

                if(isConnexionOk() && isset($idBox)) {
                    $response["status"] = "success";
                    $response["content"] = getProductsSol($idBox);
                }
            break;

            default :
                $response["status"] = "error";
                $response["errorMsg"] = "Unknown parameter";
            break;
        }
    }
    else {
        $response["status"] = "error";
        $response["errorMsg"] = "Missing parameter";
    }

    echo json_encode($response);
?>