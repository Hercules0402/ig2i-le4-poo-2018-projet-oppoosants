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
            instanceID = idCurrentInstance;
        },
        error : function(result, statut) {
            $("#webContent").html('<br/><br/><hr/><h1 align="center">Erreur : impossible de récupérer les produits/locations</h1><hr/>');
        }
    });
}

/**
 * Fonction qui génère un tableau de couleurs différentes pour l'affichage des points.
 */
function generateTabColors(length) {
    var colors = [];

    var r = 0, g = 0, b = 0;
    var rPassed = false, gPassed = false;

    var inc = parseInt((255*3) / length);
    for(i = 0; i < length; i++) {
        if(r < 256 && !rPassed) {
            r += inc;
        }
        else if (g < 256 && !gPassed) {
            g += inc;
        }
        else {
            b += inc;
        }

        if(r > 255) {
            g += r - 255;
            r = 0;
            rPassed = true;
        }

        if(g > 255) {
            b += g - 255;
            g = 0;
            gPassed = true;
        }
        
        colors.push(color(r, g, b));
    }

    return colors;
}
/**
 * Définit le contenu de la tooltip à afficher en fonction du type de point selectionné.
 * @param {*} point 
 * @param {*} typeOfPoint 
 */
function genPointContent(points) {
    var content = "";
    var point;

    for(idP in points) {
        p = points[idP];
        content += "Récupéré par le trolley " + p[2]["IDTROLLEY"] + ', box ' + p[1]["IDBOX"] + '<br/>';
        content += "Identifiant : " + p[0]["IDPRODUCT"] + " - Volume/u : " + p[0]["VOLUME"] + " - Poids/u : " + p[0]["WEIGHT"] + " - Qt : " + p[0]["QUANTITY"];
        if(idP < points.length - 1) content += '<hr/>';
    
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

    resetGraph();
    displayLocations = !displayLocations;
}

/**
 * Affiche ou cache les locations relatives au graph.
 */
function toggleLines() {
    if(displayLines) {
        $('#toggleLinesC').prop('checked', false);
    }
    else {
        $('#toggleLinesC').prop('checked', true);
    }

    resetGraph();
    displayLines = !displayLines;
}

/**
 * Affiche ou cache le point bougeant qui représente le trolley parcourant l'entrepôt.
 */
function togglePoint() {
    if(displayPoint) {
        $('#togglePointC').prop('checked', true);
        startPoint();
    }
    else {
        $('#togglePointC').prop('checked', false);
        movingPState = "end";
    }

    resetGraph();
    displayPoint = !displayPoint;
}


/**
 * Permet de redessiner le graph.
 */
function resetGraph() {
    redraw();
    isGraphic = false;
    drawed = false;
    getGraphe(idCurrentInstance);
}

function resetGraphAndPoint() {
    movingPState = "end";
    displayPoint = false;
    $('#togglePointC').prop('checked', false);

    resetGraph();
}

/**
 * Génère le canvas permettant de dessiner l'entrepôt.
 */
function setup(){
    createCanvas(windowWidth, windowHeight, WEBGL);
    $("#defaultCanvas0").hide();
    //initialise le canvas mais ne l'affiche pas si on est pas sur le graphe mais sur l'affichage tableau

    $("#defaultCanvas0").mousemove(function(e) {
        //si on bouge sur le canvas, on regarde si le curseur est sur un point : si oui, on affiche le popup d'information
        var x, y, lastX, lastY, point, typeOfPoint;
        var touchedP = [];
        var touched = false;
        $("#popup").hide();

        tabSolution.forEach(function(trolley) {
            trolley["BOXES"].forEach(function(box) {
                box["PRODUCTS"].forEach(function(p) {
                    x = parseInt(p["LOC_ABSCISSE"] *coeffW);
                    y = parseInt(p["LOC_ORDONNEE"] * coeffH);
                    if (parseInt(x + windowWidth*0.05 - pointSize/2) < e.pageX && 
                        parseInt(x + windowWidth*0.05 + pointSize/2) > e.pageX &&
                        parseInt(y + windowHeight*0.1 - pointSize/2) < e.pageY &&
                        parseInt(y + windowHeight*0.1 + pointSize/2) > e.pageY) {

                        touchedP.push([p, box, trolley]);
                        lastX = x;
                        lastY = y;
                        touched = true;
                    }
                });
            });
        });

        if(touched) {
            $("#popup").css("top", lastY + 40 + "px");
            $("#popup").css("left", lastX + 80 + "px");
            $("#popup").html(
                '<div class="card">' +
                '    <div class="card-header">Point Produit (' + (lastX/coeffW).toFixed(0) +','+ (lastY/coeffH).toFixed(0) + ')</div>' +
                '    <div class="card-body" style="max-height: 200px;overflow-y:auto;">' + genPointContent(touchedP) + '</div> ' +
                '</div>'
            );
            $("#popup").show();
        }
    });

    $(window).resize(function() {
        createCanvas(windowWidth, windowHeight, WEBGL);
        if(!isGraphic) $("#defaultCanvas0").hide();
        else processGraphe();
    });
  }

/**
 * Fonction exécutée en boucle après lancement de la page web
 * Permet de d'afficher les produits et les location sur le graph,
 * puis dessine les tournées proposées par la solution et déplace le point mouvant s'il est activé.
 */
function draw(){
    //si on est sur le graphe
    if (isGraphic) {
        processGraphe();
    }
    else {
        $("#defaultCanvas0").hide();
    }
}

function processGraphe() {
    $("#defaultCanvas0").show();

    var maxSize = getMaxDistance();
    var selectedTrolleyID = $("#trolleySelection" ).val();
    
    coeffW = windowWidth/maxSize*0.9;
    coeffH = windowHeight/maxSize*0.8;
    calcItineraireTrolley();
    background(100);
    translate(-windowWidth*0.45, -windowHeight*0.4, 0); //décale le point 0,0 depuis le centre de la fenêtre près du coin en haut à gauche

    if(displayLocations) placeLocations(); 
    placeDepots();
    if (selectedTrolleyID == tabSolution.length) {
        //Affichage de tous les trolleys
        if(displayLines) drawLiaisonsFromInstance();
        drawProductsFromInstance();
    } else {
        if(displayLines) drawLiaisonsFromTrolley(selectedTrolleyID);
        drawProductsFromTrolley(selectedTrolleyID);
    }

    processMovingP();
}
/**
 * Fonction permettant de gérer les différents cas associés au point bougeant.
 */

function processMovingP() {
    switch(movingPState) {
        case "start" :
            startPoint();
        break;

        case "move" :
            movePoint();
        break;

        case "pause" :
            pausePoint();
        break;

        case "error" :
        case "end" :
        default : 
        break;
    }
}

/**
 * Fonction qui initialise le point et le met en marche.
 */

function startPoint() {
    for(locId in allLocations) {
        var loc = allLocations[locId];

        if(loc["NAME"] == "depotStart") {
            xP = loc["ABSCISSE"] * coeffW;
            yP = loc["ORDONNEE"] * coeffH;
            movingPState = "move";
            lastVisitedP = 0;
            return;
        }
        else {
            movingPState = "error";
        }
    }
}


/**
 * Fonction qui met en pause le point.
 */

function pausePoint() {
    ellipse(xP*coeffW, yP*coeffH, 8, 8);
}


/**
 * Fonction permettant de gérer le déplacement du point en fonction de la vitesse demandée et aussi de la taille de sa trace à laisser sur le graphe.
 */

function movePoint() {
    if (typeof pointsOrder[lastVisitedP + 1] !== 'undefined') {
        
        var diffX = (pointsOrder[lastVisitedP + 1][0]) - (pointsOrder[lastVisitedP][0]);
        var diffY = (pointsOrder[lastVisitedP + 1][1]) - (pointsOrder[lastVisitedP][1]);
        var stopX = pointsOrder[lastVisitedP + 1][0] - xP; 
        var stopY = pointsOrder[lastVisitedP + 1][1] - yP; 
        
        xP += diffX*speed/1000;
        yP += diffY*speed/1000;
        
        movingHistoric.push([xP, yP]);
        if(movingHistoric.length > historicSize) movingHistoric.shift();

        fill(color(0));

        var sizeMH = 0;
        movingHistoric.forEach(function(point) {
            ellipse(point[0]*coeffW, point[1]*coeffH, sizeMH/4, sizeMH/4);
            sizeMH++;
        });

        if (stopX < 1 && stopX > -1 && stopY < 1 && stopY > -1) {
            lastVisitedP++;
            xP = pointsOrder[lastVisitedP][0];
            yP = pointsOrder[lastVisitedP][1];
        }
        
    }
    else {
        movingPState = "end";
    }
}

/**
 * Fonction permettant de dessiner les produits d'un trolley, avec une couleur identique s'il est issu du même box.
 * @param {*} coeffW 
 * @param {*} coeffH 
 */
function drawProductsFromTrolley(selectedTrolleyID) {
    
    stroke(0);
    var abs = 0;
    var ord = 0;
    var color;
    //Récupère les boxes correspondantes au trolley sélectionné
    var boxes = tabSolution[selectedTrolleyID].BOXES;
    $('#boxSelection').empty();

    colors = generateTabColors(boxes.length);

    //Lecture des produits pour chacunes des boxes (une couleur est générée pour chaque box)
    for (var j = 0; j < boxes.length; j++){

        var box = boxes[j].PRODUCTS;
        color = colors[j];
            
        $('#boxSelection').append($('<span>', {
            class: 'badge badge-primary',
            text: 'Box ID: '+boxes[j].IDBOX+' (Order: '+boxes[j].ORDER_ID+')',
            style: 'background-color:'+color+';'
        }));
            
        //Positionnement des produits avec leur coordonnées et couleur respectives
        for (var k = 0; k < box.length; k++){
            abs = parseInt(box[k].LOC_ABSCISSE);
            ord = parseInt(box[k].LOC_ORDONNEE);
            fill(color);
            ellipse(abs*coeffW, ord*coeffH,pointSize,pointSize);
        }
    }
}

/**
 * Fonction permettant de dessiner les produits de tous les trolleys, avec une couleur identique s'il est issu du même trolley.
 * @param {*} coeffW 
 * @param {*} coeffH 
 */
function drawProductsFromInstance() {
    
    stroke(0);
    $('#boxSelection').empty();
    var abs = 0;
    var ord = 0;
    var color;
    var colors = generateTabColors(tabSolution.length);

    for (var f = 0; f < tabSolution.length; f++){
        //Récupère les trolleys correspondantes à l'instance sélectionnée
        var boxes = tabSolution[f].BOXES;
        color = colors[f];

        $('#boxSelection').append($('<span>', {
            class: 'badge badge-primary',
            text: 'Trolley ID: '+tabSolution[f].IDTROLLEY,
            style: 'background-color:'+color+';'
        }));

        //Lecture des produits pour chacunes des trolleys (une couleur est générée pour chaque trolley)
        for (var j = 0; j < boxes.length; j++){

            var box = boxes[j].PRODUCTS;

            //Positionnement des produits avec leur coordonnées et couleur respectives
            for (var k = 0; k < box.length; k++){
                abs = parseInt(box[k].LOC_ABSCISSE);
                ord = parseInt(box[k].LOC_ORDONNEE);
                fill(color);
                ellipse(abs*coeffW, ord*coeffH,pointSize,pointSize);
            }
        }
    }
}

/**
 * Fonction permettant de dessiner le parcours des boxes pour un trolley donné.
 * @param {*} coeffW 
 * @param {*} coeffH 
 */
function drawLiaisonsFromTrolley(selectedTrolleyID) {
    var lastProduct = false;
    var boxes = tabSolution[selectedTrolleyID].BOXES;
    var colors = generateTabColors(boxes.length);

    for (var j = 0; j < boxes.length; j++){
        box = boxes[j];
        stroke(color(colors[j]));
        box["PRODUCTS"].forEach(function(p){
            if(!lastProduct) {
                line(0, 0, parseInt(p["LOC_ABSCISSE"])*coeffW, parseInt(p["LOC_ORDONNEE"])*coeffH);
            }
            else {
                line(parseInt(lastProduct["LOC_ABSCISSE"])*coeffW, parseInt(lastProduct["LOC_ORDONNEE"])*coeffH, parseInt(p["LOC_ABSCISSE"])*coeffW, parseInt(p["LOC_ORDONNEE"])*coeffH);
            }
            lastProduct = p;
        });
    }
    line(0, 0, parseInt(lastProduct["LOC_ABSCISSE"])*coeffW, parseInt(lastProduct["LOC_ORDONNEE"])*coeffH);
}

/**
 * Fonction permettant de dessiner le parcours des trolleys pour une instance donnée.
 * @param {*} coeffW 
 * @param {*} coeffH 
 */
function drawLiaisonsFromInstance() {
    var lastProduct = false;var trolley;
    colors = generateTabColors(tabSolution.length);

    for (var j = 0; j < tabSolution.length; j++){
        trolley = tabSolution[j];
        stroke(color(colors[j]));
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
        });
        line(0, 0, parseInt(lastProduct["LOC_ABSCISSE"])*coeffW, parseInt(lastProduct["LOC_ORDONNEE"])*coeffH);
        lastProduct = false;
    }
}


