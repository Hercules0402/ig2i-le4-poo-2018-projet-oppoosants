package metier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * Classe représentant une instance.
 */
@Entity
public class Instance implements Serializable{
    private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "NOM")
	private String nom;

	@OneToMany(mappedBy = "ninstance")
	private List<Arc> arcs;
    
    @OneToMany(mappedBy = "ninstance")
	private List<Box> boxes;
    
    @OneToMany(mappedBy = "ninstance")
	private List<Location> locations;
    
    @OneToMany(mappedBy = "ninstance")
	private List<Order> orders;
    
    @OneToMany(mappedBy = "ninstance")
	private List<Product> products;
    
    @OneToMany(mappedBy = "ninstance")
	private List<Trolley> trolleys;

	@OneToOne(mappedBy = "ninstance")
	private Graph graph;

    
    public Instance() {
		this.graph = null;
		this.arcs = new ArrayList<>();
        this.boxes = new ArrayList<>();
		this.locations = new ArrayList<>();
		this.orders = new ArrayList<>();
		this.products = new ArrayList<>();
        this.trolleys = new ArrayList<>();
	}

	public Instance(String nom) {
		this();
		this.nom = nom;
	}

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public List<Arc> getArcs() {
        return arcs;
    }

    public List<Box> getBoxes() {
        return boxes;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Trolley> getTrolleys() {
        return trolleys;
    }

    public Graph getGraph() {
        return graph;
    }

    @Override
    public String toString() {
        return "Instance{" + "id=" + id + ", nom=" + nom + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.nom);
        hash = 97 * hash + Objects.hashCode(this.graph);
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
        final Instance other = (Instance) obj;
        if (!Objects.equals(this.nom, other.nom)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.graph, other.graph)) {
            return false;
        }
        return true;
    }
    
}