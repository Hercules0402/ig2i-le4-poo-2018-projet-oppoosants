package test;

import dao.DaoFactory;
import dao.LocationDao;
import dao.PersistenceType;
import metier.Location;

/**
 *
 * @author Sébastien CORNUEL
 */
public class TestDao {
    public static void main(String[] args) {
        DaoFactory fabrique = DaoFactory.getDaoFactory(PersistenceType.JPA);
        LocationDao locationManager = fabrique.getLocationDao();
        Location loc = locationManager.findByName("depot");
		System.out.println(loc);
    }
}
