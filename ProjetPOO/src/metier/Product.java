package metier;

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
}