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
 * Définit le contenu de la tooltip à afficher en fonction du type de point selectionné.
 * @param {*} point 
 * @param {*} typeOfPoint 
 */
function genPointContent(p) {
    var content = "";

    content += "Identifiant : " + p["IDPRODUCT"] + "<br/>";
    content += "Volume : " + p["VOLUME"] + "<br/>";
    content += "Poids : " + p["WEIGHT"];

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

        tabSolution.forEach(function(trolley) {
            trolley["BOXES"].forEach(function(box) {
                box["PRODUCTS"].forEach(function(p) {
                    x = parseInt(p["LOC_ABSCISSE"]);
                    y = parseInt(p["LOC_ORDONNEE"]);
                    if (parseInt((x * coeffW) + windowWidth*0.05 - pointSize/2) < e.pageX && 
                        parseInt((x * coeffW) + windowWidth*0.05 + pointSize/2) > e.pageX &&
                        parseInt((y * coeffH) + windowHeight*0.1 - pointSize/2) < e.pageY &&
                        parseInt((y * coeffH) + windowHeight*0.1 + pointSize/2) > e.pageY) {

                        $("#popup").show();
                        $("#popup").css("top", (y * coeffH) + "px");
                        $("#popup").css("left", (x * coeffW) + 80 + "px");
                        $("#popup").html(
                            '<div class="card">' +
                            '    <div class="card-header">Point Produit (' + x +','+ y + ')</div>' +
                            '    <div class="card-body">' + genPointContent(p) + '</div> ' +
                            '</div>'
                        );
                    }
                });
            });
        });
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
    
            if(displayLocations) placeLocations();
            placeProducts();
            placeDepots();
            drawLiaisons(); 
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
function drawLiaisons() {
    var lastProduct = false;

    tabSolution.forEach(function(trolley) {
        stroke(color(getRandomInt(256),getRandomInt(256),getRandomInt(256)));
        trolley["BOXES"].forEach(function(box) {
            box["PRODUCTS"].forEach(function(p){
                if(!lastProduct) {
                    line(0, 0, parseInt(p["LOC_ABSCISSE"])*coeffW, parseInt(p["LOC_ORDONNEE"])*coeffH);
                }
                else {
                    line(parseInt(lastProduct["LOC_ABSCISSE"])*coeffW, parseInt(lastProduct["LOC_ORDONNEE"])*coeffH, parseInt(p["LOC_ABSCISSE"])*coeffW, parseInt(p["LOC_ORDONNEE"])*coeffH);
                }
                lastProduct = p;
            });
            line(0, 0, parseInt(lastProduct["LOC_ABSCISSE"])*coeffW, parseInt(lastProduct["LOC_ORDONNEE"])*coeffH);
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
function placeLocations() {
    //var colorInc = 0;

    for(locId in allLocations) {
        var loc = allLocations[locId];

        if(loc["NAME"] != "depotStart" && loc["NAME"] != "depotEnd") {
            fill(color(255,255,255));
                    
            //console.log(colorInc + " " + (255/allLocations.length));
            //fill(color(colorInc, colorInc, colorInc));
            //colorInc += (255/allLocations.length);
            
            ellipse(parseInt(loc["ABSCISSE"])*coeffW, parseInt(loc["ORDONNEE"])*coeffH,pointSize,pointSize);
        }
    }
}

/**
 * Dessine les dépôts de l'entrepôt sur le graph.
 * @param {*} coeffW 
 * @param {*} coeffH 
 */
function placeDepots() {
    for(locId in allLocations) {
        var loc = allLocations[locId];

        if(loc["NAME"] == "depotStart" || loc["NAME"] == "depotEnd") {
            fill(color(255,0,0));
            ellipse(parseInt(loc["ABSCISSE"])*coeffW, parseInt(loc["ORDONNEE"])*coeffH,pointSize,pointSize);
        }
    }
}

/**
 * Dessine les produits de l'entrepôt sur le graph.
 * @param {*} coeffW 
 * @param {*} coeffH 
 */
function placeProducts() {
    var abs = 0;
    var ord = 0;

    tabSolution.forEach(function(trolley) {
        trolley["BOXES"].forEach(function(box) {
            box["PRODUCTS"].forEach(function(p) {
                abs = parseInt(p["LOC_ABSCISSE"]);
                ord = parseInt(p["LOC_ORDONNEE"]);

                fill(color(0,255,0));
                ellipse(abs*coeffW, ord*coeffH,pointSize,pointSize);
            });
        });
    });
}

/**
 * Récupère la distance maximale à parcourir.
 * @param {*} coeffW 
 * @param {*} coeffH 
 */
function getMaxDistance() {
    var maxSize = 0;
    tabSolution.forEach(function(trolley) {
        trolley["BOXES"].forEach(function(box) {
            box["PRODUCTS"].forEach(function(p) {
                if(parseInt(p["LOC_ABSCISSE"]) > maxSize) maxSize = parseInt(p["LOC_ABSCISSE"]);
                if(parseInt(p["LOC_ORDONNEE"]) > maxSize) maxSize = parseInt(p["LOC_ORDONNEE"]);
            });
        });
    });

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
    setTabSolution(idInstance);
    setAllLocations();
    isGraphic = true;
}