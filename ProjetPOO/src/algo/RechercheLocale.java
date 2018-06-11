package algo;

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
        Writer.save(fileName, inst, false);
        
        for(Trolley t : inst.getTrolleys()){
            inst.getBoxes().addAll(t.getBoxes());
        }
        
        ClarkeAndWright cwa = new ClarkeAndWright(inst);
        inst = cwa.run();
        
        //Writer.save(fileName, inst, false);
        
        distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));
        
        RechercheLocale rL = new RechercheLocale(inst);
        rL.rechercheLocale();
        
        inst = rL.getNewInstance();
        
        //Writer.save(fileName, inst, false);
        
        distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));
    }
}
