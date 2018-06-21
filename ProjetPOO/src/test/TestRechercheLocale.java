package test;

import algo.ClarkeAndWright;
import algo.Recherche;
import algo.RechercheLocale;
import metier.Instance;
import util.Distances;
import util.Reader;
import util.Writer;

/**
 * Classe permettant de tester la classe RechercheLocale.
 */
public class TestRechercheLocale {
    public static void main(String[] args) {
        String fileName = "instance_0606_136178_Z1.txt";

        /* Création de l'instance  et d'une copie de celle-ci */
        Instance inst = Reader.read(fileName, false);
        Instance copieInst = new Instance(inst);

        /* Lancement de l'algo. natif */
        inst = Recherche.run(inst);
        int distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));
        
        Writer.save(fileName, inst, false);

        /* Lancement de l'algo. de C & W */
        ClarkeAndWright cwa = new ClarkeAndWright(inst);
        inst = cwa.run();

        Writer.save(fileName, inst, false);

        distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));

        /* Lancement de la recherche locale */
        RechercheLocale rL = new RechercheLocale(inst, new ClarkeAndWright(Recherche.run(copieInst)).run());
        boolean stop = false;
        int iteration = 0;
        while (rL.echangeInterTrolley() && !stop) {
            if(iteration == 100000) stop = true;
            iteration++;
        }

        inst = rL.getNewInstance();

        Writer.save(fileName, inst, false);

        distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));        
        
    }
}
