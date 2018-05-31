package test;

import java.util.List;
import metier.Trolley;
import util.Reader;
import algo.Recherche;
import metier.Instance;
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
        Instance inst = Reader.read(fileName, true); 
        /*for(Location l : r.getLocations()) {
            System.out.printf(l.getName() + " vers :");
            for (Map.Entry<Location, Integer> e : l.getDistances().entrySet()){
                System.out.printf(" " + e.getKey().getName() + " (" + e.getValue() + ")");
            }
            System.out.println("");
        }*/
        
        /*Recherche*/
        Recherche sol = new Recherche(inst.getOrders(), inst.getProducts(), inst.getNbBoxesTrolley(),inst.getWeightMax_box(), inst.getVolumeMax_box(),inst);
        List<Trolley> trolleys = sol.lookup();

        int distance = Distances.calcDistance(trolleys, inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));
        
        /*Writer*/
        Writer w = new Writer(fileName, trolleys, true);

        /*Checker*/
        String[] name = {""};
        name[0] = fileName.substring(0, fileName.lastIndexOf("."));
        System.out.println("\n\nChecker de l'instance : "+ fileName);
        checker.Checker.main(name);
    }
}