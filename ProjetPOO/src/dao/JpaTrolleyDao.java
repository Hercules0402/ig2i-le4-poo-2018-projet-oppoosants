package dao;

import java.util.Collection;
import metier.Trolley;

/**
 * Représente un DAO de type Trolley utilisant comme source de données une bdd.
 */
public class JpaTrolleyDao extends JpaDao<Trolley> implements TrolleyDao {
    private static JpaTrolleyDao instance;

	/**
	 * Constrcuteur par défault.
	 */
	private JpaTrolleyDao() {
		super(Trolley.class);
	}

	/**
	 * Retourne une instance de JpaTrolleyDao.
	 * @return JpaTrolleyDao
	 */
	public static JpaTrolleyDao getInstance() {
		if (instance == null) {
			instance = new JpaTrolleyDao();
		}
		return instance;
	}

    @Override
    public boolean deleteAll() {
        return super.deleteAll();
    }

    @Override
    public Collection<Trolley> findAll() {
        return super.findAll();
    }

    @Override
    public Trolley find(Integer id) {
        return super.find(id);
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public boolean delete(Trolley obj) {
        return super.delete(obj);
    }

    @Override
    public boolean update(Trolley obj) {
        return super.update(obj);
    }

    @Override
    public boolean create(Trolley obj) {
        return super.create(obj);
    }
    
    
}
