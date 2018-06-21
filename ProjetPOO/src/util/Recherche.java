package util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import metier.Arc;
import metier.Order;
import metier.Box;
import metier.Product;
import metier.Trolley;

/**
 * Classe permettant la recherche d'un itinéraire simple dans la zone de picking.
 */
public class Recherche {
    
    private List<Order> orderList;
    private List<Product> productList;
    private List<Arc> arcsList;
    private List<Arc> distancesList;
    private int nbColisMax;
    private int weightMax_box;
    private int volumeMax_box;
    private List<Trolley> trolleyList;
    private double cout;
    
    public Recherche() {
        orderList = new ArrayList();
        productList = new ArrayList();
        this.cout = 0;
        this.nbColisMax = 0;
        this.weightMax_box = 0;
        this.volumeMax_box = 0;
    }

    public Recherche(List<Order> orderList, List<Product> productList, int nbColisMax, int weightMax_box, int volumeMax_box) {
        this();
        this.orderList = new ArrayList<>(orderList);
        this.productList = productList;
        this.nbColisMax = nbColisMax;
        this.weightMax_box = weightMax_box;
        this.volumeMax_box = volumeMax_box;
    }
    
    public double getCout() {
        return cout;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }
    
    public ArrayList<Trolley> lookup(){
        int nbBox = 0;
        int qt;
        Product p, p_precedent = null;
        int idTrolley = 1;
        int idBox = 1;
        ArrayList<Trolley> solution = new ArrayList();
        
        // Création d'un chariot pour la première tournée
        Trolley trolley = new Trolley(idTrolley, nbColisMax);
        
        for(Order order : orderList ){
        
            // Création du premier colis vide pour la commande
            Box box = new Box(idBox, weightMax_box, volumeMax_box, order, 0, 0);
            
            // Variable d'itération des produits de la commande
            Iterator it = order.getProducts().entrySet().iterator();
            
            //Tant qu'il y a des produits à placer dans la recherche de solution
            while (it.hasNext()) {
                //On récupère la paire produit/qt du HashMap
                Map.Entry pair = (Map.Entry) it.next();
                p = (Product) pair.getKey();
                if (p_precedent == null)
                        p_precedent = p;
                qt = (int) pair.getValue();
                nbBox = trolley.getBoxes().size();
                
                // Vérifier qu'il y a de la place dans le chariot
                if (nbBox >= trolley.getNbColisMax()) {  
                    solution.add(trolley);
                    idTrolley++;
                    trolley = new Trolley(idTrolley, nbColisMax);
                }
                // Vérifier que le colis n'est pas plein ou surchargé
                if (box.getVolume() + (p.getVolume() * qt) < this.volumeMax_box && box.getWeight() + (p.getWeight() * qt) < this.weightMax_box) {
                    //System.out.println("PRODUCT "+p);
                    box.addProduct(p, qt);
                }//Sinon on met le colis plein dans le chariot et on ajoute un nouveau colis à remplir
                else {
                    trolley.addBox(box);
                    idBox++;
                    box = new Box(idBox, weightMax_box, volumeMax_box, order, 0, 0);
                    box.addProduct(p, qt);
                }
                
                // Mettre à jour le coût (calcul à vol d'oiseau)
                this.cout += p.getLoc().getDistanceTo(p_precedent.getLoc());
                p_precedent = p;
            }

            trolley.addBox(box);
            idBox++;
        }
       
        solution.add(trolley);
        return solution;
    }
}