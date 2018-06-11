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
            instanceID = idCurrentInstance;
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
        var x, y, point, typeOfPoint;
        var touchedP = [];
        var touched = false;
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

                        touchedP.push([p, box, trolley]);
                        touched = true;
                    }
                });
            });
        });

        if(touched) {
            $("#popup").show();
            $("#popup").css("top", (y * coeffH) + "px");
            $("#popup").css("left", (x * coeffW) + 80 + "px");
            $("#popup").html(
                '<div class="card">' +
                '    <div class="card-header">Point Produit (' + x +','+ y + ')</div>' +
                '    <div class="card-body">' + genPointContent(touchedP) + '</div> ' +
                '</div>'
            );
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

            var selectedTrolleyID = $("#trolleySelection" ).val();
    
            if(displayLocations) placeLocations();
            placeDepots();
            if (selectedTrolleyID == tabSolution.length) {
                //Affichage de tous les trolleys
                drawLiaisonsFromInstance();
                drawProductsFromInstance();
            } else {
                drawLiaisonsFromTrolley(selectedTrolleyID);
                drawProductsFromTrolley(selectedTrolleyID);
            }
            drawed = true;
        }
    }
    else {
        $("#defaultCanvas0").hide();
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
    console.log(selectedTrolleyID);
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
        line(0, 0, parseInt(lastProduct["LOC_ABSCISSE"])*coeffW, parseInt(lastProduct["LOC_ORDONNEE"])*coeffH);
        lastProduct = false;
    }
}

/**
 * Fonction permettant de dessiner le parcours des trolleys pour une instance donnée.
 * @param {*} coeffW 
 * @param {*} coeffH 
 */
function drawLiaisonsFromInstance() {
    var lastProduct = false;var trolley;

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
            line(0, 0, parseInt(lastProduct["LOC_ABSCISSE"])*coeffW, parseInt(lastProduct["LOC_ORDONNEE"])*coeffH);
            lastProduct = false;
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
        text: 'Instance '+idInstance+"/All Trolleys"
    }));
}

/**
 * Fonction d'initalisation du graph associé à l'ID d'une instance.
 * @param {*} idInstance 
 */
function getGraphe(idInstance) {
    $("#webContent").html("");
    $("#graphReturnB").show();
    $("#toggleLocations").show();
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