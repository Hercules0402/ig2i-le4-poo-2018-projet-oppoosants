var firstDraw = true;
var colors = ['#ff9900','#00cc00','#0066cc','#ff00ff','#ffff00','#00ffcc'];//Orange,Vert,Bleu,Fushia,Jaune,Cyan

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
        startPoint();
    }
    else {
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

/**
 * Génère le canvas permettant de dessiner l'entrepôt.
 */
function setup(){
    createCanvas(windowWidth, windowHeight, WEBGL);
    $("#defaultCanvas0").hide();

    $("#defaultCanvas0").mousemove(function(e) {
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
  }

/**
 * Permet de d'afficher les produits et les location sur le graph, puis dessine les tournées proposées par la solution.
 */
function draw(){
    if (isGraphic) {
        $("#defaultCanvas0").show();
        if(!drawed) {
            var maxSize = getMaxDistance();
            coeffW = windowWidth/maxSize*0.9;
            coeffH = windowHeight/maxSize*0.8;
            calcItineraireTrolley();
            drawed = true;
        }
        background(100);
        translate(-windowWidth*0.45, -windowHeight*0.4, 0); //décale le point 0,0 depuis le centre de la fenêtre près du coin en haut à gauche
    
        if(displayLocations) placeLocations();
        if(displayLines) drawLiaisonsFromTrolley(); 
        placeDepots();
        drawProductsFromTrolley();

        processMovingP();
    }
    else {
        $("#defaultCanvas0").hide();
    }
}

/**
 * Fonction permettant de gérer les différents cas associés au point bougeant
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

function pausePoint() {
    ellipse(xP*coeffW, yP*coeffH, 8, 8);
}

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
        movingHistoric.forEach(function(point) {
            ellipse(point[0]*coeffW, point[1]*coeffH, 8, 8);
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
function drawProductsFromTrolley() {
    
    //console.log(tabSolution);
    stroke(0);
    var abs = 0;
    var ord = 0;
    var color;
    var selectedTrolleyID = $("#trolleySelection" ).val();
    
    //Récupère les boxes correspondantes au trolley sélectionné
    var boxes = tabSolution[selectedTrolleyID].BOXES;
    $('#boxSelection').empty();

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
 * Fonction permettant de dessiner le parcours des trolleys pour une instance donnée.
 * @param {*} coeffW 
 * @param {*} coeffH 
 */
function drawLiaisonsFromTrolley() {
    var lastProduct = false;
    var selectedTrolleyID = $("#trolleySelection" ).val();
    var trolley = tabSolution[selectedTrolleyID];

    stroke(color(255,255,255));
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
}

function calcItineraireTrolley() {
    var lastProduct = false;
    var selectedTrolleyID = $("#trolleySelection" ).val();
    var trolley = tabSolution[selectedTrolleyID];

    pointsOrder = [];
    pointsOrder.push([0,0]);

    stroke(color(255,255,255));
    trolley["BOXES"].forEach(function(box) {
        box["PRODUCTS"].forEach(function(p){
            pointsOrder.push([parseInt(p["LOC_ABSCISSE"]), parseInt(p["LOC_ORDONNEE"])]);
        });
        pointsOrder.push([0,0]);
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
    stroke(0);

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
function setupTrolleySelection() {
    $('#trolleySelection').empty();
    for (var i = 0; i < tabSolution.length; i++){
        $('#trolleySelection').append($('<option>', {
            value: i,
            text: 'Instance '+tabSolution[i].NINSTANCE+"/Trolley "+tabSolution[i].IDTROLLEY
        }));
    }
}

/**
 * Fonction d'initalisation du graph associé à l'ID d'une instance.
 * @param {*} idInstance 
 */
function getGraphe(idInstance) {
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
        setupTrolleySelection();//doit être appellée qu'à l'initialisation du graph
        firstDraw = false;
    }
}