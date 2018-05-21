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
 * Classe définissant un product.
 */
@Entity
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identifiant du produit.
     */
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    
    /**
     * Localisation du produit.
     */
    @ManyToOne
    @JoinColumn(name = "LOC", referencedColumnName = "ID")
    Location loc;
    
    /**
     * Poids unitaire du produit.
     */
    @Column
    Integer weight;
    
    /**
     * Volume unitaire du produit.
     */
    @Column
    Integer volume;

    public Product() {
    }
      
    public Product(Integer id, Location loc, Integer weight, Integer volume) {
        this.id = id;
        this.loc = loc;
        this.weight = weight;
        this.volume = volume;
    }
    
    public Product( Location loc, Integer weight, Integer volume) {
        this.loc = loc;
        this.weight = weight;
        this.volume = volume;
    }

    public Integer getId() {
        return id;
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
    public String toString() {
        return "Product{" + "id=" + id + ", loc=" + loc + ", weight=" + weight + ", volume=" + volume + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.loc);
        hash = 67 * hash + Objects.hashCode(this.weight);
        hash = 67 * hash + Objects.hashCode(this.volume);
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
        if (!Objects.equals(this.loc, other.loc)) {
            return false;
        }
        if (!Objects.equals(this.weight, other.weight)) {
            return false;
        }
        if (!Objects.equals(this.volume, other.volume)) {
            return false;
        }
        return true;
    }
}