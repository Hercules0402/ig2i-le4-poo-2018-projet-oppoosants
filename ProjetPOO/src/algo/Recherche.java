package algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import metier.Order;
import metier.Box;
import metier.Instance;
import metier.ProdQty;
import metier.Product;
import metier.Trolley;

/**
 * Algorithme basique permettant la recherche d'un itinéraire simple dans la zone de picking.
 */
public class Recherche {
    private static Instance instance;

    public static Instance run(Instance inst){
        Recherche.instance = inst;
        
        List<Order> orderList = new ArrayList<>(instance.getOrders());
        List<Product> productList = new ArrayList<>(instance.getProducts());
        int nbColisMax = instance.getNbBoxesTrolley();
        int weightMaxBox = instance.getWeightMaxBox();
        int volumeMaxBox = instance.getVolumeMaxBox();
        
        int idTrolley = 1, idBox = 1, qt;
        Product p = null;
        ArrayList<Trolley> solution = new ArrayList();

        // Création d'un chariot pour la première tournée
        Trolley trolley = new Trolley(idTrolley, nbColisMax, instance);

        for(Order order : orderList ){
            // Création du premier colis vide pour la commande
            Box box = new Box(idBox, weightMaxBox, volumeMaxBox, order, 0, 0, instance);

            //On trie les ProdQty en fonction de la localisation du produit, pour minimiser les distances
            List<ProdQty> listPq = order.getProdQtys();
            Collections.sort(listPq);
            //Tant qu'il y a des produits à placer dans la recherche de solution
            for(ProdQty pq : listPq) {
                //On récupère le produit et la quantite
                p = pq.getProduct();
                qt = pq.getQuantity();

                // Vérifier que le colis n'est pas plein ou surchargé
                if (box.getVolume() + (p.getVolume() * qt) < volumeMaxBox &&
                        box.getWeight() + (p.getWeight() * qt) < weightMaxBox) {
                    box.addProduct(p, qt);
                } else { //Sinon on met le colis plein dans le chariot et on ajoute un nouveau colis à remplir
                    // Vérifier qu'il y a de la place dans le chariot
                    if (trolley.getBoxes().size() >= trolley.getNbColisMax()) {
                        solution.add(trolley);
                        idTrolley++;
                        trolley = new Trolley(idTrolley, nbColisMax, instance);
                    }
                    trolley.addBox(box);
                    idBox++;
                    box = new Box(idBox, weightMaxBox, volumeMaxBox, order, 0, 0, instance);
                    box.addProduct(p, qt);
                }
            }

            if (trolley.getBoxes().size() >= trolley.getNbColisMax()) {
                solution.add(trolley);
                idTrolley++;
                trolley = new Trolley(idTrolley, nbColisMax, instance);
            }

            trolley.addBox(box);
            idBox++;
        }

        solution.add(trolley);
        instance.setTrolleys(solution);
        List<Box> boxes = new ArrayList<>();
        for(Trolley t : solution) boxes.addAll(t.getBoxes());
        instance.setBoxes(boxes);    
        return instance;
    }
}