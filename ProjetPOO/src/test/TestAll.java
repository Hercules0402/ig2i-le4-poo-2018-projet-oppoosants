package test;

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
        Recherche sol = new Recherche(inst.getOrders(), inst.getProducts(), inst.getNbBoxesTrolley(),inst.getWeightMaxBox(), inst.getVolumeMaxBox(),inst);
        inst = sol.lookup();
        int distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));
        
        /*Writer*/
        Writer.save(fileName, inst, true);

        /*Checker*/
        String[] name = {""};
        name[0] = fileName.substring(0, fileName.lastIndexOf("."));
        System.out.println("\n\nChecker de l'instance : "+ fileName);
        checker.Checker.main(name);
    }
}