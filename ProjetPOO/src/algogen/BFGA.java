package algogen;

import java.util.ArrayList;
import metier.Instance;
import metier.Trolley;
import util.Distances;

public class BFGA {
    /**
     * Permet de lancer un certain nombre de fois (bruteforce) l'algorithme GATournee.
     * En fonction de la complexité de la tournée, pour ne pas mettre plus d'1min par instance.
     * @param inst Instance
     * @return Liste de tournées (solution)
     */
    public static ArrayList<Trolley> run(Instance inst){
        Long time = System.currentTimeMillis();
        ArrayList<Trolley> bestTournee = new ArrayList<>(); //On initialise une liste de tournées bestTournee
        int bestResult = Integer.MAX_VALUE; //On initialise bestResult à plus l'infini
        
        int rep = 0, val = 0, maxBest = 6;
        //En fonction de la complexité de la tournée, on définit le nombre d'essais (rep)
        //et le nombre de generations/cycle à faire (val)
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
        
        for(int i=0; i<rep; i++){ //Pour chaque essai
            ArrayList<Trolley> tournees = GATournee.runx(inst, val); //On lance l'algorithme GATournee
            int r = Distances.calcDistance(tournees, inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot()); //On calcule le cout du resultat
            System.out.println("RES : " + r);
            if(r == bestResult) //Si le résultat actuel est encore le meilleur
                maxBest--; //On décremente maxBest
            if(maxBest <= 1) break; //Quand il passe en dessous de la limite fixée, on arrête
            
            if(r<bestResult){ //Si le résultat est meilleur que le meilleur resultat actuel
                bestResult = r; //Il devient le meilleur résultat
                bestTournee = tournees;
            }
        }
        System.out.println("BFGA EXECUTION TIME: " + (System.currentTimeMillis() - time) + "ms");
        return bestTournee;
    }
}