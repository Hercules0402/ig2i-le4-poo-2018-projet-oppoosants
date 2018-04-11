package metier;

import java.util.Objects;

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
    Double weight;
    
    /**
     * Volume unitaire du produit.
     */
    Double volume;

    public Product(Integer id, Location loc, Double weight, Double volume) {
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

    public Double getWeight() {
        return weight;
    }

    public Double getVolume() {
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