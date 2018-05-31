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

function getTrolleys() {
    $.ajax({
        type: "POST",
        url: 'php/request.php',
        data: { requestType: "getTrolleysSol" },
        success : function(result, statut){ 
            result = JSON.parse(result);
            console.log(result);
            var content = '<br/><div class="row"><div align="center" class="col-md-12"><h1>Solution :</h1>';

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
                
                content += '<tr id="' + trolley['ID'] + '" onclick="getBoxesInTrolley(' + trolley['ID'] + ')">' +
                    '<td>Trolley n°' + trolley['ID'] + '</td>' +
                    '<td>' + trolley['IDTROLLEY'] + '</td>' +
                    '<td>' + trolley['NBCOLISMAX'] + '</td>' +
                    '<td>' + trolley['NINSTANCE'] + '</td>' +
                '</tr>';
            }

            content += '</tbody></table></div></div>';
            $("#webContent").html(content);
            $('#table_trolleys').DataTable();
        }
    });
}

function getProductsInBox(idBox, idTrolley) {
    $.ajax({
        type: "POST",
        url: 'php/request.php',
        data: { requestType: "getProductsSol", idBox: idBox },
        success : function(result, statut){ 
            result = JSON.parse(result);
            console.log("Products : ");
            console.log(result);
            var content = '<br/><div class="row"><div align="center" class="col-md-12"><h1>Solution :</h1>';

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

            content += '</tbody></table></div></div><button class="btn btn-secondary" onclick="getBoxesInTrolley(' + idTrolley + ')">Retour au trolley</button>';

            $("#webContent").html(content);
            $('#table_products').DataTable();
        }
    });
}

function getBoxesInTrolley(id) {
    $.ajax({
        type: "POST",
        url: 'php/request.php',
        data: { requestType: "getBoxesSol", idTrolley: id },
        success : function(result, statut){ 
            result = JSON.parse(result);
            console.log(result);
            var content = '<br/><div class="row"><div align="center" class="col-md-12"><h1>Colis dans le trolley n°'+ id +' :</h1>';

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
                content += '<tr onclick="getProductsInBox(' + box['ID'] + ',' + id + ')" id="' + box['ID'] +'">' +
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

            content += '</tbody></table></div></div><button class="btn btn-secondary" onclick="getTrolleys()">Retour au menu</button>';
            $("#webContent").html(content);
            $('#table_boxes').DataTable();
        }
    });
    
}

$(document).ready(function() {     
    getTrolleys();
});
  