/**
 * Fonction permettant de calculer l'itinéraire que parcourera le chariot lorsque affiché sur le graphe sous forme de point.
 */

function calcItineraireTrolley() {
    var lastProduct = false;
    var selectedTrolleyID = $("#trolleySelection" ).val();
    
    pointsOrder = [];
    pointsOrder.push([0,0]);

    if (selectedTrolleyID != tabSolution.length) {
        var trolley = tabSolution[selectedTrolleyID];

        stroke(color(255,255,255));
        trolley["BOXES"].forEach(function(box) {
            box["PRODUCTS"].forEach(function(p){
                pointsOrder.push([parseInt(p["LOC_ABSCISSE"]), parseInt(p["LOC_ORDONNEE"])]);
            });
        });
        pointsOrder.push([0,0]);
    }
    else {
        var trolley = tabSolution[selectedTrolleyID];

        stroke(color(255,255,255));
        tabSolution.forEach(function(trolley) {
            trolley["BOXES"].forEach(function(box) {
                box["PRODUCTS"].forEach(function(p){
                    pointsOrder.push([parseInt(p["LOC_ABSCISSE"]), parseInt(p["LOC_ORDONNEE"])]);
                });    
            });
            pointsOrder.push([0,0]);
        });      
    }
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
    stroke(0);

    for(locId in allLocations) {
        var loc = allLocations[locId];

        if(loc["NAME"] != "depotStart" && loc["NAME"] != "depotEnd") {
            fill(color(255,255,255));
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
    stroke(0);

    for(locId in allLocations) {
        var loc = allLocations[locId];

        if(loc["NAME"] == "depotStart" || loc["NAME"] == "depotEnd") {
            fill(color(255,0,0));
            ellipse(parseInt(loc["ABSCISSE"])*coeffW, parseInt(loc["ORDONNEE"])*coeffH,pointSize,pointSize);
        }
    }
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
* Fonction permettant de remplir le bouton de selection des trolleys utilisés dans une solution.
*/
function setupTrolleySelection(idInstance) {
    $('#trolleySelection').empty();
    var size = tabSolution.length;
    for (var i = 0; i < size; i++){
        $('#trolleySelection').append($('<option>', {
            value: i,
            text: 'Instance '+tabSolution[i].NINSTANCE+"/Trolley "+tabSolution[i].IDTROLLEY
        }));
    }
    //Option d'affichage de tous les trolleys
    $('#trolleySelection').append($('<option>', {
        value: size,
        text: 'Instance '+ idInstance +"/All Trolleys"
    }));
}

/**
 * Fonction d'initalisation du graph associé à l'ID d'une instance.
 * @param {*} idInstance 
 */
function getGraphe(idInstance) {
    if (idInstance != idCurrentInstance) firstDraw = true;
    $("#webContent").html("");
    $("#graphReturnB").show();
    $("#toggleLocations").show();
    $("#toggleLines").show();
    $("#togglePoint").show();
    $("#trolleySelection").show();
    $("#boxSelection").show();
    setTabSolution(idInstance);
    setAllLocations();
    isGraphic = true;
    if (firstDraw) {
        setupTrolleySelection(idInstance);//doit être appellée qu'à l'initialisation du graph
        firstDraw = false;
    }
}