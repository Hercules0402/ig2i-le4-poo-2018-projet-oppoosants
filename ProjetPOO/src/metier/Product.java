package metier;

public class Product {
    /**
     * Identifiant du produit.
     */
    Integer id;
    
    /**
     * Localisation du produit.
     */
    Integer loc;
    
    /**
     * Poids unitaire du produit.
     */
    Integer weight;
    
    /**
     * Volume unitaire du produit.
     */
    Integer volume;

    public Product(Integer id, Integer loc, Integer weight, Integer volume) {
        this.id = id;
        this.loc = loc;
        this.weight = weight;
        this.volume = volume;
    }

    public Integer getId() {
        return id;
    }

    public Integer getLoc() {
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
}