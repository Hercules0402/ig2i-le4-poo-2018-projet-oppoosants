package dao;

import java.util.Collection;
import metier.Arc;

/**
 * Représente un DAO de type Arc utilisant comme source de données une bdd.
 */
public class JpaArcDao extends JpaDao<Arc> implements ArcDao {
    private static JpaArcDao instance;

	/**
	 * Constructeur par défault.
	 */
	private JpaArcDao() {
		super(Arc.class);
	}

	/**
	 * Retourne une instance de JpaArcDao.
	 * @return JpaArcDao
	 */
	public static JpaArcDao getInstance() {
		if (instance == null) {
			instance = new JpaArcDao();
		}
		return instance;
	}

    @Override
    public boolean deleteAll() {
        return super.deleteAll();
    }

    @Override
    public Collection<Arc> findAll() {
        return super.findAll();
    }

    @Override
    public Arc find(Integer id) {
        return super.find(id);
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public boolean delete(Arc obj) {
        return super.delete(obj);
    }

    @Override
    public boolean update(Arc obj) {
        return super.update(obj);
    }

    @Override
    public boolean create(Arc obj) {
        return super.create(obj);
    }

}
