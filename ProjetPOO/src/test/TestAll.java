package test;

import java.util.List;
import metier.Trolley;
import util.Reader;
import algo.Recherche;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import metier.Arc;
import metier.Box;
import metier.Location;
import metier.ProdQty;
import util.Writer;

/**
 * Classe permettant de créer une solution 1.
 * Lit le fichier d'instances, génère une solution, et l'écrit dans un fichier texte.
 */
public class TestAll {

    public static void main(String[] args) throws Exception {
        String fileName = "instance_40000.txt";

        /*Reader*/
        Reader r = new Reader(fileName, false); 
        /*for(Location l : r.getLocations()) {
            System.out.printf(l.getName() + " vers :");
            for (Map.Entry<Location, Integer> e : l.getDistances().entrySet()){
                System.out.printf(" " + e.getKey().getName() + " (" + e.getValue() + ")");
            }
            System.out.println("");
        }*/

        /*Recherche*/
        Recherche sol = new Recherche(r.getOrders(), r.getProducts(), r.getNbBoxesTrolley(),r.getCapaBox().get(0), r.getCapaBox().get(1),r.getInstance());
        List<Trolley> trolleys = sol.lookup();

        System.out.println(formatDistance(calcDistance(trolleys, r.getDistances(), r.getDepartingDepot(), r.getArrivalDepot())));
        
        /*Writer*/
        Writer w = new Writer(fileName, trolleys, false);

        /*Checker*/
        String[] name = {""};
        name[0] = fileName.substring(0, fileName.lastIndexOf("."));
        System.out.println("\n\nChecker de l'instance : "+ fileName);
        checker.Checker.main(name);
    }
    
    public static int calcDistance(List<Trolley> trolleys, List<Arc> distances, Location dep, Location arr){
        int d = 0;
        for (Trolley t : trolleys) {
            List<Location> locations = new ArrayList();
            for (Box b : t.getBoxes()) {
                for (ProdQty pq : b.getProdQtys()) {
                    Location act = pq.getProduct().getLoc();
                    if(!locations.contains(act)) //TODO: arraylist without duplicates
                        locations.add(act);
                }
            }
            Collections.sort(locations);
            
            d += dep.getDistances().get(locations.get(0)); //Distance entre le depart et la premiere loc
            for (int i=1; i<locations.size(); i++) {
              d += locations.get(i-1).getDistances().get(locations.get(i)); //Distance entre loc n et n+1
            }
            d += locations.get(locations.size() - 1).getDistances().get(arr);; //Distance entre la derniere loc et l'arrivee
        }
        return d;
    }
    
    public static String formatDistance(int dist){
        String sdist = String.valueOf(dist);
        
        String partEntiere = sdist.substring(0, (sdist.length() - 2));
        DecimalFormat df = new DecimalFormat("#,###,###");
        String partEntiereSpaced = df.format(Integer.parseInt(partEntiere)).replaceAll(",", " ");
        
        String partDecimale = sdist.substring(sdist.length() - 2);
        
        return partEntiereSpaced  + "," + partDecimale + " m.";
    }
}