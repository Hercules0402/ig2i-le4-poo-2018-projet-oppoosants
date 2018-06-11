/**
 * Fonction permettant de tester la connexion au serveur.
 */
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

/**
 * Fonction permettant de récupérer la solution de chacunes des instances.
 */
function getInstances() {
    $("#graphReturnB").hide();
    $("#toggleLocations").hide();
    $("#mainNavbar").show();
    $("#trolleySelection").hide();
    $("#boxSelection").hide();
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

/**
 * Fonction permettant de récupérer la liste des trolleys des instances. 
 * @param {*} idInstance 
 */
function getTrolleysInInstance(idInstance) {
    setTabSolution(idInstance);

    var content = '<br/><div class="row"><div align="center" class="col-md-12">' + getArbo(idInstance,null, null);

    content += '<table id="table_trolleys" class="display">' +
    '<thead>' +
    '    <tr>' +
    '        <th>Nom</th>' +
    '        <th>ID Trolley</th>' +
    '        <th>Nb colis max</th>' +
    '    </tr>' +
    '</thead><tbody>';
    
    for(var t in tabSolution) {
        var trolley = tabSolution[t];
        
        content += '<tr id="' + trolley['ID'] + '" onclick="getBoxesInTrolley('+ idInstance +',' + t + ')">' +
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

/**
 * Fonction permettant de récupérer la produits de chacunes des boxes.
 * @param {*} idInstance 
 * @param {*} idBox 
 * @param {*} idTrolley 
 */
function getProductsInBox(idInstance, idBox, idTrolley) {
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
    
    for(var p in tabSolution[idTrolley]["BOXES"][idBox]["PRODUCTS"]) {
        var product = tabSolution[idTrolley]["BOXES"][idBox]["PRODUCTS"][p];
        
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

/**
 * Fonction permettant de récupérer les boxes de chacuns des trolleys.
 * @param {*} idInstance 
 * @param {*} idTrolley 
 */
function getBoxesInTrolley(idInstance, idTrolley) {
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

    for(var b in tabSolution[idTrolley]["BOXES"]) {
        var box = tabSolution[idTrolley]["BOXES"][b];
        content += '<tr onclick="getProductsInBox('+ idInstance +',' + b + ',' + idTrolley + ')" id="' + box['ID'] +'">' +
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

/**
 * Fonction permettant d'afficher l'arborescence de la solution d'une instance donnée.
 * @param {*} idInstance 
 * @param {*} idTrolley 
 * @param {*} idColis 
 */
function getArbo(idInstance, idTrolley, idColis) {
    var content = '<nav aria-label="breadcrumb" class="mtop"><ol class="breadcrumb"><li class="breadcrumb-item active"><a href="#" onclick="getInstances()">Accueil</a></li>';

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

/**
 * Fonction permettant de récupérer l'arborescence complète de chaque solution.
 * @param {*} idInstance 
 */
function setTabSolution(idInstance) {
    //si on charge une nouvelle solution ou qu'on a rien chargé encore alors requête, sinon non car c'est long
    if (idCurrentInstance == null || idInstance != idCurrentInstance) {
        $.ajax({
            type: "POST",
            url: 'php/request.php',
            async:false,
            data: { requestType: "getTabSol", idInstance: idInstance },
            success : function(result, statut){ 
                result = JSON.parse(result);
                console.log(result);
                tabSolution = result["content"];
                idCurrentInstance = idInstance;
            },
            error : function(result, statut) {
                $("#webContent").html('<br/><br/><hr/><h1 align="center">Erreur : impossible de récupérer les produits/locations</h1><hr/>');
            }
        });
    }
}

/**
 * Fonction appellée au chargement du fichier.
 */
$(document).ready(function() {
    getInstances();
});