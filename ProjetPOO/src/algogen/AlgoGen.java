package algogen;

import java.util.ArrayList;
import java.util.List;
import metier.Box;
import metier.Instance;
import metier.Order;
import metier.Trolley;

public class AlgoGen {
    public static List<Trolley> run(Instance inst){
        List<Order> orders = new ArrayList<>(inst.getOrders());

        List<Box> colis = new ArrayList();
        
        for(Order o : orders){
            List<Box> newColis = GAColis.run(o, inst); //GA qui opti l'ordre des produits du client en colis (return liste de colis bien remplis)
            for(Box b : newColis)
                colis.add(b);
        }
        
        List<Trolley> tournees = GATournee.run(colis, inst); //GA qui découpe la liste de box en la meilleure tournée
        return tournees;
    }
}