package dao;

import java.util.Collection;
import metier.ProdQty;

/**
 * Représente un DAO de type ProdQty utilisant comme source de données une bdd.
 */
public class JpaProdQtyDao extends JpaDao<ProdQty> implements ProdQtyDao {
    private static JpaProdQtyDao instance;

	/**
	 * Constructeur par défault.
	 */
	private JpaProdQtyDao() {
		super(ProdQty.class);
	}

	/**
	 * Retourne une instance de JpaProdQtyDao.
	 * @return JpaProdQtyDao : une nouvelle instance ou une déjà existante
	 */
	public static JpaProdQtyDao getInstance() {
		if (instance == null) {
			instance = new JpaProdQtyDao();
		}
		return instance;
	}

    @Override
    public boolean deleteAll() {
        return super.deleteAll();
    }

    @Override
    public Collection<ProdQty> findAll() {
        return super.findAll();
    }

    @Override
    public ProdQty find(Integer id) {
        return super.find(id);
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public boolean delete(ProdQty obj) {
        return super.delete(obj);
    }

    @Override
    public boolean update(ProdQty obj) {
        return super.update(obj);
    }

    @Override
    public boolean create(ProdQty obj) {
        return super.create(obj);
    }

}
