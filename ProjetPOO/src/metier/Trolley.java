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
    private static Integer nbColisMax;

    @OneToMany(mappedBy = "id")
    private List<Box> boxes;
    
    // Constructeurs

    public Trolley() {
        this.nbColisMax = 0;
        this.boxes = new ArrayList<>();
    }

    public Trolley(int nbColisMax) {
        this();
        this.nbColisMax = nbColisMax;
    }
    
    public Trolley(Integer id, Integer nbColisMax, List<Box> boxes) {
        this();
        this.id = id;
        this.nbColisMax = nbColisMax;
        this.boxes = boxes;
    }
    
    public Trolley(Integer id, Integer nbColisMax) {
        this();
        this.id = id;
        this.nbColisMax = nbColisMax;
    }
    
    // Accesseurs

    public int getId() {
        return id;
    }

    public static int getNbColisMax() {
        return nbColisMax;
    }

    public static void setNbColisMax(int nbColisMax) {
        Trolley.nbColisMax = nbColisMax;
    }

    public List<Box> getBoxes() {
        return boxes;
    }
    
    public boolean addBox(Box p) {   
        if(this.nbColisMax < boxes.size() + 1) return false;
        boxes.add(p);
        return true;
    }
    
    
    // Méthodes

    @Override
    public String toString() {
        return "\nTrolley{" + "id=" + id + ", boxes=" + boxes + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
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