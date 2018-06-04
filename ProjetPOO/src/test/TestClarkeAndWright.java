package test;

import algo.ClarkeAndWright;
import algo.Recherche;
import metier.Instance;
import metier.Trolley;
import util.Distances;
import util.Reader;
import util.Writer;

public class TestClarkeAndWright {
    public static void main(String[] args) {
        String fileName = "instance_0606_136178_Z1.txt";//instance_0116_131940_Z2

        Instance inst = Reader.read(fileName, false);
        
        inst = Recherche.run(inst);
        int distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));
        Writer.save(fileName, inst, false);
        
        for(Trolley t : inst.getTrolleys()){
            inst.getBoxes().addAll(t.getBoxes());
        }
        
        ClarkeAndWright cwa = new ClarkeAndWright(inst);
        cwa.run();
        inst = cwa.getNewInstance();
        
        Writer.save(fileName, inst, false);
        
        distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));
    }
}
