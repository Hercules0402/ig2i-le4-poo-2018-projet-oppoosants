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
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import metier.Arc;
import metier.Box;
import metier.Graph;
import metier.Instance;
import metier.Location;
import metier.Order;
import metier.ProdQty;
import metier.Product;
import metier.Trolley;

public class TestPersistance {

    public static void testAnnotations(EntityManager em) {    
        //Location
        Instance inst = new Instance("test");
        em.persist(inst);
        
        Location loc1 = new Location(1, 0, 0, "depot",inst);
        Location loc2 = new Location(2, 500, 600, "pos1",inst);
        Location loc3 = new Location(3, 600, 400, "pos2",inst);
        em.persist(loc1);
        em.persist(loc2);
        em.persist(loc3);

        //Product
        Product pro1 = new Product(1, loc1, 3, 100,inst);
        Product pro2 = new Product(2, loc2, 2, 100,inst);
        Product pro3 = new Product(3, loc2, 3, 300,inst);
        em.persist(pro1);
        em.persist(pro2);
        em.persist(pro3);

        //ProdQty
        ProdQty prq1 = new ProdQty(1, pro1, 5);
        ProdQty prq2 = new ProdQty(2, pro1, 10);
        ProdQty prq3 = new ProdQty(3, pro2, 1);
        em.persist(prq1);
        em.persist(prq2);
        em.persist(prq3);

        //Order
        ArrayList<ProdQty> listPrq1 = new ArrayList<>();
        listPrq1.add(prq1);
        listPrq1.add(prq3);
        ArrayList<ProdQty> listPrq2 = new ArrayList<>();
        listPrq2.add(prq2);
        Order ord1 = new Order(1, 5, 2, listPrq1,inst);
        Order ord2 = new Order(2, 2, 2, listPrq1,inst);
        Order ord3 = new Order(3, 5, 10, listPrq2,inst);
        em.persist(ord1);
        em.persist(ord2);
        em.persist(ord3);

        //Arc
        Arc arc1 = new Arc(1, loc1, loc2, 10, false,inst);
        Arc arc2 = new Arc(2, loc1, loc2, 5, true,inst);
        Arc arc3 = new Arc(3, loc1, loc3, 4, false,inst);
        em.persist(arc1);
        em.persist(arc2);
        em.persist(arc3);

        //Graph
        ArrayList<Arc> listArc1 = new ArrayList<>();
        listArc1.add(arc1);
        listArc1.add(arc2);
        ArrayList<Arc> listArc2 = new ArrayList<>();
        listArc2.add(arc3);
        Graph gra1 = new Graph(1, loc1, loc2, 2, 0, 0, listArc1,inst);
        Graph gra2 = new Graph(2, loc1, loc3, 1, 0, 0, listArc2,inst);
        em.persist(gra1);
        em.persist(gra2);

        //Box
        Box box1 = new Box(1, listPrq1, 100, 1000, ord1, inst);
        Box box2 = new Box(2, listPrq2, 110, 1100, ord3, inst);
        em.persist(box1);
        em.persist(box2);

        //Trolley
        ArrayList<Box> listBox1 = new ArrayList<>();
        listBox1.add(box1);
        listBox1.add(box2);
        Trolley tro1 = new Trolley(1, 5, listBox1,inst);
        em.persist(tro1);     
    }
    
    public static void testDAOs() {
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
    
    public static void main(String[] args) {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        final EntityManager em = emf.createEntityManager();
        
        try {
            final EntityTransaction et = em.getTransaction();
            try {
                et.begin();
                testAnnotations(em);
                et.commit();

                testDAOs();
            } catch (Exception ex) {
                et.rollback();
            }
        } finally {
            if(em != null && em.isOpen()){
                em.close();
            }
            if(emf != null && emf.isOpen()){
                emf.close();
            }
        }
    } 
}