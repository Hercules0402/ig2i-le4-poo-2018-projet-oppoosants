package algo;

import java.util.List;
import metier.Instance;
import metier.Location;
import metier.ProdQty;
import metier.Trolley;
import util.BestSolution;
import util.Distances;
import util.Reader;

/**
 * Classe implémentant l'algorithme de Recherche locale.
 */
public class RechercheLocale {
    private Instance instance; // Correspond à la solution initiale (C&W) sur laquelle on va travailler
    public static double oldDiffCout = 0; // Correspond à l'ancien coût d'échange
    public static double newDiffCout = 0; // Correspond au nouveau coût d'échange
    // Ces coûts permettent d'empêcher les échanges causant de "mauvaise solutions"
    public Instance oldInstance; // Correspond à la solution initiale (C&W)

	public RechercheLocale(Instance instance, Instance old) {
		this.instance = instance;
        this.oldInstance = old;
    }
    
    /**
     * Permet d'échanger des boxes dans différents trolleys (inter).
     * @return boolean : indique si l'échange a été réalisé ou pas
     */
    public boolean echangeInterTrolley() {
		InterTrolleyInfos interTrolleyInfos = new InterTrolleyInfos();
        // Parcours des trolleys pour obtenir les informations nécessaires à
        // l'échange deux boxes de deux trolleys différents dont le coût de
        // l'échange
        for (Trolley t1 : this.instance.getTrolleys()) {
            for (Trolley t2 : this.instance.getTrolleys()) {
                // Aucune utilé car revient à faire un échange intra et dans notre
                // cas seul les mouvments de type inter sont utiles
                if (t1 == t2) continue;
                // Récupération des informations d'échange entre deux boxes de
                // deux trolleys différents
                InterTrolleyInfos tmp = this.echangeInterTrolley(t1,t2);
                // On récupére à chaque fois un coût négatif ce qui correspond 
                // à une distance totale minimale
                if (interTrolleyInfos.getDiffCout() > tmp.getDiffCout()) {
                    interTrolleyInfos = new InterTrolleyInfos(tmp);
                }                
            }            
        }
        RechercheLocale.newDiffCout = interTrolleyInfos.getDiffCout();
        // On réalise l'échange seulement si notre coût est négatif
		if (interTrolleyInfos.getDiffCout() < 0) {
            // Permet de déterminer si l'échange est nécessaire ou pas
            if (RechercheLocale.newDiffCout != Double.NEGATIVE_INFINITY) {
                if (RechercheLocale.oldDiffCout > RechercheLocale.newDiffCout) {
                    RechercheLocale.oldDiffCout = RechercheLocale.newDiffCout;
                    return interTrolleyInfos.doEchangeInterTrolley();
                }
                return false;
            }
            return interTrolleyInfos.doEchangeInterTrolley();
		}
		return false;
    }
    
    /**
	 * Retourne les infos sur l'échange inter à réaliser de deux boxes entre deux trolleys.
     * @param t1 : trolley t1
     * @param t2 : trolley t2
	 * @return InterTrolleyInfos : informations sur l'échange à réaliser
	 */
    public InterTrolleyInfos echangeInterTrolley(Trolley t1, Trolley t2) {
		InterTrolleyInfos interInfos = new InterTrolleyInfos();
		int nbBoxes1 = t1.getBoxes().size();
        int nbBoxes2 = t2.getBoxes().size();
        // Parcours des boxes pour calculer le coût de leur échange
		for (int b1 = 0; b1 < nbBoxes1; b1++) {
			for (int b2 = 0; b2 < nbBoxes2; b2++) {
				InterTrolleyInfos interInfosNew = evaluerEchangeInter(b1,b2,t1,t2); 
                // On retourne ici toujours l'échange dontle coût négatif
				if (interInfosNew.getDiffCout() < interInfos.getDiffCout()) {
                    interInfos = new InterTrolleyInfos(interInfosNew);                    
                }               
			}
		}
		return interInfos;
	}
    
    /**
	 * Retourne les données représentant l'évaluation de l'échange de 2 boxes de deux trolleys.
	 * @param posBox1 : position de la boxe 1 dans le trolley t1
	 * @param posBox2 : position de la boxe 2 dans le trolley t2
     * @param t1 : trolley t1
     * @param t2 : trolley t2
	 * @return InterTrolleyInfos : informations sur l'échange à réaliser
	 */
	private InterTrolleyInfos evaluerEchangeInter(int posBox1, int posBox2, Trolley t1, Trolley t2) {
		double diffCout = this.calculerDeltaCoutEchangeInter(posBox1, posBox2, t1, t2);
		return new InterTrolleyInfos(t1,t2,posBox1,posBox2,diffCout);
	}

