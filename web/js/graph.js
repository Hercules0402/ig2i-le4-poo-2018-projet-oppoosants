/**
 * Fonction permettant de récupérer la liste des locations de l'entrepôt.
 */
function setAllLocations() {
    $.ajax({
        type: "POST",
        async: false,
        url: 'php/request.php',
        data: { requestType: "getLocsSol", idInstance: idCurrentInstance },
        success : function(result, statut){ 
            result = JSON.parse(result);
            allLocations = result["content"];
        },
        error : function(result, statut) {
            $("#webContent").html('<br/><br/><hr/><h1 align="center">Erreur : impossible de récupérer les produits/locations</h1><hr/>');
        }
    });
}

/**
 * Fonction permettant de récupérer la liste des produits.
 */
function setAllProducts() {
    $.ajax({
        type: "POST",
        async: false,
        url: 'php/request.php',
        data: { requestType: "getLightProductsSol", idInstance: idCurrentInstance },
        success : function(result, statut){ 
            result = JSON.parse(result);
            allProducts = result["content"];
        },
        error : function(result, statut) {
            $("#webContent").html('<br/><br/><hr/><h1 align="center">Erreur : impossible de récupérer les produits/locations</h1><hr/>');
        }
    });
}

/**
 * Définit le contenu de la tooltip à afficher en fonction du type de point selectionné.
 * @param {*} point 
 * @param {*} typeOfPoint 
 */
function genPointContent(point, typeOfPoint) {
    var content = "";

    switch(typeOfPoint) {
        case("Location") :
        case("Dépôt") :
            content += "Nom : " + point["NAME"] + "<br/>";
            content += "Abscisse : " + point["ABSCISSE"] + "<br/>";
            content += "Ordonnée : " + point["ORDONNEE"];
        break;

        case("Product") :
            content += "Identifiant : " + point["IDPRODUCT"] + "<br/>";
            content += "Volume : " + point["VOLUME"] + "<br/>";
            content += "Poids : " + point["WEIGHT"];
        break;

        default: 
            content += "Erreur de récupération des données";
        break;
    }

    return content;
}

/**
 * Affiche ou cache les locations relatives au graph.
 */
function toggleLocations() {
    if(displayLocations) {
        $('#toggleLocationsC').prop('checked', false);
    }
    else {
        $('#toggleLocationsC').prop('checked', true);
    }

    redraw();
    isGraphic = false;
    drawed = false;
    getGraphe(idCurrentInstance);
    displayLocations = !displayLocations;
}

/**
 * Génère le canvas permettant de dessiner l'entrepôt.
 */
function setup(){
    createCanvas(windowWidth, windowHeight, WEBGL);
    $("#defaultCanvas0").hide();

    $("#defaultCanvas0").mousemove(function(e) {
        var x, y, point, typeOfPoint;
        $("#popup").hide();

        for(p in allPoints) {
            x = parseInt(allPoints[p][0]);
            y = parseInt(allPoints[p][1]);
            point = allPoints[p][2];
            typeOfPoint = allPoints[p][3];

            if (parseInt(x + windowWidth*0.05 - pointSize/2) < e.pageX && 
                parseInt(x + windowWidth*0.05 + pointSize/2) > e.pageX &&
                parseInt(y + windowHeight*0.1 - pointSize/2) < e.pageY &&
                parseInt(y + windowHeight*0.1 + pointSize/2) > e.pageY) {
                
                $("#popup").show();
                $("#popup").css("top", y + "px");
                $("#popup").css("left", x + 80 + "px");
                $("#popup").html(
                    '<div class="card">' +
                    '    <div class="card-header">Point ' + typeOfPoint +' (' + x +','+ y + ')</div>' +
                    '    <div class="card-body">' + genPointContent(point, typeOfPoint) + '</div> ' +
                    '</div>'
                );
            }
        }
    });
  }

/**
 * Permet de d'afficher les produits et les location sur le graph, puis dessine les tournées proposées par la solution.
 */
function draw(){
    if (isGraphic) {
        $("#defaultCanvas0").show();
        if(!drawed) {
            background(100);
            translate(-windowWidth*0.45, -windowHeight*0.4, 0); //décale le point 0,0 depuis le centre de la fenêtre près du coin en haut à gauche
    
            var maxSize = getMaxDistance();
            coeffW = windowWidth/maxSize*0.9;
            coeffH = windowHeight/maxSize*0.8;
    
            if(displayLocations) placeLocations(coeffW, coeffH);
            placeProducts(coeffW, coeffH);
            placeDepots(coeffW, coeffH);
            drawLiaisons(coeffW, coeffH); 
            //console.log(allPoints);
            drawed = true;
        }
    }
    else {
        $("#defaultCanvas0").hide();
    }
}

/**
 * Fonction permettant de dessiner le parcours des trolleys pour une instance donnée.
 * @param {*} coeffW 
 * @param {*} coeffH 
 */
