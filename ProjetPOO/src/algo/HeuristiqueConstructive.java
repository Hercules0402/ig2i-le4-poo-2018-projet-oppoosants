package algo;

import java.util.ArrayList;
import java.util.List;
import metier.Box;
import metier.Instance;
import metier.Product;
import metier.Trolley;
import util.Distances;
import util.Reader;
import util.Writer;

public class HeuristiqueConstructive {
    private Instance instance;

    public HeuristiqueConstructive(Instance instance) {
        this.instance = instance;
    }
    
    public Instance clarkeAndWright() {
        this.instance.clear();
		List<Box> boxes = this.instance.getBoxes();
		List<Trolley> trolleys = this.instance.getTrolleys();
		List<Trolley> trolleysUsed = new ArrayList<>();
		int next = 0;
        
		for (Box box : boxes) {
			if (trolleys.isEmpty()) {
				System.out.println("Erreur : Plus de trolley dispo pour "
						+ "affecter la box " + box.getIdBox());
			} else {
				Trolley t = trolleys.remove(0);
                if (!t.addBoxes(box)) {
                    System.out.println("Erreur : Box " + box.getIdBox() + " n'a pas "
                            + "pu être affecté au trolley " + t.getIdTrolley());
                }				
                trolleysUsed.add(t);
			}
		}
		boolean ameliore = true;
		while (ameliore) {
			ameliore = fusionTrolleys(trolleysUsed);
		}
        return this.instance;
    }
    
    /**
	 * Algo de fusion des trolleys.
	 * @param trolleysUsed TODO
	 * @return boolean
	 */
	private boolean fusionTrolleys(List<Trolley> trolleysUsed) {
		int bestR = -1;
		int bestS = -1;
		double bestGain = 1;
		for (int r = 0; r < trolleysUsed.size(); r++) {
			for (int s = 0; s < trolleysUsed.size(); s++) {
				if (r != s) {
					double gain = trolleysUsed.get(r).coutFusion(trolleysUsed.get(s));
					if (gain < bestGain) {
						bestGain = gain;
						bestR = r;
						bestS = s;
					}
				}
			}
		}
		if (bestGain >= 0) {
			return false;
		}
		boolean fusion = trolleysUsed.get(bestR).fusion(trolleysUsed.get(bestS));
		if (fusion) {
			trolleysUsed.remove(bestS);
		}
		return true;
    }
    
    public static void main(String[] args) {
        String fileName = "instance_0116_131940_Z2.txt";

        /*Reader*/
        Instance inst = Reader.read(fileName, false);
        
        Recherche sol = new Recherche(inst.getOrders(), inst.getProducts(), inst.getNbBoxesTrolley(),inst.getWeightMaxBox(), inst.getVolumeMaxBox(),inst);
        inst = sol.lookup();
        int distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));
        Writer.save(fileName, inst, false);
        
        for(Trolley t : inst.getTrolleys()){
            inst.getBoxes().addAll(t.getBoxes());
        }
        
        HeuristiqueConstructive heuris = new HeuristiqueConstructive(inst);
        inst = heuris.clarkeAndWright();
        
        Writer.save("toto.txt", inst, false);
        
        distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));
    }
}