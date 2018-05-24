package metier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    // Attributs
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer nbColisMax;

    @OneToMany()
    private List<Box> boxes;
    
    @JoinColumn(name = "NINSTANCE", referencedColumnName = "ID")
	@ManyToOne
	private Instance ninstance;
    
    // Constructeurs

    public Trolley() {
        this.boxes = new ArrayList<>();
    }
    
    public Trolley(Integer id, Integer nbColisMax, List<Box> boxes,Instance ninstance) {        
        this.id = id;
        this.nbColisMax = nbColisMax;
        this.boxes = new ArrayList<>(boxes);
        this.ninstance = ninstance;
    }
    
    public Trolley(Integer nbColisMax, List<Box> boxes,Instance ninstance) {
        this.id = id;
        this.nbColisMax = nbColisMax;
        this.boxes = new ArrayList<>(boxes);
        this.ninstance = ninstance;
    }
    
    public Trolley(Integer id, Integer nbColisMax,Instance ninstance) {
        this();
        this.id = id;
        this.nbColisMax = nbColisMax;
        this.ninstance = ninstance;
    }
    
    // Accesseurs

    public int getId() {
        return id;
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
        if(this.nbColisMax < boxes.size() + 1) return false;
        boxes.add(p);
        return true;
    }

    @Override
    public String toString() {
        return "Trolley{" + "id=" + id + ", boxes=" + boxes + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}