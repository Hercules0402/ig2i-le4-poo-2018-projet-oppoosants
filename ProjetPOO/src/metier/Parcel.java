/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier;

import java.util.HashMap;

/**
 *
 * @author nicol
 */
public class Parcel {
    private final int idParcel;
    private HashMap products;
    private double weightMax;
    private double volumeMax;
    private Order order;

    public Parcel(int idParcel, double weightMax, double volumeMax, Order order) {
        this.idParcel = idParcel;
        this.volumeMax = volumeMax;
        this.weightMax = weightMax;
        products = new HashMap<Product, Integer>();
        this.order = order;
    }

    public Parcel(int idParcel, HashMap products, double weightMax, double volumeMax, Order order) {
        this.idParcel = idParcel;
        this.products = products;
        this.volumeMax = volumeMax;
        this.weightMax = weightMax;
        this.order = order;
    }

    public int getIdParcel() {
        return idParcel;
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
        return "Parcel{" + "idParcel=" + idParcel + ", products=" + products + ", weightMax=" + weightMax + ", volumeMax=" + volumeMax + '}';
    }
    
    
}
