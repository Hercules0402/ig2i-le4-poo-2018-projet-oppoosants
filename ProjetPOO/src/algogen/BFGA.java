package algogen;

import java.util.ArrayList;
import metier.Instance;
import metier.Trolley;
import util.Distances;

public class BFGA {
    public static ArrayList<Trolley> run(Instance inst){
        Long time = System.currentTimeMillis();
        ArrayList<Trolley> bestTournee = new ArrayList<>();
        int bestResult = Integer.MAX_VALUE;
        
        int rep = 0, val = 0, maxBest = 6;
        int nb = inst.getTrolleys().size();
        if(nb<4){
            rep = 50;
            val = 500;
        } else if(nb<10){
            rep = 50;
            val = 500;
        } else if (nb<30){
            rep = 5;
            val = 1000;
        } else {
            rep = 1;
            val = 5000;
        }
        
        for(int i=0; i<rep; i++){
            ArrayList<Trolley> tournees = GATournee.runx(inst, val);
            int r = Distances.calcDistance(tournees, inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
            System.out.println("RES : " + r);
            if(r == bestResult)
                maxBest--;
            if(maxBest <= 1) break;
            if(r<bestResult){
                bestResult = r;
                bestTournee = tournees;
            }
        }
        System.out.println("BFGA EXECUTION TIME: " + (System.currentTimeMillis() - time) + "ms");
        return bestTournee;
    }
}