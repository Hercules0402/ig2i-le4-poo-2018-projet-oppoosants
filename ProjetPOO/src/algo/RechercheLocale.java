package algo;

import java.util.ArrayList;
import metier.Instance;
import metier.Trolley;
import util.Distances;
import util.Reader;
import util.Writer;

public class RechercheLocale {
    
    private Instance instance;

	public RechercheLocale(Instance instance) {
		this.instance = instance;
    }
    
    private boolean deplacementIntraTrolley() {
        return this.instance.deplacementIntraTrolley();
    }
    
    private boolean echangeIntraTrolley() {
		return this.instance.echangeIntraTrolley();
}
    
    public void rechercheLocale(String type) {
        switch (type) {
            case "depl.":
               while(this.deplacementIntraTrolley()) {
            
               }
               break;
            case "ech.":
               while(this.echangeIntraTrolley()) {
            
               }
               break;
            default:
                break;
        }        
    }

    /**
     * Permet de récupérer l'instance.
     * @return Instance
     */
    public Instance getNewInstance() {
        return this.instance;
    }
    
    public static void main(String[] args) {
        String fileName = "instance_0116_131940_Z2.txt";

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
        
        RechercheLocale rL = new RechercheLocale(inst);
        rL.rechercheLocale("depl.");
        
        inst = rL.getNewInstance();
        
        Writer.save(fileName, inst, false);
        
        distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));
        
        RechercheLocale rL2 = new RechercheLocale(inst);
        rL2.rechercheLocale("ech.");
        
        inst = rL2.getNewInstance();
        
        Writer.save(fileName, inst, false);
        
        distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));
        
    }
}
