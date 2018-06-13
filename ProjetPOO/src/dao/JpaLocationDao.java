package dao;

import java.util.Collection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import metier.Location;

/**
 * Représente un DAO de type Location utilisant comme source de données une bdd.
 */
public class JpaLocationDao extends JpaDao<Location> implements LocationDao {
    private static JpaLocationDao instance;

	/**
	 * Constructeur par défault.
	 */
	private JpaLocationDao() {
		super(Location.class);
	}

	/**
	 * Retourne une instance de JpaLocationDao.
	 * @return JpaLocationDao
	 */
	public static JpaLocationDao getInstance() {
		if (instance == null) {
			instance = new JpaLocationDao();
		}
		return instance;
	}

    @Override
    public boolean deleteAll() {
        return super.deleteAll();
    }

    @Override
    public Collection<Location> findAll() {
        return super.findAll();
    }

    @Override
    public Location find(Integer id) {
        return super.find(id);
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public boolean delete(Location obj) {
        return super.delete(obj);
    }

    @Override
    public boolean update(Location obj) {
        return super.update(obj);
    }

    @Override
    public boolean create(Location obj) {
        return super.create(obj);
    }
    
    @Override
	public Location findByName(String name) {
		CriteriaBuilder cb = super.em.getCriteriaBuilder();
		CriteriaQuery<Location> cq = cb.createQuery(Location.class);
		Root<Location> tasks = cq.from(Location.class);
		cq.select(tasks).where(cb.like(tasks.get("name"), "%" + name + "%"));
		return super.em.createQuery(cq).getSingleResult();
	}

}