    /**
	 * Permet de calculer le coût delta représentant l'échange inter de deux boxes.
	 * @param posBox1 : position de la boxe 1 dans le trolley t1
	 * @param posBox2 : position de la boxe 2 dans le trolley t2
     * @param t1 : trolley t1
     * @param t2 : trolley t2
	 * @return double : coût de l'échange inter à réaliser
	 */
	public double calculerDeltaCoutEchangeInter(int posBox1, int posBox2, Trolley t1, Trolley t2) {
		// Si les positions sont en-dehors de l'ensemble alors aucun calcul possible
        if (posBox1 < 0 || posBox1 > t1.getBoxes().size()) {
			return Double.MAX_VALUE;
		}

		if (posBox2 < 0 || posBox2 > t2.getBoxes().size()) {
			return Double.MAX_VALUE;
		}

        // Récupération des locations des boxes précédant et suivant les boxes
        // à échanger avec comme intialisation le dépôt
		Location prec1 = this.instance.getGraph().getDepartingDepot();
		Location prec2 = this.instance.getGraph().getDepartingDepot();
		Location next1 = this.instance.getGraph().getDepartingDepot();
		Location next2 = this.instance.getGraph().getDepartingDepot();
		
        // Récupération des locations répresentant les extrémités des boxes à
        // échanger
        List<ProdQty> prodQtys = t1.getBoxes().get(posBox1).getProdQtys();
        List<ProdQty> prodQtysBis = t2.getBoxes().get(posBox2).getProdQtys();
		
        Location l1_start = prodQtys.get(0).getProduct().getLoc();
		Location l1_end = prodQtys.get(prodQtys.size() - 1).getProduct().getLoc();
        Location l2_start = prodQtysBis.get(0).getProduct().getLoc();
		Location l2_end = prodQtysBis.get(prodQtysBis.size() - 1).getProduct().getLoc();

        // On fixe les nouvelles locations des boxes précédant et suivant les boxes
        // à échanger
		if (posBox1 > 0) {
			List<ProdQty> prodQtysBisTer = t1.getBoxes().get(posBox1 - 1).getProdQtys();
			prec1 = prodQtysBisTer.get(prodQtysBisTer.size() - 1).getProduct().getLoc();
		}
		if (posBox2 > 0) {
			List<ProdQty> prodQtysQuater = t2.getBoxes().get(posBox2 -  1).getProdQtys();
			prec2 = prodQtysQuater.get(prodQtysQuater.size() - 1).getProduct().getLoc();
		}

		if (posBox1 < t1.getBoxes().size() - 1) {
			List<ProdQty> prodQtysQuinquies = t1.getBoxes().get(posBox1 + 1).getProdQtys();
			next1 = prodQtysQuinquies.get(0).getProduct().getLoc();
		}
		if (posBox2 < t2.getBoxes().size() - 1) {
			List<ProdQty> prodQtysSixies = t2.getBoxes().get(posBox2 + 1).getProdQtys();
			next2 = prodQtysSixies.get(0).getProduct().getLoc();
		}

        // Calcul de l'ancienne distance
		double previousDistance = 0;

        if (!prec1.equals(next1) || !prec2.equals(next2)) {
			double previousDistanceBox1 = 0;
            double previousDistanceBox2 = 0;

            if (prec1.getDistanceTo(l1_start) == Double.MAX_VALUE) {
                previousDistanceBox1 = l1_start.getDistanceTo(prec1);
            }
            else {
                previousDistanceBox1 = prec1.getDistanceTo(l1_start);
            }
            if (l1_end.getDistanceTo(next1) == Double.MAX_VALUE) {
                previousDistanceBox1 += next1.getDistanceTo(l1_end);
            }
            else {
                previousDistanceBox1 += l1_end.getDistanceTo(next1);
            }

            if (prec2.getDistanceTo(l2_start) == Double.MAX_VALUE) {
                previousDistanceBox2 = l2_start.getDistanceTo(prec2);
            }
            else {
                previousDistanceBox2 = prec2.getDistanceTo(l2_start);
            }
            if (l2_end.getDistanceTo(next2) == Double.MAX_VALUE) {
                previousDistanceBox2 += next2.getDistanceTo(l2_end);
            }
            else {
                previousDistanceBox2 += l2_end.getDistanceTo(next2);
            }

            previousDistance = previousDistanceBox1 + previousDistanceBox2;
        }

        // Calcul de la nouvelle distance
        double newDistanceBox = 0;

        newDistanceBox = prec1.getDistanceTo(l2_start) + l2_end.getDistanceTo(next1)
                    + prec2.getDistanceTo(l1_start) + l1_end.getDistanceTo(next2);

        // le coût de l'échange correspond à la nouvelle distance moins l'ancienne
		return newDistanceBox - previousDistance;
	}
   
    /**
     * Permet de récupérer l'instance.
     * @return Instance : instance modifiée
     */
    public Instance getNewInstance() {
        return BestSolution.getBestSolution(oldInstance, instance);
    }
    
    public static void main(String[] args) {
        String fileName = "../instances/instance_0606_136178_Z1.txt";

        Instance inst = Reader.read(fileName, false);
        Instance copie = new Instance(inst);

        inst = Recherche.run(inst);
        int distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));

        ClarkeAndWright cwa = new ClarkeAndWright(inst);
        inst = cwa.run();
        
        distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));

        RechercheLocale rL = new RechercheLocale(inst, new ClarkeAndWright(Recherche.run(copie)).run());        
        boolean stop = false;
        int iteration = 0;
        while (rL.echangeInterTrolley() && !stop) {
            if(iteration == 1) stop = true;
            iteration++;
        }

        inst = rL.getNewInstance();

        distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));
    }
}
