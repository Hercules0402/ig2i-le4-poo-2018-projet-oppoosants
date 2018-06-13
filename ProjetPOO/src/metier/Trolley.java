package metier;

import algo.InterTrolleyInfos;
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
     * Correspond à l'id de la ligne dans la bdd.
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
        if(this.nbColisMax < boxes.size() + 1) {
            return false;
        }
        boxes.add(p);
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
	 * Retourne les infos sur l'échange inter d'une box.
     * @param t
	 * @return InterTrolleyInfos
	 */
    public InterTrolleyInfos echangeInterTrolley(Trolley t) {
		InterTrolleyInfos interInfos = new InterTrolleyInfos();
		int nbBoxes1 = this.boxes.size();
        int nbBoxes2 = t.boxes.size();
		for (int b1 = 0; b1 < nbBoxes1; b1++) {
			for (int b2 = 0; b2 < nbBoxes2; b2++) {
				InterTrolleyInfos interInfosNew = this.evaluerEchangeInter(b1,b2,t);                
				if (interInfosNew.getDiffCout() < interInfos.getDiffCout()) {
                    if(interInfosNew.getDiffCout() < 0 && interInfos.getDiffCout() < 0){
                        continue;
                    }
                    else {
                        interInfos = new InterTrolleyInfos(interInfosNew);
                    }                    
                }               
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
			prec1 = prodQtysBisTer.get(prodQtysBisTer.size() - 1).getProduct().getLoc();
		}
		if (posBox2 > 0) {
			List<ProdQty> prodQtysQuater = t.boxes.get(posBox2 -  1).getProdQtys();
			prec2 = prodQtysQuater.get(prodQtysQuater.size() - 1).getProduct().getLoc();
		}

		if (posBox1 < this.boxes.size() - 1) {
			List<ProdQty> prodQtysQuinquies = this.boxes.get(posBox1 + 1).getProdQtys();
			next1 = prodQtysQuinquies.get(0).getProduct().getLoc();
		}
		if (posBox2 < t.boxes.size() - 1) {
			List<ProdQty> prodQtysSixies = t.boxes.get(posBox2 + 1).getProdQtys();
			next2 = prodQtysSixies.get(0).getProduct().getLoc();
		}

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

        double newDistanceBox1 = 0;
        double newDistanceBox2 = 0;

        if (prec1.getDistanceTo(l2_start) == Double.MAX_VALUE) {
            newDistanceBox1 = l2_start.getDistanceTo(prec1);
        }
        else {
            newDistanceBox1 = prec1.getDistanceTo(l2_start);
        }
        if (l2_end.getDistanceTo(next1) == Double.MAX_VALUE) {
            newDistanceBox1 += next1.getDistanceTo(l2_end);
        }
        else {
            newDistanceBox1 += l2_end.getDistanceTo(next1);
        }
        if (prec2.getDistanceTo(l1_start) == Double.MAX_VALUE) {
            newDistanceBox2 = l1_start.getDistanceTo(prec2);
        }
        else {
            newDistanceBox2 = prec2.getDistanceTo(l1_start);
        }
        if (l1_end.getDistanceTo(next2) == Double.MAX_VALUE) {
            newDistanceBox2 += next2.getDistanceTo(l1_end);
        }
        else {
            newDistanceBox2 += l1_end.getDistanceTo(next2);
        }

		return (newDistanceBox1 + newDistanceBox2) - previousDistance;
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
        
        interTrolleyInfos.getNewTrolley().boxes.remove(b2);
        this.boxes.remove(b1);
        
		if (interTrolleyInfos.getNewTrolley().addBoxByPos(b1, interTrolleyInfos.getNewPosition())
				&& this.addBoxByPos(b2,interTrolleyInfos.getOldPosition())) {
			return true;
		} else {
            interTrolleyInfos.getNewTrolley().boxes.add(interTrolleyInfos.getNewPosition(),b2);
            this.boxes.add(interTrolleyInfos.getOldPosition(), b1);
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