package test;

import dao.ArcDao;
import dao.BoxDao;
import dao.DaoFactory;
import dao.GraphDao;
import dao.LocationDao;
import dao.OrderDao;
import dao.PersistenceType;
import dao.ProdQtyDao;
import dao.ProductDao;
import dao.TrolleyDao;
import java.util.List;
import metier.Arc;
import metier.Box;
import metier.Graph;
import metier.Location;
import metier.Order;
import metier.ProdQty;
import metier.Product;
import metier.Trolley;
import util.Reader;
import util.Recherche;
import util.Writer;

/**
 * Classe permettant de tester la classe Reader.
 */
public class TestAllJPA {
    public static void main(String[] args) throws Exception {
        /*Fabrique*/
        DaoFactory fabrique = DaoFactory.getDaoFactory(PersistenceType.JPA);
        
        String fileName = "instance_simple.txt";
        
        System.out.println("\nLecture des données...\n");
        
        /*Reader*/
        Reader r = new Reader(fileName);
        
        System.out.println(r);
        
        System.out.println("\nEnregistrement des données en base...\n");
        
        Long time = System.currentTimeMillis();
        
        LocationDao locationManager = fabrique.getLocationDao();
        for(Location l: r.getLocations()) {
            locationManager.create(l);
        }
        
        ProductDao productManager = fabrique.getProductDao();
        for(Product p: r.getProducts()) {
            productManager.create(p);
        }
        
        ArcDao arcManager = fabrique.getArcDao();
        for(Arc a: r.getArcs()) {
            arcManager.create(a);
        }
        for(Arc a: r.getDistances()) {
            arcManager.create(a);
        }
        
        ProdQtyDao prodQtyManager1 = fabrique.getProdQtyDao();
        OrderDao orderManager = fabrique.getOrderDao();
        for(Order o: r.getOrders()) {
            for(ProdQty pq: o.getProdQtys()){
                prodQtyManager1.create(pq);
            }
             orderManager.create(o);
        }
        
        GraphDao graphManager = fabrique.getGraphDao();
        Graph g = new Graph(r.getDepartingDepot(), r.getArrivalDepot(),
                r.getNbLocations(), r.getNbProducts(), 
                r.getNbVerticesIntersections(), r.getArcs());
        graphManager.create(g);        
        System.out.println("SAVE DATA EXECUTION TIME: " + (System.currentTimeMillis() - time) + "ms");
        
        System.out.println("\nGénération de la solution...\n");
        
        time = System.currentTimeMillis();
        
        /*Recherche*/
        Recherche sol = new Recherche(r.getOrders(), r.getProducts(), r.getNbBoxesTrolley(),r.getCapaBox().get(0), r.getCapaBox().get(1));
        List<Trolley> trolleys = sol.lookup();        
        
        System.out.println("SEARCH EXECUTION TIME: " + (System.currentTimeMillis() - time) + "ms");
        
        System.out.println("\nEnregistrement de la solution en base...\n");
        
        time = System.currentTimeMillis();
        
        TrolleyDao trolleyManager = fabrique.getTrolleyDao();
        BoxDao boxManager = fabrique.getBoxDao();
        ProdQtyDao prodQtyManager2 = fabrique.getProdQtyDao();
        for(Trolley t: trolleys){
            for(Box b: t.getBoxes()){
                for(ProdQty pqty: b.getProdQtys()){
                    prodQtyManager2.create(pqty);
                }
                boxManager.create(b);
            }
            trolleyManager.create(t);
        }
        
        System.out.println("SAVE SOLUTION EXECUTION TIME: " + (System.currentTimeMillis() - time) + "ms");
        
        System.out.println("\nGénération du fichier solution...\n");
        
        /*Writer*/
        Writer w = new Writer(fileName, trolleys);
    }
}