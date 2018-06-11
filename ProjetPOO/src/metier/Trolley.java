package metier;

import algo.InterTrolleyInfos;
import algo.IntraTrolleyInfos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * Classe définissant un chariot.
 */
@Entity
public class Trolley implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id corespondant à l'id de la ligne dans le bdd.
     */
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Correspond à l'id dans le fichier instance.
     */
    @Column
    private Integer idTrolley;

    @Column
    private Integer nbColisMax;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Box> boxes;

    @JoinColumn(name = "NINSTANCE", referencedColumnName = "ID")
	@ManyToOne
	private Instance ninstance;

    public Trolley() {
        this.idTrolley = -1;
        this.boxes = new ArrayList<>();
    }

    public Trolley(Integer id, Integer nbColisMax, List<Box> boxes,Instance ninstance) {
        this.idTrolley = id;
        this.nbColisMax = nbColisMax;
        this.boxes = new ArrayList<>(boxes);
        this.ninstance = ninstance;
    }

    public Trolley(Integer nbColisMax, List<Box> boxes,Instance ninstance) {
        this.nbColisMax = nbColisMax;
        this.boxes = new ArrayList<>(boxes);
        this.ninstance = ninstance;
    }

    public Trolley(Integer id, Integer nbColisMax,Instance ninstance) {
        this();
        this.idTrolley = id;
        this.nbColisMax = nbColisMax;
        this.ninstance = ninstance;
    }

    public int getId() {
        return id;
    }

    public Integer getIdTrolley() {
        return idTrolley;
    }

    public void setIdTrolley(Integer idTrolley) {
        this.idTrolley = idTrolley;
    }

    public int getNbColisMax() {
        return nbColisMax;
    }

    public void setNbColisMax(int nbColisMax) {
        this.nbColisMax = nbColisMax;
    }

    public List<Box> getBoxes() {
        return boxes;
    }

    public boolean addBox(Box p) {
        //System.out.println("Ajout coli " + p.getIdBox() + " dans " + this.getIdTrolley());
        if(this.nbColisMax < boxes.size() + 1) {
            //System.out.println("Pas reussi");
            return false;
        }
        boxes.add(p);
        //System.out.println("Reussi");
        return true;
    }

    public void setBoxes(List<Box> boxes) {
        this.boxes = boxes;
    }

    public boolean addBoxes(List<Box> boxes){
        if (boxes == null) return false;
        for (Box b : boxes) {
            addBox(b);
        }
        return true;
    } 

    public boolean addBoxClarkeAndWright(Box b){
        if (b == null) return false;
        if(this.nbColisMax < this.boxes.size() + 1) return false;
        this.boxes.add(b);
        return true;
    }

    /**
	 * Retourne les infos sur le déplacment intra d'un trolley.
	 * @return IntraTrolleyInfos
	 */
    public IntraTrolleyInfos deplacementIntraTrolley() {
        IntraTrolleyInfos intraInfos = new IntraTrolleyInfos();
        int nbBoxes = this.boxes.size();
		for (int i = 0; i < nbBoxes; i++) {
			int posBox = i;
			for (int pos = 1; pos < nbBoxes; pos++) {
				if (pos != posBox) {
					IntraTrolleyInfos intraInfosNew = this.evaluerDeplacement(this.boxes.get(i),pos);                    
                    if (intraInfosNew.getDiffCout() < intraInfos.getDiffCout()) {
						intraInfos = new IntraTrolleyInfos(intraInfosNew);
					}
				}
			}
		}
		return intraInfos;
    }

    /**
	 * Retourne les données représentant l'évaluation du déplacement d'une box.
	 * @param b
	 * @param newPosition
	 * @return IntraTrolleyInfos
	 */
	private IntraTrolleyInfos evaluerDeplacement(Box b, int newPosition) {
        int positionActu = this.boxes.indexOf(b);
		double diffCout = this.calculerDeltaCoutDeplacement(positionActu, newPosition);
        
		return new IntraTrolleyInfos(this,positionActu,newPosition,diffCout);
	}

    /**
	 * Retourne les infos sur l'échange intra d'une boxe.
	 * @return IntraTrolleyInfos
	 */
	public IntraTrolleyInfos echangeIntraTrolley() {
		IntraTrolleyInfos intraInfos = new IntraTrolleyInfos();
		int nbBoxes = this.boxes.size();
		for (int b1 = 0; b1 < nbBoxes; b1++) {
			for (int b2 = 0; b2 < nbBoxes; b2++) {
				if (b1 != b2) {
					IntraTrolleyInfos intraInfosNew = this.evaluerEchange(b1,b2);
					if (intraInfosNew.getDiffCout() < intraInfos.getDiffCout()) {
						intraInfos = new IntraTrolleyInfos(intraInfosNew);
					}
				}
			}
		}
		return intraInfos;
	}

    /**
	 * Retourne les données représentant l'évaluation de l'échange de 2 boxes.
	 * @param posBox1
	 * @param posBox2
	 * @return IntraTrolleyInfos
	 */
	private IntraTrolleyInfos evaluerEchange(int posBox1, int posBox2) {
		double diffCout = this.calculerDeltaCoutEchange(posBox1, posBox2);
		return new IntraTrolleyInfos(this,posBox1,posBox2,diffCout);
	}

    /**
	 * Permet de calculer le coût delta représentant l'insertion d'une box b à
	 * la position pos.
	 * @param b
	 * @param pos
	 * @return double
	 */
	public double calculerDeltaCout(Box b, int pos) {
		if (pos < 0 || pos > this.boxes.size()) {
			return Double.MAX_VALUE;
		}

		Location prec = this.ninstance.getGraph().getDepartingDepot();
		if (pos > 0) {
            List<ProdQty> prodQtys = this.boxes.get(pos - 1).getProdQtys();
			prec = prodQtys.get(prodQtys.size() - 1).getProduct().getLoc();
		}

		Location next = this.ninstance.getGraph().getDepartingDepot();
		if (pos < this.boxes.size()) {
            List<ProdQty> prodQtysBis = this.boxes.get(pos).getProdQtys();
			next = prodQtysBis.get(0).getProduct().getLoc();
		}

        List<ProdQty> prodQtysTer = b.getProdQtys();
        Location actu_start = prodQtysTer.get(prodQtysTer.size() - 1).getProduct().getLoc();
        Location actu_end = prodQtysTer.get(prodQtysTer.size() - 1).getProduct().getLoc();

		double previousDistance = 0;
		if (!prec.equals(next)) {
            previousDistance = prec.getDistanceTo(next);
		}

		return prec.getDistanceTo(actu_start) + actu_end.getDistanceTo(next) - previousDistance;
	}

    /**
	 * Permet de calculer le coût delta représentant le déplacment d'une box.
	 * @param oldPosition
	 * @param newPosition
	 * @return double
	 */
	public double calculerDeltaCoutDeplacement(int oldPosition, int newPosition) {
		if (oldPosition < 0 || oldPosition > this.boxes.size()) {
			return Double.MAX_VALUE;
		}

		if (newPosition < 0 || newPosition > this.boxes.size()) {
			return Double.MAX_VALUE;
		}

		Location prec1 = this.ninstance.getGraph().getDepartingDepot();
		Location prec2 = this.ninstance.getGraph().getDepartingDepot();
		Location next1 = this.ninstance.getGraph().getDepartingDepot();
		Location next2 = this.ninstance.getGraph().getDepartingDepot();

        List<ProdQty> prodQtys = this.boxes.get(oldPosition).getProdQtys();
        List<ProdQty> prodQtysBis = this.boxes.get(newPosition).getProdQtys();
		
        Location l1_start = prodQtys.get(0).getProduct().getLoc();
        Location l1_end = prodQtys.get(prodQtys.size() - 1).getProduct().getLoc();
		Location l2_start = prodQtysBis.get(0).getProduct().getLoc();
        Location l2_end = prodQtysBis.get(prodQtysBis.size() - 1).getProduct().getLoc();

		if (oldPosition > 0) {
            List<ProdQty> prodQtysBisTer = this.boxes.get(oldPosition - 1).getProdQtys();
			prec1 = prodQtysBisTer.get(prodQtysBisTer.size() -1).getProduct().getLoc();
		}
		if (newPosition > 0) {
            List<ProdQty> prodQtysQuater = this.boxes.get(newPosition - 1).getProdQtys();
			prec2 = prodQtysQuater.get(prodQtysQuater.size() -1).getProduct().getLoc();
		}

		if (oldPosition < this.boxes.size() - 1) {
            List<ProdQty> prodQtysQuinquies = this.boxes.get(oldPosition + 1).getProdQtys();
			next1 = prodQtysQuinquies.get(0).getProduct().getLoc();
		}
		if (newPosition < this.boxes.size() - 1) {
			List<ProdQty> prodQtysSixies = this.boxes.get(newPosition + 1).getProdQtys();
			next2 = prodQtysSixies.get(0).getProduct().getLoc();
		}

		double previousDistance = 0;

		if (oldPosition < newPosition) {
			if (!prec1.equals(next1) || !prec2.equals(next2)) {
				previousDistance = prec1.getDistanceTo(l1_start) + l1_end.getDistanceTo(next1)
						+ l2_end.getDistanceTo(next2);
			}
			return prec1.getDistanceTo(next1) + l2_end.getDistanceTo(l1_start)
					+ l1_end.getDistanceTo(next2) - previousDistance;
		} else {
			if (!prec1.equals(next1) || !prec2.equals(next2)) {
				previousDistance = prec1.getDistanceTo(l1_start) + l1_end.getDistanceTo(next1)
						+ prec2.getDistanceTo(l2_start);
			}
			return prec2.getDistanceTo(l1_start) + l1_end.getDistanceTo(l2_start)
					+ prec1.getDistanceTo(next1) - previousDistance;
		}
	}

    /**
	 * Permet de calculer le coût delta représentant l'échange de deux boxes.
	 * @param posBox1
	 * @param posBox2
	 * @return double
	 */
	public double calculerDeltaCoutEchange(int posBox1, int posBox2) {
		if (posBox1 < 0 || posBox1 > this.boxes.size()) {
			return Double.MAX_VALUE;
		}

		if (posBox2 < 0 || posBox2 > this.boxes.size()) {
			return Double.MAX_VALUE;
		}

		Location prec1 = this.ninstance.getGraph().getDepartingDepot();
		Location prec2 = this.ninstance.getGraph().getDepartingDepot();
		Location next1 = this.ninstance.getGraph().getDepartingDepot();
		Location next2 = this.ninstance.getGraph().getDepartingDepot();
		
        List<ProdQty> prodQtys = this.boxes.get(posBox1).getProdQtys();
        List<ProdQty> prodQtysBis = this.boxes.get(posBox2).getProdQtys();
		
        Location l1_start = prodQtys.get(0).getProduct().getLoc();
		Location l1_end = prodQtys.get(prodQtys.size() - 1).getProduct().getLoc();
        Location l2_start = prodQtysBis.get(0).getProduct().getLoc();
		Location l2_end = prodQtysBis.get(prodQtysBis.size() - 1).getProduct().getLoc();

		if (posBox1 > 0) {
			List<ProdQty> prodQtysBisTer = this.boxes.get(posBox1 - 1).getProdQtys();
			prec1 = prodQtysBisTer.get(prodQtysBisTer.size() -1).getProduct().getLoc();
		}
		if (posBox2 > 0) {
			List<ProdQty> prodQtysQuater = this.boxes.get(posBox2 - 1).getProdQtys();
			prec2 = prodQtysQuater.get(prodQtysQuater.size() -1).getProduct().getLoc();
		}

		if (posBox1 < this.boxes.size() - 1) {
			List<ProdQty> prodQtysQuinquies = this.boxes.get(posBox1 + 1).getProdQtys();
			next1 = prodQtysQuinquies.get(0).getProduct().getLoc();
		}
		if (posBox2 < this.boxes.size() - 1) {
			List<ProdQty> prodQtysSixies = this.boxes.get(posBox2 + 1).getProdQtys();
			next2 = prodQtysSixies.get(0).getProduct().getLoc();
		}

		double previousDistance = 0;

		if (posBox1 == (posBox2 - 1) || (posBox1 + 1) == posBox2
				|| posBox2 == (posBox1 - 1) || (posBox2 + 1) == posBox1) {

			if (!prec1.equals(next1) || !prec2.equals(next2)) {
				previousDistance = prec1.getDistanceTo(l1_start) + l1_end.getDistanceTo(l2_start)
						+ l2_end.getDistanceTo(next2);
			}

			return prec1.getDistanceTo(l2_start) + l2_end.getDistanceTo(l1_start)
					+ l1_end.getDistanceTo(next2) - previousDistance;
		} else {

			if (!prec1.equals(next1) || !prec2.equals(next2)) {
				previousDistance = prec1.getDistanceTo(l1_start) + l1_end.getDistanceTo(next1)
						+ prec2.getDistanceTo(l2_start) + l2_end.getDistanceTo(next2);
			}

			return prec1.getDistanceTo(l2_start) + l2_end.getDistanceTo(next1)
					+ prec2.getDistanceTo(l1_start) + l1_end.getDistanceTo(next2) - previousDistance;
		}
	}

    /**
	 * Retourne les infos sur le déplacment inter entre deux trolleys.
     * @param t
	 * @return InterTrolleyInfos
	 */
    public InterTrolleyInfos deplacementInterTrolley(Trolley t) {
        InterTrolleyInfos interInfos = new InterTrolleyInfos();
        int nbBoxes1 = this.boxes.size();
        int nbBoxes2 = t.boxes.size();
		for (int i = 0; i < nbBoxes1; i++) {
			int posBox = i;
			for (int pos = 1; pos < nbBoxes2; pos++) {
				InterTrolleyInfos interInfosNew = this.evaluerDeplacementInter(this.boxes.get(i),pos,t);
                if (interInfosNew.getDiffCout() < interInfos.getDiffCout()) {
                    interInfos = new InterTrolleyInfos(interInfosNew);
				}
			}
		}
		return interInfos;
    }

    /**
	 * Retourne les données représentant l'évaluation du déplacement d'une box entre deux trolleys.
	 * @param b
	 * @param newPosition
     * @param t
	 * @return InterTrolleyInfos
	 */
	private InterTrolleyInfos evaluerDeplacementInter(Box b, int newPosition,Trolley t) {
        int positionActu = this.boxes.indexOf(b);
		double diffCout = this.calculerDeltaCoutDeplacementInter(positionActu, newPosition, t);
        
		return new InterTrolleyInfos(this,t,positionActu,newPosition,diffCout);
	}

    /**
	 * Permet de calculer le coût delta représentant le déplacment d'une box.
	 * @param oldPosition
	 * @param newPosition
     * @param t
	 * @return double
	 */
	public double calculerDeltaCoutDeplacementInter(int oldPosition, int newPosition, Trolley t) {
		if (oldPosition < 0 || oldPosition > this.boxes.size()) {
			return Double.MAX_VALUE;
		}

		if (newPosition < 0 || newPosition > t.boxes.size()) {
			return Double.MAX_VALUE;
		}

		Location prec1 = this.ninstance.getGraph().getDepartingDepot();
		Location prec2 = t.ninstance.getGraph().getDepartingDepot();
		Location next1 = this.ninstance.getGraph().getDepartingDepot();
		Location next2 = t.ninstance.getGraph().getDepartingDepot();

        List<ProdQty> prodQtys = this.boxes.get(oldPosition).getProdQtys();
        List<ProdQty> prodQtysBis = t.boxes.get(newPosition).getProdQtys();
		
        Location l1_start = prodQtys.get(0).getProduct().getLoc();
        Location l1_end = prodQtys.get(prodQtys.size() - 1).getProduct().getLoc();
		Location l2_start = prodQtysBis.get(0).getProduct().getLoc();
        Location l2_end = prodQtysBis.get(prodQtysBis.size() - 1).getProduct().getLoc();

		if (oldPosition > 0) {
            List<ProdQty> prodQtysBisTer = this.boxes.get(oldPosition - 1).getProdQtys();
			prec1 = prodQtysBisTer.get(prodQtysBisTer.size() -1).getProduct().getLoc();
		}
		if (newPosition > 0) {
            List<ProdQty> prodQtysQuater = t.boxes.get(newPosition - 1).getProdQtys();
			prec2 = prodQtysQuater.get(prodQtysQuater.size() -1).getProduct().getLoc();
		}

		if (oldPosition < this.boxes.size() - 1) {
            List<ProdQty> prodQtysQuinquies = this.boxes.get(oldPosition + 1).getProdQtys();
			next1 = prodQtysQuinquies.get(0).getProduct().getLoc();
		}
		if (newPosition < this.boxes.size() - 1) {
			List<ProdQty> prodQtysSixies = t.boxes.get(newPosition + 1).getProdQtys();
			next2 = prodQtysSixies.get(0).getProduct().getLoc();
		}

		double previousDistance = 0;

		if (oldPosition < newPosition) {
			if (!prec1.equals(next1) || !prec2.equals(next2)) {
				previousDistance = prec1.getDistanceTo(l1_start) + l1_end.getDistanceTo(next1)
						+ l2_end.getDistanceTo(next2);
			}
			return prec1.getDistanceTo(next1) + l2_end.getDistanceTo(l1_start)
					+ l1_end.getDistanceTo(next2) - previousDistance;
		} else {
			if (!prec1.equals(next1) || !prec2.equals(next2)) {
				previousDistance = prec1.getDistanceTo(l1_start) + l1_end.getDistanceTo(next1)
						+ prec2.getDistanceTo(l2_start);
			}
			return prec2.getDistanceTo(l1_start) + l1_end.getDistanceTo(l2_start)
					+ prec1.getDistanceTo(next1) - previousDistance;
		}
	}

    /**
	 * Retourne les infos sur l'échange inter d'une box.
     * @param t
	 * @return InterTrolleyInfos
	 */
    public InterTrolleyInfos echangeInterTrolley(Trolley t) {
		InterTrolleyInfos interInfos = new InterTrolleyInfos();
		int nbBoxes1 = this.boxes.size();
        int nbBoxes2 = t.boxes.size();
		for (int l1 = 0; l1 < nbBoxes1; l1++) {
			for (int l2 = 0; l2 < nbBoxes2; l2++) {
				//if (l1 != l2) {
				InterTrolleyInfos interInfosNew = this.evaluerEchangeInter(l1,l2,t);
				if (interInfosNew.getDiffCout() < interInfos.getDiffCout()) {
					interInfos = new InterTrolleyInfos(interInfosNew);
				}
				//}
			}
		}
		return interInfos;
	}

    /**
	 * Retourne les données représentant l'évaluation de l'échange de 2 boxes de deux trolleys.
	 * @param posBox1
	 * @param posBox2
     * @param t
	 * @return InterTrolleyInfos
	 */
	private InterTrolleyInfos evaluerEchangeInter(int posBox1, int posBox2, Trolley t) {
		double diffCout = this.calculerDeltaCoutEchangeInter(posBox1, posBox2, t);
		return new InterTrolleyInfos(this,t,posBox1,posBox2,diffCout);
	}

    /**
	 * Permet de calculer le coût delta représentant l'échange de deux boxes.
	 * @param posBox1
	 * @param posBox2
     * @param t
	 * @return double
	 */
	public double calculerDeltaCoutEchangeInter(int posBox1, int posBox2, Trolley t) {
		if (posBox1 < 0 || posBox1 > this.boxes.size()) {
			return Double.MAX_VALUE;
		}

		if (posBox2 < 0 || posBox2 > t.boxes.size()) {
			return Double.MAX_VALUE;
		}

		Location prec1 = this.ninstance.getGraph().getDepartingDepot();
		Location prec2 = t.ninstance.getGraph().getDepartingDepot();
		Location next1 = this.ninstance.getGraph().getDepartingDepot();
		Location next2 = t.ninstance.getGraph().getDepartingDepot();
		
        List<ProdQty> prodQtys = this.boxes.get(posBox1).getProdQtys();
        List<ProdQty> prodQtysBis = t.boxes.get(posBox2).getProdQtys();
		
        Location l1_start = prodQtys.get(0).getProduct().getLoc();
		Location l1_end = prodQtys.get(prodQtys.size() - 1).getProduct().getLoc();
        Location l2_start = prodQtysBis.get(0).getProduct().getLoc();
		Location l2_end = prodQtysBis.get(prodQtysBis.size() - 1).getProduct().getLoc();

		if (posBox1 > 0) {
			List<ProdQty> prodQtysBisTer = this.boxes.get(posBox1 - 1).getProdQtys();
			prec1 = prodQtysBisTer.get(prodQtysBisTer.size() -1).getProduct().getLoc();
		}
		if (posBox2 > 0) {
			List<ProdQty> prodQtysQuater = t.boxes.get(posBox2 - 1).getProdQtys();
			prec2 = prodQtysQuater.get(prodQtysQuater.size() -1).getProduct().getLoc();
		}

		if (posBox1 < this.boxes.size() - 1) {
			List<ProdQty> prodQtysQuinquies = this.boxes.get(posBox1 + 1).getProdQtys();
			next1 = prodQtysQuinquies.get(0).getProduct().getLoc();
		}
		if (posBox2 < t.boxes.size() - 1) {
			List<ProdQty> prodQtysSixies = this.boxes.get(posBox2 + 1).getProdQtys();
			next2 = prodQtysSixies.get(0).getProduct().getLoc();
		}

		double previousDistance = 0;

		if (posBox1 == (posBox2 - 1) || (posBox1 + 1) == posBox2
				|| posBox2 == (posBox1 - 1) || (posBox2 + 1) == posBox1) {

			if (!prec1.equals(next1) || !prec2.equals(next2)) {
				previousDistance = prec1.getDistanceTo(l1_start) + l1_end.getDistanceTo(l2_start)
						+ l2_end.getDistanceTo(next2);
			}

			return prec1.getDistanceTo(l2_start) + l2_end.getDistanceTo(l1_start)
					+ l1_end.getDistanceTo(next2) - previousDistance;
		} else {

			if (!prec1.equals(next1) || !prec2.equals(next2)) {
				previousDistance = prec1.getDistanceTo(l1_start) + l1_end.getDistanceTo(next1)
						+ prec2.getDistanceTo(l2_start) + l2_end.getDistanceTo(next2);
			}

			return prec1.getDistanceTo(l2_start) + l2_end.getDistanceTo(next1)
					+ prec2.getDistanceTo(l1_start) + l1_end.getDistanceTo(next2) - previousDistance;
		}
	}
    
    /**
	 * Méthode exécutant le déplacement qui permet d’améliorer le plus la
	 * solution courante.
	 * @param intraTrolleyInfos
	 * @return boolean
	 */
	public boolean doDeplacementIntraTrolley(IntraTrolleyInfos intraTrolleyInfos) {
        Box b = this.boxes.get(intraTrolleyInfos.getOldPosition());;
		return this.addBoxByPos(b, intraTrolleyInfos.getNewPosition());
	}

    /**
	 * Méthode exécutant l'échange qui permet d’améliorer le plus la
	 * solution courante.
	 * @param intraTrolleyInfos
	 * @return boolean
	 */
	public boolean doEchangeIntraTrolley(IntraTrolleyInfos intraTrolleyInfos) {
		Box b1 = this.boxes.get(intraTrolleyInfos.getOldPosition());
		Box b2 = this.boxes.get(intraTrolleyInfos.getNewPosition());

		if (this.addBoxByPos(b1, intraTrolleyInfos.getNewPosition())
				&& this.addBoxByPos(b2,intraTrolleyInfos.getOldPosition())) {
			return true;
		} else {
			return false;
		}
	}

    /**
	 * Méthode exécutant le déplacement entre deux trolleys qui permet d’améliorer le plus la
	 * solution courante.
	 * @param interTrolleyInfos
	 * @return boolean
	 */
	public boolean doDeplacementInterTrolley(InterTrolleyInfos interTrolleyInfos) {
        Box b = this.boxes.get(interTrolleyInfos.getOldPosition());
		return interTrolleyInfos.getNewTrolley().addBoxByPos(b, interTrolleyInfos.getNewPosition());
	}

    /**
	 * Méthode exécutant l'échange inter qui permet d’améliorer le plus la
	 * solution courante.
	 * @param interTrolleyInfos
	 * @return boolean
	 */
	public boolean doEchangeInterTrolley(InterTrolleyInfos interTrolleyInfos) {
		Box b1 = this.boxes.get(interTrolleyInfos.getOldPosition());
		Box b2 = interTrolleyInfos.getNewTrolley().boxes.get(interTrolleyInfos.getNewPosition());

		if (this.addBoxByPos(b1, interTrolleyInfos.getNewPosition())
				&& this.addBoxByPos(b2,interTrolleyInfos.getOldPosition())) {
			return true;
		} else {
			return false;
		}
	}

    private boolean addBoxByPos(Box b, int pos) {
        if (b == null) {
			return false;
		}
		if (pos < 0 || pos > this.boxes.size()) {
			return false;
		}
		if ((this.boxes.size() + 1) > this.nbColisMax) {
			return false;
		}

		this.boxes.add(pos, b);
		return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.idTrolley);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Trolley other = (Trolley) obj;
        if (!Objects.equals(this.idTrolley, other.idTrolley)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Trolley{" + "idTrolley=" + idTrolley + ", boxes=" + boxes + '}';
    }
}