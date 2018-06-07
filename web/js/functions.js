var isGraphic = false;
var allLocations;
var allProducts;
var allPoints;
var drawed = false;
var displayLocations = false;
var pointSize = 5;
var idCurrentInstance;
var tabSolution;

function testConnexion() {
    $.ajax({
        type: "POST",
        url: 'php/request.php',
        data: { requestType: "testConnexion" },
        success : function(result, statut){ 
            result = JSON.parse(result);
            //console.log(result);
        }
    });
}

function setTabSolution() {
    $.ajax({
        type: "POST",
        async: false,
        url: 'php/request.php',
        data: { requestType: "getTabSol", idInstance: idCurrentInstance },
        success : function(result, statut){ 
            result = JSON.parse(result);
            console.log(result);
            tabSolution = result["content"];
        },
        error : function(result, statut) {
            $("#webContent").html('<br/><br/><hr/><h1 align="center">Erreur : impossible de récupérer les produits/locations</h1><hr/>');
        }
    });
}

function getGraphe(idInstance) {
    idCurrentInstance = idInstance;
    $("#webContent").html("");
    $("#graphReturnB").show();
    $("#toggleLocations").show();
    $("#mainNavbar").hide();
    allPoints = [];
    setAllLocations();
    setAllProducts();
    setTabSolution();
    isGraphic = true;
}

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

function getInstances() {
    $("#graphReturnB").hide();
    $("#toggleLocations").hide();
    $("#mainNavbar").show();
    isGraphic = false;
    drawed = false;

    $.ajax({
        type: "POST",
        url: 'php/request.php',
        data: { requestType: "getInstancesSol" },
        success : function(result, statut){ 
            result = JSON.parse(result);
            //console.log(result);
            var content = '<br/><br/><br/><div class="row"><div align="center" class="col-md-12"><h1>Menu des solutions</h1>';

            content += '<table id="table_instances" class="display">' +
            '<thead>' +
            '    <tr>' +
            '        <th>ID</th>' +
            '        <th>Nom</th>' +
            '        <th>Graphe</th>' +
            '    </tr>' +
            '</thead><tbody>';
            
            for(var i in result["content"]) {
                var instance = result["content"][i];
                
                content += '<tr id="' + instance['ID'] + '">' +
                    '<td onclick="getTrolleysInInstance(' + instance['ID'] + ')">Instance n°' + instance['ID'] + '</td>' +
                    '<td onclick="getTrolleysInInstance(' + instance['ID'] + ')">' + instance['NOM'] + '</td>' +
                    '<td><button class="btn btn-outline-dark btn-block" onclick="getGraphe('+ instance['ID'] +');">Afficher le graphe</button></td>' +
                '</tr>';
            }

            content += '</tbody></table></div></div>';
            $("#webContent").html(content);
            $('#table_instances').DataTable();
        }
    });
}

function getTrolleysInInstance(idInstance) {
    $.ajax({
        type: "POST",
        url: 'php/request.php',
        data: { requestType: "getTrolleysSol", idInstance: idInstance },
        success : function(result, statut){ 
            result = JSON.parse(result);
            //console.log(result);
            var content = '<br/><div class="row"><div align="center" class="col-md-12">' + getArbo(idInstance,null, null);

            content += '<table id="table_trolleys" class="display">' +
            '<thead>' +
            '    <tr>' +
            '        <th>Nom</th>' +
            '        <th>ID Trolley</th>' +
            '        <th>Nb colis max</th>' +
            '    </tr>' +
            '</thead><tbody>';
            
            for(var t in result["content"]) {
                var trolley = result["content"][t];
                
                content += '<tr id="' + trolley['ID'] + '" onclick="getBoxesInTrolley('+ idInstance +',' + trolley['ID'] + ')">' +
                    '<td>Trolley n°' + trolley['ID'] + '</td>' +
                    '<td>' + trolley['IDTROLLEY'] + '</td>' +
                    '<td>' + trolley['NBCOLISMAX'] + '</td>' +
                '</tr>';
            }

            content += '</tbody></table></div></div>';
            content += '</tbody></table></div></div><button class="btn btn-secondary" onclick="getInstances()">Retour à l\'accueil</button>';
            $("#webContent").html(content);
            $('#table_trolleys').DataTable();
        }
    });
}

function getProductsInBox(idInstance, idBox, idTrolley) {
    $.ajax({
        type: "POST",
        url: 'php/request.php',
        data: { requestType: "getProductsSol", idBox: idBox, idInstance: idInstance },
        success : function(result, statut){ 
            result = JSON.parse(result);
            //console.log(result);
            var content = '<br/><div class="row"><div align="center" class="col-md-12">' + getArbo(idInstance, idTrolley, idBox);

            content += '<table id="table_products" class="display">' +
            '<thead>' +
            '    <tr>' +
            '        <th>Nom</th>' +
            '        <th>ID Product</th>' +
            '        <th>Volume</th>' +
            '        <th>Poids</th>' +
            '        <th>Quantité</th>' +
            '        <th>Nom location produit</th>' +
            '        <th>X loc produit</th>' +
            '        <th>Y loc produit</th>' +            
            '    </tr>' +
            '</thead><tbody>';
            
            for(var p in result["content"]) {
                var product = result["content"][p];
                
                content += '<tr id="' + product['ID'] + '">' +
                    '<td>Produit n°' + product['ID'] + '</td>' +
                    '<td>' + product['IDPRODUCT'] + '</td>' +
                    '<td>' + product['VOLUME'] + '</td>' +
                    '<td>' + product['WEIGHT'] + '</td>' +
                    '<td>' + product['QUANTITY'] + '</td>' +
                    '<td>' + product['LOC_NAME'] + '</td>' +
                    '<td>' + product['LOC_ABSCISSE'] + '</td>' +
                    '<td>' + product['LOC_ORDONNEE'] + '</td>' +
                '</tr>';
            }

            content += '</tbody></table></div></div><button class="btn btn-secondary" onclick="getBoxesInTrolley(' + idInstance + ',' + idTrolley + ')">Retour au trolley</button>';
            $("#webContent").html(content);
            $('#table_products').DataTable();
        }
    });
}

