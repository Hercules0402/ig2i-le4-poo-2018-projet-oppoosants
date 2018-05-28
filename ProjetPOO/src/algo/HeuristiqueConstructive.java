package algo;

import java.util.ArrayList;
import java.util.List;
import metier.Box;
import metier.Instance;
import metier.Product;

public class HeuristiqueConstructive {
    private Instance instance;

    public HeuristiqueConstructive(Instance instance) {
        this.instance = instance;
    }
    
    public void clarkeAndWright() {
        this.instance.clear();
		List<Product> products = this.instance.getProducts();
		List<Box> boxes = this.instance.getBoxes();
		List<Box> boxesUsed = new ArrayList<>();
		int next = 0;

		for (Product p : products) {
			if (boxes.isEmpty()) {
				System.out.println("Erreur : Plus de boxes dispo pour "
						+ "affecter le produit " + p);
			} else {
				Box b = boxes.remove(0);
				/*this.instance.addBoxInGraph(b);
				if (!b.addProduct(p.getProduct(), p.getQuantity())) {
					System.out.println("Erreur : product " + p + " n'a pas "
							+ "pu être affecté au box " + b);
				}*/
				boxesUsed.add(b);
			}
		}
		boolean ameliore = true;
		while (ameliore) {
			ameliore = fusionTrolleys(boxesUsed);
		}

		//this.instance.updatePositions();
    }
    
    /**
	 * Algo de fusion des trolleys.
	 * @param boxesUsed TODO
	 * @return boolean
	 */
	private boolean fusionTrolleys(List<Box> boxesUsed) {
		int bestR = -1;
		int bestS = -1;
		double bestGain = 1;
		for (int r = 0; r < boxesUsed.size(); r++) {
			for (int s = 0; s < boxesUsed.size(); s++) {
				if (r != s) {
					double gain = boxesUsed.get(r).coutFusion(boxesUsed.get(s));
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
		boolean fusion = boxesUsed.get(bestR).fusion(boxesUsed.get(bestS));
		if (fusion) {
			boxesUsed.remove(bestS);
		}
		return true;
    }
}