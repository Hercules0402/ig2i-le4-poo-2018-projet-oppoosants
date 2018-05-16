package dao;

import java.util.Collection;
import metier.Graph;

/**
 * Représente un DAO de type Graph utilisant comme source de données une bdd.
 */
public class JpaGraphDao extends JpaDao<Graph> implements GraphDao {
    private static JpaGraphDao instance;

	/**
	 * Constrcuteur par défault.
	 */
	private JpaGraphDao() {
		super(Graph.class);
	}

	/**
	 * Retourne une instance de JpaGraphDao.
	 * @return JpaGraphDao
	 */
	public static JpaGraphDao getInstance() {
		if (instance == null) {
			instance = new JpaGraphDao();
		}
		return instance;
	}

    @Override
    public boolean deleteAll() {
        return super.deleteAll();
    }

    @Override
    public Collection<Graph> findAll() {
        return super.findAll();
    }

    @Override
    public Graph find(Integer id) {
        return super.find(id);
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public boolean delete(Graph obj) {
        return super.delete(obj);
    }

    @Override
    public boolean update(Graph obj) {
        return super.update(obj);
    }

    @Override
    public boolean create(Graph obj) {
        return super.create(obj);
    }
    
    
}
