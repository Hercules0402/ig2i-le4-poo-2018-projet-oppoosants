package metier;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Classe définissant un produit.
 */
@Entity
public class Product implements Serializable {
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
    private Integer idProduct;

    @ManyToOne
    @JoinColumn(name = "LOC", referencedColumnName = "ID")
    private Location loc;

    @Column
    private Integer weight;

    @Column
    private Integer volume;

    @JoinColumn(name = "NINSTANCE", referencedColumnName = "ID")
	@ManyToOne
	private Instance ninstance;

    public Product() {
    }

    public Product(Integer id, Location loc, Integer weight, Integer volume,Instance ninstance) {
        this.idProduct = id;
        this.loc = loc;
        this.weight = weight;
        this.volume = volume;
        this.ninstance = ninstance;
    }

    public Product( Location loc, Integer weight, Integer volume,Instance ninstance) {
        this.loc = loc;
        this.weight = weight;
        this.volume = volume;
        this.ninstance = ninstance;
    }

    public Integer getId() {
        return id;
    }
    
    public Integer getIdProduct() {
        return idProduct;
    }

    public Location getLoc() {
        return loc;
    }

    public Integer getWeight() {
        return weight;
    }

    public Integer getVolume() {
        return volume;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.idProduct);
        hash = 67 * hash + Objects.hashCode(this.loc);
        hash = 67 * hash + Objects.hashCode(this.weight);
        hash = 67 * hash + Objects.hashCode(this.volume);
        hash = 67 * hash + Objects.hashCode(this.ninstance);
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
        final Product other = (Product) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.idProduct, other.idProduct)) {
            return false;
        }
        if (!Objects.equals(this.loc, other.loc)) {
            return false;
        }
        if (!Objects.equals(this.weight, other.weight)) {
            return false;
        }
        if (!Objects.equals(this.volume, other.volume)) {
            return false;
        }
        if (!Objects.equals(this.ninstance, other.ninstance)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Product{" + "idProduct=" + idProduct + ", loc=" + loc + ", weight=" + weight + ", volume=" + volume + '}';
    }
}