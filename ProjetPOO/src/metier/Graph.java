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
import javax.persistence.OneToOne;

/**
 * Représente le graphe sous-jacent à la zone de pickeing.
 */
@Entity
public class Graph implements Serializable {
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
    private Integer idGraph;

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

    @OneToMany()
    private List<Arc> arcs;

    @JoinColumn(name = "NINSTANCE", referencedColumnName = "ID")
	@OneToOne
	private Instance ninstance;

    public Graph() {
        this.arcs = new ArrayList();
    }

    public Graph(Integer id, Location departingDepot, Location arrivalDepot, int nbLocations, int nbProducts, int nbVerticesIntersections, List<Arc> arcs, Instance ninstance) {
        this.idGraph = id;
        this.departingDepot = departingDepot;
        this.arrivalDepot = arrivalDepot;
        this.nbLocations = nbLocations;
        this.nbProducts = nbProducts;
        this.nbVerticesIntersections = nbVerticesIntersections;
        this.arcs = arcs;
        this.ninstance = ninstance;
    }

    public Graph(Location departingDepot, Location arrivalDepot, int nbLocations, int nbProducts, int nbVerticesIntersections, List<Arc> arcs,Instance ninstance) {
        this.departingDepot = departingDepot;
        this.arrivalDepot = arrivalDepot;
        this.nbLocations = nbLocations;
        this.nbProducts = nbProducts;
        this.nbVerticesIntersections = nbVerticesIntersections;
        this.arcs = arcs;        
        this.ninstance = ninstance;
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

    public Integer getIdGraph() {
        return idGraph;
    }

    public Instance getNinstance() {
        return ninstance;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.idGraph);
        hash = 79 * hash + Objects.hashCode(this.departingDepot);
        hash = 79 * hash + Objects.hashCode(this.arrivalDepot);
        hash = 79 * hash + this.nbLocations;
        hash = 79 * hash + this.nbProducts;
        hash = 79 * hash + this.nbVerticesIntersections;
        hash = 79 * hash + Objects.hashCode(this.ninstance);
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
        if (!Objects.equals(this.idGraph, other.idGraph)) {
            return false;
        }
        if (!Objects.equals(this.departingDepot, other.departingDepot)) {
            return false;
        }
        if (!Objects.equals(this.arrivalDepot, other.arrivalDepot)) {
            return false;
        }
        if (!Objects.equals(this.ninstance, other.ninstance)) {
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