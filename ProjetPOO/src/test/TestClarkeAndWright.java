package test;

import algo.ClarkeAndWright;
import algo.Recherche;
import metier.Instance;
import util.Distances;
import util.Reader;
import util.Writer;

/**
 * Classe permettant de tester la classe ClarkeAndWright.
 */
public class TestClarkeAndWright {
    public static void main(String[] args) {
        String fileName = "instance_0606_136178_Z1.txt";

        /* Création d'une instance */
        Instance inst = Reader.read(fileName, false);
        
        /* Lancement de l'algo. natif */
        inst = Recherche.run(inst);
        int distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));
        Writer.save(fileName, inst, false);
        
        /* Lancement de l'algo. C & W */
        ClarkeAndWright cwa = new ClarkeAndWright(inst);
        inst = cwa.run();
        
        Writer.save(fileName, inst, false);
        
        distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));
    }
}
