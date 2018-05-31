function testConnexion() {
    $.ajax({
        type: "POST",
        url: 'php/request.php',
        data: { requestType: "testConnexion" },
        success : function(result, statut){ 
            result = JSON.parse(result);
            console.log(result);
        }
    });
}

function getInstances() {
    $.ajax({
        type: "POST",
        url: 'php/request.php',
        data: { requestType: "getInstancesSol" },
        success : function(result, statut){ 
            result = JSON.parse(result);
            console.log(result);
            var content = '<br/><div class="row"><div align="center" class="col-md-12"><h1>Menu des solutions</h1>';

            content += '<table id="table_instances" class="display">' +
            '<thead>' +
            '    <tr>' +
            '        <th>ID</th>' +
            '        <th>Nom</th>' +
            '    </tr>' +
            '</thead><tbody>';
            
            for(var i in result["content"]) {
                var instance = result["content"][i];
                
                content += '<tr id="' + instance['ID'] + '" onclick="getTrolleysInInstance(' + instance['ID'] + ')">' +
                    '<td>Instance n°' + instance['ID'] + '</td>' +
                    '<td>' + instance['NOM'] + '</td>' +
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
            console.log(result);
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
            console.log("Products : ");
            console.log(result);
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
            console.log(result);
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

$(document).ready(function() {     
    getInstances();
});

  

