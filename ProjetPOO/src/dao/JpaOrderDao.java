package dao;

import java.util.Collection;
import metier.Order;

/**
 * Représente un DAO de type Order utilisant comme source de données une bdd.
 */
public class JpaOrderDao extends JpaDao<Order> implements OrderDao {
    private static JpaOrderDao instance;

	/**
	 * Constrcuteur par défault.
	 */
	private JpaOrderDao() {
		super(Order.class);
	}

	/**
	 * Retourne une instance de JpaOrderDao.
	 * @return JpaOrderDao
	 */
	public static JpaOrderDao getInstance() {
		if (instance == null) {
			instance = new JpaOrderDao();
		}
		return instance;
	}

    @Override
    public boolean deleteAll() {
        return super.deleteAll();
    }

    @Override
    public Collection<Order> findAll() {
        return super.findAll();
    }

    @Override
    public Order find(Integer id) {
        return super.find(id);
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public boolean delete(Order obj) {
        return super.delete(obj);
    }

    @Override
    public boolean update(Order obj) {
        return super.update(obj);
    }

    @Override
    public boolean create(Order obj) {
        return super.create(obj);
    }

}
