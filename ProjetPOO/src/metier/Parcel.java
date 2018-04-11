package metier;

import java.util.HashMap;

public class Parcel {
    private final Integer id;
    private HashMap<Product, Integer> products;
    private double weightMax;
    private double volumeMax;
    private Order order;

    public Parcel(Integer id, double weightMax, double volumeMax, Order order) {
        this.id= id;
        this.volumeMax = volumeMax;
        this.weightMax = weightMax;
        products = new HashMap();
        this.order = order;
    }

    public Parcel(Integer id, HashMap products, double weightMax, double volumeMax, Order order) {
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

    public void setWeightMax(double weightMax) {
        this.weightMax = weightMax;
    }

    public void setVolumeMax(double volumeMax) {
        this.volumeMax = volumeMax;
    }

    @Override
    public String toString() {
        return "Parcel{" + "id=" + id + ", products=" + products + ", weightMax=" + weightMax + ", volumeMax=" + volumeMax + '}';
    }
}