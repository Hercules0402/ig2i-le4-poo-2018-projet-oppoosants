package metier;

import java.util.HashMap;
import java.util.Objects;

public class Parcel {
    private final Integer id;
    private HashMap<Product, Integer> products;
    private static int weightMax;
    private static int volumeMax;
    private int weight;
    private int volume;
    private Order order;

    public Parcel(Integer id, int weightMax, int volumeMax, Order order, int weight, int volume) {
        this.id= id;
        this.volumeMax = volumeMax;
        this.weightMax = weightMax;
        this.volume = volume;
        this.weight = weight;
        products = new HashMap();
        this.order = order;
    }

    public Parcel(Integer id, HashMap products, int weightMax, int volumeMax, Order order) {
        this.id = id;
        this.products = products;
        this.volumeMax = volumeMax;
        this.weightMax = weightMax;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public HashMap getProducts() {
        return products;
    }

    public Order getOrder() {
        return order;
    }    
    
    public void setProducts(HashMap products) {
        this.products = products;
    }

    public void setWeightMax(int weightMax) {
        this.weightMax = weightMax;
    }

    public void setVolumeMax(int volumeMax) {
        this.volumeMax = volumeMax;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.id);
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
        final Parcel other = (Parcel) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

        

    @Override
    public String toString() {
        return "Parcel{" + "id=" + id + ", products=" + products + ", weightMax=" + weightMax + ", volumeMax=" + volumeMax + '}';
    }
}