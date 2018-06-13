package algo;

import metier.Instance;
import util.Distances;
import util.Reader;
import util.Writer;

public class RechercheLocale {

    private Instance instance;
    public static double diffCout = -Double.MAX_VALUE;

	public RechercheLocale(Instance instance) {
		this.instance = instance;
    }

    /**
     * Permet d'échanger des boxes dans différents trolleys.
     * @return boolean : indique si l'échange a été réalisé ou pas
     */
    private boolean echangeInterTrolley() {
		return this.instance.echangeInterTrolley();
    }
   
    /**
     * Permet de récupérer l'instance.
     * @return Instance
     */
    public Instance getNewInstance() {
        return this.instance;
    }

    public static void main(String[] args) {
        String fileName = "instance_0606_136178_Z1.txt";

        Instance inst = Reader.read(fileName, false);

        inst = Recherche.run(inst);
        int distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));
        /*Writer.save(fileName, inst, false);

        String[] name = {""};
        name[0] = "instance_0606_136178_Z1";
        System.out.println("\n\nChecker de l'instance : instance_0606_136178_Z1");
        checker.Checker.main(name);*/

        ClarkeAndWright cwa = new ClarkeAndWright(inst);
        inst = cwa.run();

        //Writer.save(fileName, inst, false);

        distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));

        /*name[0] = "instance_0606_136178_Z1";
        System.out.println("\n\nChecker de l'instance : instance_0606_136178_Z1");
        checker.Checker.main(name);*/

        RechercheLocale rL = new RechercheLocale(inst);
        boolean stop = false;
        int count = 0;
        while (rL.echangeInterTrolley() && !stop) {
            if(count == 100000) stop = true;
            count++;
        }

        inst = rL.getNewInstance();

        //Writer.save(fileName, inst, false);

        distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));
    }
}