function getBoxesInTrolley(idInstance, idTrolley) {
    $.ajax({
        type: "POST",
        url: 'php/request.php',
        data: { requestType: "getBoxesSol", idTrolley: idTrolley, idInstance: idInstance },
        success : function(result, statut){ 
            result = JSON.parse(result);
            //console.log(result);
            var content = '<br/><div class="row"><div align="center" class="col-md-12">' + getArbo(idInstance, idTrolley);

            content += '<table id="table_boxes" class="display">' +
            '<thead>' +
            '    <tr>' +
            '        <th>Nom</th>' +
            '        <th>ID Box</th>' +
            '        <th>Volume</th>' +
            '        <th>Ratio Volume/Volume max</th>' +
            '        <th>Poids</th>' +
            '        <th>Ratio Poids/Poids max</th>' +
            '        <th>ID Order</th>' +
            '    </tr>' +
            '</thead><tbody>';
            
            for(var b in result["content"]) {
                var box = result["content"][b];
                content += '<tr onclick="getProductsInBox('+ idInstance +',' + box['ID'] + ',' + idTrolley + ')" id="' + box['ID'] +'">' +
                    '<td>Colis n°' + box['ID'] + '</td>' +
                    '<td>' + box['IDBOX'] + '</td>' +
                    '<td>' + box['VOLUME'] + '</td>' +
                    '<td>' + ((box['VOLUME'] / 92160)*100).toFixed(2) + '%</td>' +
                    '<td>' + box['WEIGHT'] + '</td>' +
                    '<td>' + ((box['WEIGHT'] / 12000)*100).toFixed(2) + '%</td>' +
                    '<td>' + box['ORDER_ID'] + '</td>' +
                '</tr>';
            }

            content += '</tbody></table></div></div><button class="btn btn-secondary" onclick="getTrolleysInInstance(' + idInstance + ')">Retour à l\'instance</button>';
            
            $("#webContent").html(content);
            $('#table_boxes').DataTable();
        }
    });
    
}

function getArbo(idInstance, idTrolley, idColis) {
    var content = '<nav aria-label="breadcrumb"><ol class="breadcrumb"><li class="breadcrumb-item active"><a href="#" onclick="getInstances()">Accueil</a></li>';

    if (idInstance != null && idTrolley == null && idColis == null) {
        content += '<li class="breadcrumb-item active"><a href="#" onclick="getTrolleysInInstance('+ idInstance +')">Instance n°'+ idInstance +'</a></li>';
    }

    if (idInstance != null && idTrolley != null && idColis == null) {
        content += '<li class="breadcrumb-item"><a href="#" onclick="getTrolleysInInstance('+ idInstance +')">Instance n°'+ idInstance +'</a></li>';
        content += '<li class="breadcrumb-item active"><a href="#" onclick="getBoxesInTrolley('+ idInstance +','+ idTrolley +')">Chariot n°'+ idTrolley +'</a></li>';
    }

    if (idInstance != null && idTrolley != null && idColis != null) {
        content += '<li class="breadcrumb-item"><a href="#" onclick="getTrolleysInInstance('+ idInstance +')">Instance n°'+ idInstance +'</a></li>';
        content += '<li class="breadcrumb-item"><a href="#" onclick="getBoxesInTrolley('+ idInstance +','+ idTrolley +')">Chariot n°'+ idTrolley +'</a></li>';
        content += '<li class="breadcrumb-item active"><a href="#" onclick="getProductsInBox('+ idInstance +','+ idColis +','+ idTrolley +')">Colis n°'+ idColis +'</a></li>';
    }

    content += '</ol></nav>';

    return content;
}

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


function drawLiaisons(coeffW, coeffH) {
    var lastLocProduct = false;
    var tempLoc = false;

    tabSolution.forEach(function(trolley) {
        stroke(color(getRandomInt(256),getRandomInt(256),getRandomInt(256)));
        trolley["BOXES"].forEach(function(box) {
            box["PRODUCTS"].forEach(function(product){
                allLocations.forEach(function(loc){
                    console.log(product["LOC"] + " " + loc["IDLOCATION"]);
                    if(loc["IDLOCATION"] == product["LOC"]) {
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

function getRandomInt(max) {
    return Math.floor(Math.random() * Math.floor(max));
}

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

function getMaxDistance() {
    var maxSize = 0;
    for(locId in allLocations) {
        var loc = allLocations[locId];

        if(parseInt(loc["ABSCISSE"]) > maxSize) maxSize = parseInt(loc["ABSCISSE"]);
        if(parseInt(loc["ORDONNEE"]) > maxSize) maxSize = parseInt(loc["ORDONNEE"]);
    }

    return maxSize;
}

$(document).ready(function() {     
    $("#graphReturnB").hide();
    getInstances();
});
  

