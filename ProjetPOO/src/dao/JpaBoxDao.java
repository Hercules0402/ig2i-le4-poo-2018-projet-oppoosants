package dao;

import java.util.Collection;
import metier.Box;

/**
 * Représente un DAO de type Box utilisant comme source de données une bdd.
 */
public class JpaBoxDao extends JpaDao<Box> implements BoxDao {
    private static JpaBoxDao instance;

	/**
	 * Constructeur par défault.
	 */
	private JpaBoxDao() {
		super(Box.class);
	}

	/**
	 * Retourne une instance de JpaBoxDao.
	 * @return JpaBoxDao
	 */
	public static JpaBoxDao getInstance() {
		if (instance == null) {
			instance = new JpaBoxDao();
		}
		return instance;
	}

    @Override
    public boolean deleteAll() {
        return super.deleteAll();
    }

    @Override
    public Collection<Box> findAll() {
        return super.findAll();
    }

    @Override
    public Box find(Integer id) {
        return super.find(id);
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public boolean delete(Box obj) {
        return super.delete(obj);
    }

    @Override
    public boolean update(Box obj) {
        return super.update(obj);
    }

    @Override
    public boolean create(Box obj) {
        return super.create(obj);
    }

}
