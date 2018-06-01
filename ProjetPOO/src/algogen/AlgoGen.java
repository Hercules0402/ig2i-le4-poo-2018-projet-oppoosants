package algogen;

import java.util.ArrayList;
import java.util.List;
import metier.Box;
import metier.Instance;
import metier.Order;
import metier.Product;
import metier.Trolley;

public class AlgoGen {
    public static List<Trolley> run(Instance inst){
        List<Order> orders = new ArrayList<>(inst.getOrders());

        List<Box> colis = new ArrayList();
        
        for(Order o : orders){
            List<Product> produits = GAColis.run(o, inst); //GA qui opti l'ordre des produits du client
            //Découper ces produits en colis (fct split)
            //Ajouter ces colis a la liste des colis totaux
        }
        
        List<Trolley> tournees = GATournee.run(colis, inst); //GA qui découpe la liste de box en la meilleure tournée
        return tournees;
    }
}