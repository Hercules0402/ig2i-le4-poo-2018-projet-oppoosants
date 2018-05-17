package test;

import dao.DaoFactory;
import dao.LocationDao;
import dao.OrderDao;
import dao.PersistenceType;
import dao.ProdQtyDao;
import dao.ProductDao;
import metier.Location;
import metier.Order;
import metier.ProdQty;
import metier.Product;

/**
 *
 * @author Sébastien CORNUEL
 */
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
    }
}
