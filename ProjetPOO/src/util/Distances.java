package util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import metier.Box;
import metier.Location;
import metier.ProdQty;
import metier.Product;
import metier.Trolley;

/**
 * Permet de réaliser des fonctions statiques relatives au distances.
 */
public class Distances {
    
    /**
     * Permet de calculer le cout/distance de la solution.
     * @param trolleys Solution contenant une liste de chariots contenants une liste de colis
     * @param dep Location du dépot de départ
     * @param arr Location du dépot d'arrivée
     * @return int distance du trajet
     */
    public static int calcDistance(List<Trolley> trolleys, Location dep, Location arr){
        int d = 0;
        for (Trolley t : trolleys) {
            List<Location> locations = new ArrayList();
            for (Box b : t.getBoxes()) {
                for (ProdQty pq : b.getProdQtys()) {
                    Location act = pq.getProduct().getLoc();
                    if(!locations.contains(act))
                        locations.add(act);
                }
            }
            Collections.sort(locations);
         
            /*System.out.println(locations.get(0) + " - " + locations.get(0).getId() + " " + locations.get(0).getIdLocation());
            System.out.println("- - - -");
            for (Map.Entry<Location, Integer> a : dep.getDistances().entrySet()){
                System.out.println(a.getKey() + " - " + a.getKey().getId() + " : " + a.getKey().getIdLocation());
            }*/
            
            d += dep.getDistances().get(locations.get(0)); //Distance entre le depart et la premiere loc
            for (int i=1; i<locations.size(); i++) {
                d += locations.get(i-1).getDistances().get(locations.get(i)); //Distance entre loc n et n+1
                //System.out.println(locations.get(i-1).getDistances().get(locations.get(i)));
            }
            d += locations.get(locations.size() - 1).getDistances().get(arr); //Distance entre la derniere loc et l'arrivee
        }
        return d;
    }
    
    /**
     * Formate une distance en cm en m avec un format semblable a celui du checker.
     * @param dist Distance en cm
     * @return String distance formatée
     */
    public static String formatDistance(int dist){
        String sdist = String.valueOf(dist);
        
        String partEntiere = sdist.substring(0, (sdist.length() - 2));
        DecimalFormat df = new DecimalFormat("#,###,###");
        String partEntiereSpaced = df.format(Integer.parseInt(partEntiere)).replaceAll(",", " ");
        
        String partDecimale = sdist.substring(sdist.length() - 2);
        
        return partEntiereSpaced  + "," + partDecimale + " m.";
    }
}