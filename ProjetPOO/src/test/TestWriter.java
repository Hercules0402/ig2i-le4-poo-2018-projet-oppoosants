package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import metier.Location;
import metier.Order;
import metier.Box;
import metier.Product;
import metier.Trolley;
import util.Writer;

/**
 * Classe permettant de tester la classe Writer.
 */
public class TestWriter {
    
    public static void main(String[] args) throws Exception {      
        Order or1 = new Order(1, 5, 15, null, null);
        Order or2 = new Order(2, 5, 15, null, null);
        
        Location l = new Location(1, 200, 200, "Default",null);
        Product pr1 = new Product(78, l, 5, 5,null);
        Product pr2 = new Product(45, l, 5, 5,null);
        Product pr3 = new Product(12, l, 5, 5,null);
        Product pr4 = new Product(3, l, 5, 5,null);
        Product pr5 = new Product(95, l, 5, 5,null);
        Product pr6 = new Product(67, l, 5, 5,null);
        Product pr7 = new Product(39, l, 5, 5,null);
        Product pr8 = new Product(84, l, 5, 5,null);
        Product pr9 = new Product(28, l, 5, 5,null);
        Product pr10 = new Product(55, l, 5, 5,null);
        
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
        
        Box p1 = new Box(1, products1, 5, 5, or1,null);
        Box p2 = new Box(2, products2, 5, 5, or2,null);
        Box p3 = new Box(3, products3, 5, 5, or1,null);
        Box p4 = new Box(4, products4, 5, 5, or2,null);
        
        List<Box> boxes1 = new ArrayList<>();
        boxes1.add(p1);
        boxes1.add(p2);
        List<Box> boxes2 = new ArrayList<>();
        boxes2.add(p3);
        boxes2.add(p4);
        
        List<Trolley> trolleys = new ArrayList<>();
        trolleys.add(new Trolley(1, 2, boxes1,null));
        trolleys.add(new Trolley(2, 2, boxes2,null));
        
        Writer w = new Writer("instance_0116_131940_Z2.txt", trolleys, false);
    }
}