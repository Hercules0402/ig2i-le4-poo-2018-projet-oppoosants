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
import metier.Arc;
import metier.Box;
import metier.Graph;
import metier.Location;
import metier.Order;
import metier.ProdQty;
import metier.Product;
import metier.Trolley;

public class TestDao {
    
    public static void main(String[] args) {
        DaoFactory fabrique = DaoFactory.getDaoFactory(PersistenceType.JPA);
        
        //Location
        LocationDao locationManager = fabrique.getLocationDao();
        Location loc = locationManager.findByName("depot");
		System.out.println(loc);
        
        //Product
        ProductDao productManager = fabrique.getProductDao();
        Product pro = productManager.find(1);
		System.out.println(pro);
        
        //ProdQty
        ProdQtyDao prodQtyManager = fabrique.getProdQtyDao();
        ProdQty prq = prodQtyManager.find(1);
		System.out.println(prq);
        
        //Order
        OrderDao orderManager = fabrique.getOrderDao();
        Order ord = orderManager.find(1);
		System.out.println(ord);
        
        //Arc
        ArcDao arcManager = fabrique.getArcDao();
        Arc arc = arcManager.find(1);
		System.out.println(arc);
        
        //Graph
        GraphDao graphManager = fabrique.getGraphDao();
        Graph gra = graphManager.find(1);
		System.out.println(gra);
        
        //Box
        BoxDao boxManager = fabrique.getBoxDao();
        Box box = boxManager.find(1);
		System.out.println(box);
                
        //Trolley
        TrolleyDao trolleyManager = fabrique.getTrolleyDao();
        Trolley tro = trolleyManager.find(1);
		System.out.println(tro);
    }
}
