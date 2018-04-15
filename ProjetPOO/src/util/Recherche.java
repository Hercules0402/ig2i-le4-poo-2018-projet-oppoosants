package util;

import java.util.ArrayList;
import java.util.HashMap;
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
 * @author Sébastien
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

    public Recherche(ArrayList<Order> orderList, List<Product> productList, int nbColisMax, int weightMax_parcel, int volumeMax_parcel) {
        this();
        this.orderList = orderList;
        this.productList = productList;
        this.nbColisMax = nbColisMax;
        this.weightMax_parcel = weightMax_parcel;
        this.volumeMax_parcel = volumeMax_parcel;
    }
    
    // Méthodes
    
    public void lookup(){
        
        // Création d'un chariot pour la première tournée
        Trolley trolley = new Trolley(nbColisMax);
        int nbParcel = 0;
        ArrayList<ArrayList<Trolley>> solutions = new ArrayList();
        ArrayList<Trolley> solution = new ArrayList();
        int qt;
        Product p;
        
        for(Order order : orderList ){

            // Création du premier colis vide pour la commande
            Parcel parcel = new Parcel(0, weightMax_parcel, volumeMax_parcel, order, 0, 0);
            
            // Variable d'itération des produits de la commande
            Iterator it = order.getProducts().entrySet().iterator();
            
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                p = (Product) pair.getKey();
                qt = (int) pair.getValue();
                nbParcel = trolley.getParcels().size();
                
                // Vérifier qu'il y a de la place dans le chariot
                if (nbParcel < trolley.getNbColisMax()) {
                    // Vérifier que le colis n'est pas plein ou surchargé
                    if (parcel.getVolume() + p.getVolume() < this.volumeMax_parcel && parcel.getWeight() + p.getWeight() < this.weightMax_parcel) {
                        parcel.addProduct(p, qt);
                    }
                }
                
                it.remove();
                // Mettre à jour le coût
            }
        }
    }
    
    
    
}
