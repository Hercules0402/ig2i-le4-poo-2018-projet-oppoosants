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

            case "getInstancesSol" :
                if(isConnexionOk()) {
                    $response["status"] = "success";
                    $response["content"] = getInstancesSol();
                }
            break;

            case "getLocsSol" :
                $idInstance = $_POST["idInstance"]; 

                if(isConnexionOk()) {
                    $response["status"] = "success";
                    $response["content"] = getLocsSol($idInstance);
                }
            break;

            case "getTabSol" :
                $idInstance = $_POST["idInstance"]; 

                if(isConnexionOk()) {
                    $response["status"] = "success";
                    $response["content"] = getTabSol($idInstance);
                }
            break;

            case "getLightProductsSol" :
            $idInstance = $_POST["idInstance"]; 

            if(isConnexionOk()) {
                $response["status"] = "success";
                $response["content"] = getLightProductsSol($idInstance);
            }
        break;

            case "getTrolleysSol" :
                $idInstance = $_POST["idInstance"]; 

                if(isConnexionOk() && isset($idInstance)) {
                    $response["status"] = "success";
                    $response["content"] = getTrolleysSol($idInstance);
                }
            break;

            case "getBoxesSol":
                $idTrolley = $_POST["idTrolley"];
                $idInstance = $_POST["idInstance"];

                if(isConnexionOk() && isset($idTrolley) && isset($idInstance)) {
                    $response["status"] = "success";
                    $response["content"] = getBoxesSol($idTrolley, $idInstance);
                }
            break;

            case "getProductsSol":
                $idBox = $_POST["idBox"];
                $idInstance = $_POST["idInstance"];

                if(isConnexionOk() && isset($idBox) && isset($idInstance)) {
                    $response["status"] = "success";
                    $response["content"] = getProductsSol($idBox, $idInstance);
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