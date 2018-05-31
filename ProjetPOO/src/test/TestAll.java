package test;

import java.util.List;
import metier.Trolley;
import util.Reader;
import algo.Recherche;
import util.Distances;
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

        int distance = Distances.calcDistance(trolleys, r.getDepartingDepot(), r.getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));
        
        /*Writer*/
        Writer w = new Writer(fileName, trolleys, false);

        /*Checker*/
        String[] name = {""};
        name[0] = fileName.substring(0, fileName.lastIndexOf("."));
        System.out.println("\n\nChecker de l'instance : "+ fileName);
        checker.Checker.main(name);
    }
}