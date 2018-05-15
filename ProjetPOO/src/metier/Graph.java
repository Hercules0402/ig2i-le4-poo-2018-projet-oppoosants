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
 * Représente le graphe sous-jacent à la zone de pickeing.
 */
@Entity
public class Graph implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @JoinColumn(referencedColumnName = "ID")
    @ManyToOne
    private Location departingDepot;

    @JoinColumn(referencedColumnName = "ID")
    @ManyToOne
    private Location arrivalDepot;

    @Column
    private int nbLocations;

    @Column
    private int nbProducts;

    @Column
    private int nbVerticesIntersections;

    @OneToMany(mappedBy = "id")
    private List<Arc> arcs;

    /**
     * Constructeur par données.
     * @param nbVerticesIntersections TODO
     * @param arcs TODO
     */
    public Graph(int nbVerticesIntersections) {
        this.arcs = new ArrayList();
        
        this.nbLocations = 0;
        this.nbVerticesIntersections = nbVerticesIntersections;
        this.nbProducts = this.nbLocations - this.nbVerticesIntersections - 2;        
    }

    public Location getDepartingDepot() {
        return departingDepot;
    }

    public Location getArrivalDepot() {
        return arrivalDepot;
    }

    public int getNbLocations() {
        return nbLocations;
    }

    public int getNbProducts() {
        return nbProducts;
    }

    public int getNbVerticesIntersections() {
        return nbVerticesIntersections;
    }

    public List<Arc> getArcs() {
        return arcs;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.departingDepot);
        hash = 79 * hash + Objects.hashCode(this.arrivalDepot);
        hash = 79 * hash + this.nbLocations;
        hash = 79 * hash + this.nbProducts;
        hash = 79 * hash + this.nbVerticesIntersections;
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
        final Graph other = (Graph) obj;
        if (this.nbLocations != other.nbLocations) {
            return false;
        }
        if (this.nbProducts != other.nbProducts) {
            return false;
        }
        if (this.nbVerticesIntersections != other.nbVerticesIntersections) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.departingDepot, other.departingDepot)) {
            return false;
        }
        if (!Objects.equals(this.arrivalDepot, other.arrivalDepot)) {
            return false;
        }
        return true;
    }
    
    

    @Override
    public String toString() {
        return "Graph{" + "departingDepot=" + departingDepot + ", arrivalDepot="
                + arrivalDepot + ", nbLocations=" + nbLocations + ", nbProducts="
                + nbProducts + ", nbVerticesIntersections="
                + nbVerticesIntersections + '}';
    }
}