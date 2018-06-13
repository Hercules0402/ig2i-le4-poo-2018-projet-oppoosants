package algo;

import metier.Box;
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

    private boolean deplacementInterTrolley() {
        return this.instance.deplacementInterTrolley();
    }
    
    private boolean echangeInterTrolley() {
		return this.instance.echangeInterTrolley();
    }
    
    public void rechercheLocale() {
        boolean improved = false;
        int count = 1;
        Instance inst = this.instance;
        double deltaCout = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());

        for (int i = 0; i < 4; i++) {                
            switch (i) {
                case 0:
                    while (this.deplacementIntraTrolley()) {
                    }   
                    break;
                case 1:
                    while (this.echangeIntraTrolley()) {
                    }   
                    break;
                case 2:
                    while (this.deplacementInterTrolley()) {
                    }   
                    break;
                default:
                    while (this.echangeInterTrolley()) {
                    }   
                    break;
            }
            double cout = Distances.calcDistance(this.instance.getTrolleys(), this.instance.getGraph().getDepartingDepot(), this.instance.getGraph().getArrivalDepot());
            if (cout < deltaCout) {
                deltaCout = cout;
            }
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
        String fileName = "instance_0606_136178_Z1.txt";//"instance_0116_131940_Z2.txt";

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
        
        Trolley t1 = inst.getTrolleys().get(0);
        Trolley t2 = inst.getTrolleys().get(45);
        
        Box b1 = t1.getBoxes().get(1);
        Box b2 = t2.getBoxes().get(5);
        
        t1.getBoxes().add(1, b2);
        t2.getBoxes().add(5, b1);
        
        t1.getBoxes().remove(b1);
        t2.getBoxes().remove(b2);
        
        inst.getTrolleys().set(0, t1);
        inst.getTrolleys().set(45, t2);
        
        
        //Writer.save(fileName, inst, false);
        
        distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));
        
        /*name[0] = "instance_0606_136178_Z1";
        System.out.println("\n\nChecker de l'instance : instance_0606_136178_Z1");
        checker.Checker.main(name);*/
        
        RechercheLocale rL = new RechercheLocale(inst);
        while (rL.echangeInterTrolley()) {
        }
        
        inst = rL.getNewInstance();
        
        //Writer.save(fileName, inst, false);
        
        distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));
    }
}
