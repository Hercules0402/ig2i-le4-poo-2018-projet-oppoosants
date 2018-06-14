package metier;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * Classe représentant une instance.
 */
@Entity
public class Instance implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * Correspond à l'id de la ligne dans la bdd.
     */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "NOM")
	private String nom;

	@OneToMany(mappedBy = "ninstance", cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
	private List<Arc> arcs;
 
    @OneToMany(mappedBy = "ninstance", cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Arc> distances;

    @OneToMany(mappedBy = "ninstance")
	private List<Box> boxes;

    @OneToMany(mappedBy = "ninstance", cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
	private List<Location> locations;

    @OneToMany(mappedBy = "ninstance", cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
	private List<Order> orders;

    @OneToMany(mappedBy = "ninstance", cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
	private List<Product> products;

    @OneToMany(mappedBy = "ninstance")
	private List<Trolley> trolleys;

	@OneToOne(mappedBy = "ninstance", cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
	private Graph graph;

    @Column
    private int nbBoxesTrolley;

    @Column
    private int weightMaxBox;

    @Column
    private int volumeMaxBox;

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

    public Instance(Instance instance) {
        this.id = instance.id;
        this.nom = instance.nom;
        this.graph = instance.graph;
		this.arcs = new ArrayList<>(instance.arcs);
        this.distances = new ArrayList<>(instance.distances);
        this.boxes = new ArrayList<>(instance.boxes);
		this.locations = new ArrayList<>(instance.locations);
		this.orders = new ArrayList<>(instance.orders);
		this.products = new ArrayList<>(instance.products);
        this.trolleys = new ArrayList<>(instance.trolleys);
        this.weightMaxBox = instance.weightMaxBox;
        this.volumeMaxBox = instance.volumeMaxBox;
        this.nbBoxesTrolley = instance.nbBoxesTrolley;
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

    public List<Arc> getDistances() {
        return distances;
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

    public int getNbBoxesTrolley() {
        return nbBoxesTrolley;
    }

    public int getWeightMaxBox() {
        return weightMaxBox;
    }

    public int getVolumeMaxBox() {
        return volumeMaxBox;
    }

    public void setArcs(List<Arc> arcs) {
        this.arcs = arcs;
    }

    public void setDistances(List<Arc> distances) {
        this.distances = distances;
    }

    public void setBoxes(List<Box> boxes) {
        this.boxes = boxes;
    }

    public void setTrolleys(List<Trolley> trolleys) {
        this.trolleys = trolleys;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setNbBoxesTrolley(int nbBoxesTrolley) {
        this.nbBoxesTrolley = nbBoxesTrolley;
    }

    public void setWeightMaxBox(int weightMaxBox) {
        this.weightMaxBox = weightMaxBox;
    }

    public void setVolumeMaxBox(int volumeMaxBox) {
        this.volumeMaxBox = volumeMaxBox;
    }

    /**
     * Permet de vider l'instance.
     */
    public void clear() {
		for (Box b : this.boxes) {
			b.clear();
		}
		this.graph.clear();
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
        if (!Objects.equals(this.graph, other.graph)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Instance{" + "id=" + id + ", nom=" + nom + '}';
    }
}