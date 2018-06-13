package dao;

import java.util.Collection;
import metier.Product;

/**
 * Représente un DAO de type Product utilisant comme source de données une bdd.
 */
public class JpaProductDao extends JpaDao<Product> implements ProductDao {
    private static JpaProductDao instance;

	/**
	 * Constructeur par défault.
	 */
	private JpaProductDao() {
		super(Product.class);
	}

	/**
	 * Retourne une instance de JpaProductDao.
	 * @return JpaProductDao
	 */
	public static JpaProductDao getInstance() {
		if (instance == null) {
			instance = new JpaProductDao();
		}
		return instance;
	}

    @Override
    public boolean deleteAll() {
        return super.deleteAll();
    }

    @Override
    public Collection<Product> findAll() {
        return super.findAll();
    }

    @Override
    public Product find(Integer id) {
        return super.find(id);
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public boolean delete(Product obj) {
        return super.delete(obj);
    }

    @Override
    public boolean update(Product obj) {
        return super.update(obj);
    }

    @Override
    public boolean create(Product obj) {
        return super.create(obj);
    }

}
