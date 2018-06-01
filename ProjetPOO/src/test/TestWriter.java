package test;

import java.util.ArrayList;
import java.util.List;
import metier.Location;
import metier.Order;
import metier.Box;
import metier.Instance;
import metier.ProdQty;
import metier.Product;
import metier.Trolley;
import util.Writer;

/**
 * Classe permettant de tester la classe Writer.
 */
public class TestWriter {

    public static void main(String[] args) throws Exception {
        /* Instance */
        Instance inst = new Instance("instance_0116_131940_Z2");

        /* Location */
        Location l = new Location(1, 200, 200, "Default", inst);

        /* Products */
        Product pr1 = new Product(78, l, 5, 5, inst);
        Product pr2 = new Product(45, l, 5, 5, inst);
        Product pr3 = new Product(12, l, 5, 5, inst);
        Product pr4 = new Product(3, l, 5, 5, inst);
        Product pr5 = new Product(95, l, 5, 5, inst);
        Product pr6 = new Product(67, l, 5, 5, inst);
        Product pr7 = new Product(39, l, 5, 5, inst);
        Product pr8 = new Product(84, l, 5, 5, inst);
        Product pr9 = new Product(28, l, 5, 5, inst);
        Product pr10 = new Product(55, l, 5, 5, inst);

        /* ProdQtys */        
        ArrayList<ProdQty> prodQtys1 = new ArrayList<>();
        ArrayList<ProdQty> prodQtys2 = new ArrayList<>();
        ArrayList<ProdQty> prodQtys3 = new ArrayList<>();
        ArrayList<ProdQty> prodQtys4 = new ArrayList<>();
        ArrayList<ProdQty> prodQtys5 = new ArrayList<>();
        ArrayList<ProdQty> prodQtys6 = new ArrayList<>();

        prodQtys1.add(new ProdQty(pr1, 5));
        prodQtys1.add(new ProdQty(pr3, 1));
        prodQtys1.add(new ProdQty(pr5, 10));

        prodQtys2.add(new ProdQty(pr7, 2));
        prodQtys2.add(new ProdQty(pr9, 8));

        prodQtys3.add(new ProdQty(pr2, 12));
        prodQtys3.add(new ProdQty(pr4, 25));

        prodQtys4.add(new ProdQty(pr6, 5));
        prodQtys4.add(new ProdQty(pr8, 9));
        prodQtys4.add(new ProdQty(pr10, 8));

        prodQtys5.add(new ProdQty(pr1, 5));
        prodQtys5.add(new ProdQty(pr3, 1));
        prodQtys5.add(new ProdQty(pr5, 10));
        prodQtys5.add(new ProdQty(pr2, 12));
        prodQtys5.add(new ProdQty(pr4, 25));

        prodQtys6.add(new ProdQty(pr7, 2));
        prodQtys6.add(new ProdQty(pr9, 8));
        prodQtys6.add(new ProdQty(pr6, 5));
        prodQtys6.add(new ProdQty(pr8, 9));
        prodQtys6.add(new ProdQty(pr10, 8));

        /* Orders */
        Order or1 = new Order(1, 5, 15, prodQtys5, inst);
        Order or2 = new Order(2, 5, 15, prodQtys6, inst);

        /* Boxes */
        Box p1 = new Box(1, prodQtys1, 5, 5, or1, inst);
        Box p2 = new Box(2, prodQtys2, 5, 5, or2, inst);
        Box p3 = new Box(3, prodQtys3, 5, 5, or1, inst);
        Box p4 = new Box(4, prodQtys4, 5, 5, or2, inst);

        List<Box> boxes1 = new ArrayList<>();
        boxes1.add(p1);
        boxes1.add(p2);
        List<Box> boxes2 = new ArrayList<>();
        boxes2.add(p3);
        boxes2.add(p4);

        /* Trolleys */
        List<Trolley> trolleys = new ArrayList<>();
        trolleys.add(new Trolley(1, 2, boxes1, inst));
        trolleys.add(new Trolley(2, 2, boxes2, inst));
        
        inst.setTrolleys(trolleys);

        Writer.save("instance_0116_131940_Z2.txt", inst, false);
    }
}