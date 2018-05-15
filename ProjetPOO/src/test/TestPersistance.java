package test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import metier.Location;

public class TestPersistance {

    public static void main(String[] args) {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        final EntityManager em = emf.createEntityManager();
        
        try {
            final EntityTransaction et = em.getTransaction();
            try {
                et.begin();
                Location loc1 = new Location(1, 0, 0, "depot");
                em.persist(loc1);

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