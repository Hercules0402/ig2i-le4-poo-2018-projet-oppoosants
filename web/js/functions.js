var isGraphic = false;
var allLocations;
var allProducts;
var allPoints = [];
var drawed = false;

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

function getGraphe(idInstance) {
    $("#webContent").html("");
    $("#graphReturnB").show();
    setAllLocations(idInstance);
    setAllProducts(idInstance);
    isGraphic = true;
}

function setAllLocations(idInstance) {
    $.ajax({
        type: "POST",
        async: false,
        url: 'php/request.php',
        data: { requestType: "getLocsSol", idInstance: idInstance },
        success : function(result, statut){ 
            result = JSON.parse(result);
            allLocations = result["content"];
        },
        error : function(result, statut) {
            $("#webContent").html('<br/><br/><hr/><h1 align="center">Erreur : impossible de récupérer les produits/locations</h1><hr/>');
        }
    });
}

function setAllProducts(idInstance) {
    $.ajax({
        type: "POST",
        async: false,
        url: 'php/request.php',
        data: { requestType: "getLightProductsSol", idInstance: idInstance },
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
    isGraphic = false;
    drawed = false;

    $.ajax({
        type: "POST",
        url: 'php/request.php',
        data: { requestType: "getInstancesSol" },
        success : function(result, statut){ 
            result = JSON.parse(result);
            //console.log(result);
            var content = '<br/><div class="row"><div align="center" class="col-md-12"><h1>Menu des solutions</h1>';

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
            '        <th>N° Instance</th>' +
            '    </tr>' +
            '</thead><tbody>';
            
            for(var t in result["content"]) {
                var trolley = result["content"][t];
                
                content += '<tr id="' + trolley['ID'] + '" onclick="getBoxesInTrolley('+ idInstance +',' + trolley['ID'] + ')">' +
                    '<td>Trolley n°' + trolley['ID'] + '</td>' +
                    '<td>' + trolley['IDTROLLEY'] + '</td>' +
                    '<td>' + trolley['NBCOLISMAX'] + '</td>' +
                    '<td>' + trolley['NINSTANCE'] + '</td>' +
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
        data: { requestType: "getProductsSol", idBox: idBox },
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
            '        <th>Location</th>' +
            '        <th>N° Instance</th>' +
            '        <th>Quantité</th>' +
            '    </tr>' +
            '</thead><tbody>';
            
            for(var p in result["content"]) {
                var product = result["content"][p];
                
                content += '<tr id="' + product['ID'] + '">' +
                    '<td>Produit n°' + product['ID'] + '</td>' +
                    '<td>' + product['IDPRODUCT'] + '</td>' +
                    '<td>' + product['VOLUME'] + '</td>' +
                    '<td>' + product['WEIGHT'] + '</td>' +
                    '<td>' + product['LOC'] + '</td>' +
                    '<td>' + product['NINSTANCE'] + '</td>' +
                    '<td>' + product['QUANTITY'] + '</td>' +
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
        data: { requestType: "getBoxesSol", idTrolley: idTrolley },
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
            '        <th>NInstance</th>' +
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
                    '<td>' + box['NINSTANCE'] + '</td>' +
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

function setup(){
    createCanvas(windowWidth, windowHeight - 55, WEBGL);
    $("#defaultCanvas0").hide();

    /*$("#defaultCanvas0").mousemove(function(e) {
        for(p in allPoints) {
            if(p == 0) $("#graphReturnB").html("Cherche : " + allPoints[p][0] + " " + allPoints[p][1] + " " + e.pageX-55 + " " + e.pageY-55);
            if (parseInt(allPoints[p][0] + windowWidth*0.05 - 3) < e.pageX && 
                parseInt(allPoints[p][0] + windowWidth*0.05 + 3) > e.pageX &&
                parseInt(allPoints[p][1] + windowHeight*0.1 - 3) < e.pageY - 55 &&
                parseInt(allPoints[p][1] + windowHeight*0.1 + 3) > e.pageY - 55) {
                $("#graphReturnB").html("Touché : " + allPoints[p][0] + " " + allPoints[p][1]);
            }
        }
    });*/
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
    
            placeLocations(coeffW, coeffH);
            placeProducts(coeffW, coeffH);
    
            //console.log(allPoints);
            drawed = true;
        }
    }
    else {
        $("#defaultCanvas0").hide();
    }
}

function placeLocations(coeffW, coeffH) {
    //var colorInc = 0;

    for(locId in allLocations) {
        var loc = allLocations[locId];

        if(loc["NAME"] == "depotStart" || loc["NAME"] == "depotEnd") {
            fill(color(255,0,0));
            var typeOfPoint = "depot";
        }
        else {
            fill(color(255,255,255));
            var typeOfPoint = "location";
        }
        
        //console.log(colorInc + " " + (255/allLocations.length));
        //fill(color(colorInc, colorInc, colorInc));
        //colorInc += (255/allLocations.length);
        
        ellipse(parseInt(loc["ABSCISSE"])*coeffW, parseInt(loc["ORDONNEE"])*coeffH,10,10, loc);
        allPoints.push([parseInt(loc["ABSCISSE"])*coeffW, parseInt(loc["ORDONNEE"])*coeffH, loc, typeOfPoint]);
    }
}

function placeProducts(coeffW, coeffH) {
    var colorInc = 0;

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

        fill(color(0,colorInc,0));
        colorInc += (255/allProducts.length);

        var pd = ellipse(abs*coeffW, ord*coeffH,10,10);
        allPoints.push([abs*coeffW,ord*coeffH, prod,"product"], pd);
        
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

function mousePressed() {
    
}

$(document).ready(function() {     
    $("#graphReturnB").hide();
    getInstances();
});
  