function drawLiaisons(coeffW, coeffH) {
    var lastLocProduct = false;
    var tempLoc = false;

    tabSolution.forEach(function(trolley) {
        stroke(color(getRandomInt(256),getRandomInt(256),getRandomInt(256)));
        trolley["BOXES"].forEach(function(box) {
            box["PRODUCTS"].forEach(function(product){
                allLocations.forEach(function(loc){
                    //console.log(product["LOC"] + " " + loc["IDLOCATION"]);
                    if(loc["ID"] == product["LOC"]) {
                        tempLoc = loc;
                    }
                });
                if(!lastLocProduct) {
                    line(0, 0, parseInt(tempLoc["ABSCISSE"])*coeffW, parseInt(tempLoc["ORDONNEE"])*coeffH);
                }
                else {
                    line(parseInt(tempLoc["ABSCISSE"])*coeffW, parseInt(tempLoc["ORDONNEE"])*coeffH, parseInt(lastLocProduct["ABSCISSE"])*coeffW, parseInt(lastLocProduct["ORDONNEE"])*coeffH);
                }
                lastLocProduct = tempLoc;
                tempLoc = false;
            });
            line(0, 0, parseInt(lastLocProduct["ABSCISSE"])*coeffW, parseInt(lastLocProduct["ORDONNEE"])*coeffH);
            lastProduct = false;
        });
    });
}

/**
 * Permet d'obtenir un entier aléatoirement entre 0 et une valeur max donnée.
 * @param {*} max 
 */
function getRandomInt(max) {
    return Math.floor(Math.random() * Math.floor(max));
}

/**
 * Dessine les locations de l'entrepôt sur le graph.
 * @param {*} coeffW 
 * @param {*} coeffH 
 */
function placeLocations(coeffW, coeffH) {
    //var colorInc = 0;

    for(locId in allLocations) {
        var loc = allLocations[locId];

        if(loc["NAME"] != "depotStart" && loc["NAME"] != "depotEnd") {
            fill(color(255,255,255));
                    
            //console.log(colorInc + " " + (255/allLocations.length));
            //fill(color(colorInc, colorInc, colorInc));
            //colorInc += (255/allLocations.length);
            
            ellipse(parseInt(loc["ABSCISSE"])*coeffW, parseInt(loc["ORDONNEE"])*coeffH,pointSize,pointSize);
            allPoints.push([parseInt(loc["ABSCISSE"])*coeffW, parseInt(loc["ORDONNEE"])*coeffH, loc, "Location"]);
        }
    }
}

/**
 * Dessine les dépôts de l'entrepôt sur le graph.
 * @param {*} coeffW 
 * @param {*} coeffH 
 */
function placeDepots(coeffW, coeffH) {
    for(locId in allLocations) {
        var loc = allLocations[locId];

        if(loc["NAME"] == "depotStart" || loc["NAME"] == "depotEnd") {
            fill(color(255,0,0));
            ellipse(parseInt(loc["ABSCISSE"])*coeffW, parseInt(loc["ORDONNEE"])*coeffH,pointSize,pointSize);
            allPoints.push([parseInt(loc["ABSCISSE"])*coeffW, parseInt(loc["ORDONNEE"])*coeffH, loc, "Dépôt"]);
        }
    }
}

/**
 * Dessine les produits de l'entrepôt sur le graph.
 * @param {*} coeffW 
 * @param {*} coeffH 
 */
function placeProducts(coeffW, coeffH) {
    //var colorInc = 0;

    for(prodId in allProducts) {
        var prod = allProducts[prodId];
        var abs = 0;
        var ord = 0;

        for(locId in allLocations) {
            var loc = allLocations[locId];
            
            if(loc["ID"] == prod["LOC"]) {
                //console.log(prod["LOC"] + " " + locId + " " + parseInt(loc["ABSCISSE"]) + ":" + parseInt(loc["ORDONNEE"]));
                abs = parseInt(loc["ABSCISSE"]);
                ord = parseInt(loc["ORDONNEE"]);
            }
        }

        fill(color(0,255,0));
        //colorInc += (255/allProducts.length);

        /*noFill();
        strokeWeight(0.01);
        stroke(255,0,0);*/
        ellipse(abs*coeffW, ord*coeffH,pointSize,pointSize);
        allPoints.push([abs*coeffW,ord*coeffH, prod,"Product"]);
        
    }
}

/**
 * Récupère la distance maximale à parcourir.
 * @param {*} coeffW 
 * @param {*} coeffH 
 */
function getMaxDistance() {
    var maxSize = 0;
    for(locId in allLocations) {
        var loc = allLocations[locId];

        if(parseInt(loc["ABSCISSE"]) > maxSize) maxSize = parseInt(loc["ABSCISSE"]);
        if(parseInt(loc["ORDONNEE"]) > maxSize) maxSize = parseInt(loc["ORDONNEE"]);
    }

    return maxSize;
}

/**
 * Fonction d'initalisation du graph associé à l'ID d'une instance.
 * @param {*} idInstance 
 */
function getGraphe(idInstance) {
    $("#webContent").html("");
    $("#graphReturnB").show();
    $("#toggleLocations").show();
    $("#mainNavbar").hide();
    allPoints = [];
    setAllLocations();
    setAllProducts();
    setTabSolution(idInstance);
    isGraphic = true;
}