package util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import metier.Arc;
import metier.Order;
import metier.Parcel;
import metier.Product;
import metier.Trolley;

/**
 * Classe permettant la recherche d'un itinéraire simple dans la zone de picking.
 */
public class Recherche {
    
    //Attributs
    private List<Order> orderList;
    private List<Product> productList;
    private List<Arc> arcsList;
    private List<Arc> distancesList;
    private int nbColisMax;
    
    //Critères poid/volume
    private int weightMax_parcel;
    private int volumeMax_parcel;
    
    // Données à déterminer
    private List<Trolley> trolleyList;
    private int cout;
    
    //Constructeurs
    
    public Recherche() {
        orderList = new ArrayList();
        productList = new ArrayList();
        this.cout = 0;
        this.nbColisMax = 0;
        this.weightMax_parcel = 0;
        this.volumeMax_parcel = 0;
    }

    public Recherche(List<Order> orderList, List<Product> productList, int nbColisMax, int weightMax_parcel, int volumeMax_parcel) {
        this();
        this.orderList = new ArrayList<>(orderList);
        this.productList = productList;
        this.nbColisMax = nbColisMax;
        this.weightMax_parcel = weightMax_parcel;
        this.volumeMax_parcel = volumeMax_parcel;
    }
    
    // Méthodes
    
    
    public ArrayList<Trolley> lookup(){
        int nbParcel = 0;
        int qt;
        Product p;
        int idTrolley = 1;
        int idParcel = 1;
        ArrayList<Trolley> solution = new ArrayList();
        
        // Création d'un chariot pour la première tournée
        Trolley trolley = new Trolley(idTrolley, nbColisMax);
        
        for(Order order : orderList ){
        
            // Création du premier colis vide pour la commande
            Parcel parcel = new Parcel(idParcel, weightMax_parcel, volumeMax_parcel, order, 0, 0);
            
            // Variable d'itération des produits de la commande
            Iterator it = order.getProducts().entrySet().iterator();
            
            //Tant qu'il y a des produits à placer dans la recherche de solution
            while (it.hasNext()) {
                //On récupère la paire produit/qt du HashMap
                Map.Entry pair = (Map.Entry) it.next();
                p = (Product) pair.getKey();
                qt = (int) pair.getValue();
                nbParcel = trolley.getParcels().size();
                
                // Vérifier qu'il y a de la place dans le chariot
                if (nbParcel < trolley.getNbColisMax()) {
                    // Vérifier que le colis n'est pas plein ou surchargé
                    if (parcel.getVolume() + (p.getVolume() * qt) < this.volumeMax_parcel && parcel.getWeight() + (p.getWeight() * qt) < this.weightMax_parcel) {
                        parcel.addProduct(p, qt);
                    }//Sinon on met le colis plein dans le chariot et on ajoute un nouveau colis à remplir
                    else {
                        trolley.addParcel(parcel);
                        idParcel++;
                        parcel = new Parcel(idParcel, weightMax_parcel, volumeMax_parcel, order, 0, 0);
                        parcel.addProduct(p, qt);
                    }
                }
                else {//sinon on met le chariot dans la tournée et on recommence un chariot
                    solution.add(trolley);
                    idTrolley++;
                    trolley = new Trolley(idTrolley, nbColisMax);
                }
                
                // Mettre à jour le coût
            }

            trolley.addParcel(parcel);
            idParcel++;
        }
       
        solution.add(trolley);
        return solution;
    }
    
    
    
}
