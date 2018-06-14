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

    /**
     * Permet d'ajouter une box à un trolley pour la méthode de Clarke And Wright
     * @param b
     * @return boolean
     */
    public boolean addBoxClarkeAndWright(Box b){
        if (b == null) return false;
        if(this.nbColisMax < this.boxes.size() + 1) return false;
        this.boxes.add(b);
        return true;
    }

    /**
	 * Méthode exécutant l'échange inter qui permet d’améliorer le plus la
	 * solution courante.
	 * @param interTrolleyInfos
	 * @return boolean
	 */
	public boolean doEchangeInterTrolley(InterTrolleyInfos interTrolleyInfos) {
        // Récupération des boxes dans leur trolley associé
		Box b1 = this.boxes.get(interTrolleyInfos.getOldPosition());
		Box b2 = interTrolleyInfos.getNewTrolley().boxes.get(interTrolleyInfos.getNewPosition());
        
        // Suppression des boxes dans leur trolley associé
        interTrolleyInfos.getNewTrolley().boxes.remove(b2);
        this.boxes.remove(b1);
        
        // AJout des boxes dans leur nouveau trolley
		if (interTrolleyInfos.getNewTrolley().addBoxByPos(b1, interTrolleyInfos.getNewPosition())
				&& this.addBoxByPos(b2,interTrolleyInfos.getOldPosition())) {
			return true;
		} else {
            // Echec de l'ajout des boxes dans leur nouveau trolley donc on les
            // remets dans leur ancien trolley
            interTrolleyInfos.getNewTrolley().boxes.add(interTrolleyInfos.getNewPosition(),b2);
            this.boxes.add(interTrolleyInfos.getOldPosition(), b1);
			return false;
		}
	}

    /**
     * Ajout d'une box à une position donnée.
     * @param b
     * @param pos
     * @return 
     */
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