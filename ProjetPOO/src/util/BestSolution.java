package util;

import metier.Instance;

/**
 * Classe permettant de déterminer la meilleure solution entre deux solutions.
 */
public class BestSolution {
    /**
     * Permet de déterminer la meilleure solution entre deux solutions.
     * @param inst1
     * @param inst2
     * @return Instance
     */
    public static Instance getBestSolution(Instance inst1, Instance inst2) {
        int distanceInst1 = Distances.calcDistance(inst1.getTrolleys(), inst1.getGraph().getDepartingDepot(), inst1.getGraph().getArrivalDepot());
        int distanceInst2 = Distances.calcDistance(inst2.getTrolleys(), inst2.getGraph().getDepartingDepot(), inst2.getGraph().getArrivalDepot());

        return distanceInst1 < distanceInst2 ? inst1 : inst2;
    } 
}
