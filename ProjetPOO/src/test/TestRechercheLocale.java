package test;

import algo.ClarkeAndWright;
import algo.Recherche;
import algo.RechercheLocale;
import metier.Instance;
import util.Distances;
import util.Reader;

/**
 * Classe permettant de tester la classe RechercheLocale.
 */
public class TestRechercheLocale {
    public static void main(String[] args) {
        String fileName = "instance_0606_136178_Z1.txt";

        Instance inst = Reader.read(fileName, false);

        inst = Recherche.run(inst);
        int distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));
        /*Writer.save(fileName, inst, false);

        String[] name = {""};
        name[0] = "instance_0606_136178_Z1";
        System.out.println("\n\nChecker de l'instance : " + fileName);
        checker.Checker.main(name);*/

        ClarkeAndWright cwa = new ClarkeAndWright(inst);
        inst = cwa.run();

        //Writer.save(fileName, inst, false);

        distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));

        /*name[0] = "instance_0606_136178_Z1";
        System.out.println("\n\nChecker de l'instance : " + fileName);
        checker.Checker.main(name);*/

        RechercheLocale rL = new RechercheLocale(inst);
        boolean stop = false;
        int iteration = 0;
        while (rL.echangeInterTrolley() && !stop) {
            if(iteration == 100000) stop = true;
            iteration++;
        }

        inst = rL.getNewInstance();

        //Writer.save(fileName, inst, false);

        distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));
        
        /*name[0] = "instance_0606_136178_Z1";
        System.out.println("\n\nChecker de l'instance : " + fileName);
        checker.Checker.main(name);*/
    }
}
