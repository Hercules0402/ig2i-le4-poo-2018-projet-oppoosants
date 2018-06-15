<?php
    $GLOBALS["addr"] = "projetpoo.c20jnlk2jlcj.eu-west-3.rds.amazonaws.com";
    $GLOBALS["username"] = "ino";
    $GLOBALS["password"] = "inopooxxx";
    $GLOBALS["database"] = "projetpoo";

    function connect() {
        try {
            return new PDO('mysql:host=' . $GLOBALS["addr"] . ';dbname=' . $GLOBALS["database"] . ';charset=utf8', $GLOBALS["username"], $GLOBALS["password"]);
        }
        catch(Exception $e) {
            return false;
        }
    }

    function isConnexionOk() {
        $bdd = connect();
        if ($bdd != false) {
            return true;
        }
        return false;
    }

    function getInstancesSol() {
        $bdd = connect();
        $instance = array();
        $instances = array();

        if (!$bdd) return false;

        $reponse = $bdd->query('SELECT * FROM INSTANCE');

        // On affiche chaque entrée une à une
        while ($donnees = $reponse->fetch()) {
            $instance["ID"] = $donnees["ID"];
            $instance["NOM"] = $donnees["NOM"];

            array_push($instances, $instance);
            $instance = array();
        }

        return $instances;
    }

    function getLocsSol($idInstance) {
        $bdd = connect();
        $loc = array();
        $locs = array();

        if (!$bdd) return false;

        $reponse = $bdd->query('SELECT * FROM LOCATION where NINSTANCE = '. $idInstance);

        // On affiche chaque entrée une à une
        while ($donnees = $reponse->fetch()) {
            $loc["ID"] = $donnees["ID"];
            $loc["ABSCISSE"] = $donnees["ABSCISSE"];
            $loc["IDLOCATION"] = $donnees["IDLOCATION"];
            $loc["NAME"] = $donnees["NAME"];
            $loc["ORDONNEE"] = $donnees["ORDONNEE"];
            $loc["NINSTANCE"] = $donnees["NINSTANCE"];

            array_push($locs, $loc);
            $loc = array();
        }

        return $locs;
    }

    function getLightProductsSol($idInstance) {
        $bdd = connect();
        $prod = array();
        $prods = array();

        if (!$bdd) return false;

        $reponse = $bdd->query('SELECT * FROM PRODUCT where NINSTANCE = '. $idInstance);

        // On affiche chaque entrée une à une
        while ($donnees = $reponse->fetch()) {
            $prod["ID"] = $donnees["ID"];
            $prod["IDPRODUCT"] = $donnees["IDPRODUCT"];
            $prod["VOLUME"] = $donnees["VOLUME"];
            $prod["WEIGHT"] = $donnees["WEIGHT"];
            $prod["LOC"] = $donnees["LOC"];
            $prod["NINSTANCE"] = $donnees["NINSTANCE"];

            array_push($prods, $prod);
            $prod = array();
        }

        return $prods;
    }

    function getTrolleysSol($idInstance) {
        $bdd = connect();
        $trolley = array();
        $trolleys = array();

        if (!$bdd) return false;

        $reponse = $bdd->query('SELECT * FROM TROLLEY WHERE NINSTANCE =' . $idInstance);

        // On affiche chaque entrée une à une
        while ($donnees = $reponse->fetch()) {
            $trolley["ID"] = $donnees["ID"];
            $trolley["IDTROLLEY"] = $donnees["IDTROLLEY"];
            $trolley["NBCOLISMAX"] = $donnees["NBCOLISMAX"];
            $trolley["NINSTANCE"] = $donnees["NINSTANCE"];

            $reponseBis = $bdd->query('SELECT count(*) as NBCOLIS FROM BOX JOIN TROLLEY_BOX ON TROLLEY_BOX.boxes_ID = BOX.ID WHERE TROLLEY_BOX.Trolley_ID = ' . $trolley["ID"]); 
            while ($donneesBis = $reponseBis->fetch()) {
                $trolley["NBCOLIS"] = $donneesBis["NBCOLIS"];
            }

            array_push($trolleys, $trolley);
            $trolley = array();
        }

        return $trolleys;
    }

    function getBoxesSol($idTrolley, $idInstance) {
        $bdd = connect();
        $box = array();
        $boxes = array();

        if (!$bdd) return false;
        $reponse = $bdd->query('SELECT * FROM BOX JOIN TROLLEY_BOX ON TROLLEY_BOX.boxes_ID = BOX.ID WHERE TROLLEY_BOX.Trolley_ID = ' . $idTrolley   . ' AND BOX.NINSTANCE = ' . $idInstance);

        // On affiche chaque entrée une à une
        while ($donnees = $reponse->fetch()) {
            $box["ID"] = $donnees["ID"];
            $box["IDBOX"] = $donnees["IDBOX"];
            $box["VOLUME"] = $donnees["VOLUME"];
            $box["WEIGHT"] = $donnees["WEIGHT"];
            $box["NINSTANCE"] = $donnees["NINSTANCE"];
            $box["ORDER_ID"] = $donnees["ORDER_ID"];

            array_push($boxes, $box);
            $box = array();
        }

        return $boxes;
    }

    function getProductsSol($idBox, $idInstance) {
        $bdd = connect();
        $product = array();
        $products = array();

        if (!$bdd) return false;

        $reponse = $bdd->query('SELECT PRODUCT.ID as id,
        PRODUCT.IDPRODUCT as idP,
        PRODUCT.VOLUME as vol,
        PRODUCT.WEIGHT as we,
        PRODUCT.NINSTANCE as ninst,
        PRODUCT.LOC as loc,
        LOCATION.ABSCISSE as loc_abs,
        LOCATION.ORDONNEE as loc_ordo,
        LOCATION.IDLOCATION as loc_id,
        LOCATION.NAME as loc_name,
        PRODQTY.QUANTITY as qt FROM PRODUCT 
        JOIN PRODQTY ON PRODUCT.ID = PRODQTY.Product_ID
        JOIN BOX_PRODQTY ON BOX_PRODQTY.prodQtys_ID = PRODQTY.ID 
        JOIN BOX ON BOX.ID = BOX_PRODQTY.Box_ID 
		JOIN LOCATION ON LOCATION.ID = PRODUCT.LOC 
        WHERE BOX.ID = ' . $idBox . ' AND PRODUCT.NINSTANCE = ' . $idInstance . ' ORDER BY loc_id');

        // On affiche chaque entrée une à une
        while ($donnees = $reponse->fetch()) {
            $product["ID"] = $donnees["id"];
            //echo $product["ID"] . " + ";
            $product["IDPRODUCT"] = $donnees["idP"];
            $product["VOLUME"] = $donnees["vol"];
            $product["WEIGHT"] = $donnees["we"];
            $product["NINSTANCE"] = $donnees["ninst"];
            $product["LOC"] = $donnees["loc"];
            $product["QUANTITY"] = $donnees["qt"];
            $product["LOC_ABSCISSE"] = $donnees["loc_abs"];
            $product["LOC_ORDONNEE"] = $donnees["loc_ordo"];
            $product["LOC_ID"] = $donnees["loc_id"];
            $product["LOC_NAME"] = $donnees["loc_name"];            
            array_push($products, $product);
            $product = array();
        }
        
        return $products;
    }

    function getTabSol($idInstance) {
        $trolleys = getTrolleysSol($idInstance);
        foreach($trolleys as &$trolley) {
            $trolley["BOXES"] = getBoxesSol($trolley["ID"],$idInstance);
            foreach($trolley["BOXES"] as &$box) {
                $box["PRODUCTS"] = getProductsSol($box["ID"],$idInstance);
            }
        }
        return $trolleys;
    }
?>