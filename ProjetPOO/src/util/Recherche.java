package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import metier.Arc;
import metier.Order;
import metier.Box;
import metier.Instance;
import metier.ProdQty;
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
    private int weightMax_box;
    private int volumeMax_box;
    
    // Données à déterminer
    private List<Trolley> trolleyList;
    private double cout;
    
    private Instance instance;
    
    //Constructeurs
    
    public Recherche() {
        orderList = new ArrayList();
        productList = new ArrayList();
        this.cout = 0;
        this.nbColisMax = 0;
        this.weightMax_box = 0;
        this.volumeMax_box = 0;
        this.instance = null;
    }

    public Recherche(List<Order> orderList, List<Product> productList, int nbColisMax, int weightMax_box, int volumeMax_box, Instance instance) {
        this();
        this.orderList = new ArrayList<>(orderList);
        this.productList = productList;
        this.nbColisMax = nbColisMax;
        this.weightMax_box = weightMax_box;
        this.volumeMax_box = volumeMax_box;
        this.instance = instance;
    }
    
    // Accesseur

    public double getCout() {
        return cout;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }
    
    public List<Order> getOrderList() {
        return orderList;
    }

    public List<Product> getProductList() {
        return productList;
    }
    
    // Méthodes

    public ArrayList<Trolley> lookup(){
        int nbBox = 0;
        int qt;
        Product p, p_precedent = null;
        int idTrolley = 1;
        int idBox = 1;
        ArrayList<Trolley> solution = new ArrayList();
        
        // Création d'un chariot pour la première tournée
        Trolley trolley = new Trolley(idTrolley, nbColisMax, this.instance);
        
        for(Order order : orderList ){
            // Création du premier colis vide pour la commande
            Box box = new Box(idBox, weightMax_box, volumeMax_box, order, 0, 0, this.instance);
            
            //On trie les ProdQty en fonction de la localisation du produit, pour minimiser les distances
            List<ProdQty> listPq = order.getProdQtys();
            Collections.sort(listPq);
            //Tant qu'il y a des produits à placer dans la recherche de solution
            for(ProdQty pq : listPq) {
                //On récupère le produit et la quantite
                p = pq.getProduct();
                if (p_precedent == null)
                        p_precedent = p;
                qt = pq.getQuantity();

                // Vérifier que le colis n'est pas plein ou surchargé
                if (box.getVolume() + (p.getVolume() * qt) < this.volumeMax_box && 
                        box.getWeight() + (p.getWeight() * qt) < this.weightMax_box) {
                    box.addProduct(p, qt);
                } else { //Sinon on met le colis plein dans le chariot et on ajoute un nouveau colis à remplir
                    // Vérifier qu'il y a de la place dans le chariot
                    if (trolley.getBoxes().size() >= trolley.getNbColisMax()) {  
                        solution.add(trolley);
                        idTrolley++;
                        trolley = new Trolley(idTrolley, nbColisMax,this.instance);
                    }
                    trolley.addBox(box);
                    idBox++;
                    box = new Box(idBox, weightMax_box, volumeMax_box, order, 0, 0,this.instance);
                    box.addProduct(p, qt);
                }
                
                // Mettre à jour le coût (calcul à vol d'oiseau)
                this.cout += p.getLoc().getDistanceTo(p_precedent.getLoc());
                p_precedent = p;
            }

            if (trolley.getBoxes().size() >= trolley.getNbColisMax()) {  
                solution.add(trolley);
                idTrolley++;
                trolley = new Trolley(idTrolley, nbColisMax,this.instance);
            }
       
            trolley.addBox(box);
            idBox++;
        }

        solution.add(trolley);
        return solution;
    }
}