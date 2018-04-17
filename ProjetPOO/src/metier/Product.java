package metier;

import java.util.Objects;

/**
 * Classe définissant un product.
 */
public class Product {
    /**
     * Identifiant du produit.
     */
    Integer id;
    
    /**
     * Localisation du produit.
     */
    Location loc;
    
    /**
     * Poids unitaire du produit.
     */
    Integer weight;
    
    /**
     * Volume unitaire du produit.
     */
    Integer volume;

    public Product(Integer id, Location loc, Integer weight, Integer volume) {
        this.id = id;
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
        return "\n\tProduct{" + "id=" + id + ", loc=" + loc + ", weight=" + weight + ", volume=" + volume + '}';
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