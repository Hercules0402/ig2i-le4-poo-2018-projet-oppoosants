package test;

import util.Reader;
import algo.Recherche;
import metier.Instance;
import util.Distances;
import util.Writer;

/**
 * Classe de test complet.
 * Classe permettant de tester la lecture, la persistance (si les booleans sont à true),
 * la résolution de la solution, puis l'écriture et enfin que la solution passe bien le checker.
 */
public class TestComplet {

    public static void main(String[] args) throws Exception {
        String fileName = "instance_0606_136175_Z1.txt";

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
        inst = Recherche.run(inst);
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