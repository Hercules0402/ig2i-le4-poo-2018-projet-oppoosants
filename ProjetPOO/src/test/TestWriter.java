package test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import metier.Location;
import metier.Order;
import metier.Parcel;
import metier.Product;
import metier.Trolley;
import util.Writer;

public class TestWriter {
    public static void main(String[] args) throws Exception {      
        Order or1 = new Order(1, 5, 15, null);
        Order or2 = new Order(2, 5, 15, null);
        
        Location l = new Location(1, 200, 200, "Default");
        Product pr1 = new Product(78, l, 5, 5);
        Product pr2 = new Product(45, l, 5, 5);
        Product pr3 = new Product(12, l, 5, 5);
        Product pr4 = new Product(3, l, 5, 5);
        Product pr5 = new Product(95, l, 5, 5);
        Product pr6 = new Product(67, l, 5, 5);
        Product pr7 = new Product(39, l, 5, 5);
        Product pr8 = new Product(84, l, 5, 5);
        Product pr9 = new Product(28, l, 5, 5);
        Product pr10 = new Product(55, l, 5, 5);
        
        HashMap<Product,Integer> products1 = new HashMap<>();
        HashMap<Product,Integer> products2 = new HashMap<>(); 
        HashMap<Product,Integer> products3 = new HashMap<>();
        HashMap<Product,Integer> products4 = new HashMap<>();
        products1.put(pr1, 5);
        products1.put(pr3, 1);
        products1.put(pr5, 10);
        
        products2.put(pr7, 2);
        products2.put(pr9, 8);
        
        products3.put(pr2, 12);
        products3.put(pr4, 25);
        
        products4.put(pr6, 5);
        products4.put(pr8, 9);
        products4.put(pr10, 8);
        
        Parcel p1 = new Parcel(1, products1, 5, 5, or1);
        Parcel p2 = new Parcel(2, products2, 5, 5, or2);
        Parcel p3 = new Parcel(3, products3, 5, 5, or1);
        Parcel p4 = new Parcel(4, products4, 5, 5, or2);
        
        List<Parcel> parcels1 = new ArrayList<>();
        parcels1.add(p1);
        parcels1.add(p2);
        List<Parcel> parcels2 = new ArrayList<>();
        parcels2.add(p3);
        parcels2.add(p4);
        
        List<Trolley> trolleys = new ArrayList<>();
        trolleys.add(new Trolley(1, 2, parcels1));
        trolleys.add(new Trolley(2, 2, parcels2));
        
        Writer w = new Writer("instance_0116_131940_Z2.txt",trolleys);
    }
}