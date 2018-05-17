package test;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import metier.Location;
import metier.Order;
import metier.ProdQty;
import metier.Product;

public class TestPersistance {

    public static void main(String[] args) {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        final EntityManager em = emf.createEntityManager();
        
        try {
            final EntityTransaction et = em.getTransaction();
            try {
                et.begin();
                
                //Location
                Location loc1 = new Location(1, 0, 0, "depot");
                Location loc2 = new Location(2, 500, 600, "pos1");
                Location loc3 = new Location(3, 600, 400, "pos2");
                em.persist(loc1);
                em.persist(loc2);
                em.persist(loc3);
                
                //Product
                Product pro1 = new Product(1, loc1, 3, 100);
                Product pro2 = new Product(2, loc2, 2, 100);
                Product pro3 = new Product(3, loc2, 3, 300);
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
                ArrayList<ProdQty> list1 = new ArrayList<>();
                list1.add(prq1);
                list1.add(prq3);
                ArrayList<ProdQty> list2 = new ArrayList<>();
                list2.add(prq2);
                Order ord1 = new Order(1, 5, 2, list1);
                Order ord2 = new Order(2, 2, 2, list1);
                Order ord3 = new Order(3, 5, 10, list2);
                em.persist(ord1);
                em.persist(ord2);
                em.persist(ord3);
                
                et.commit();
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