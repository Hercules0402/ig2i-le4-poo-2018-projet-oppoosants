package dao;

import java.util.Collection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import metier.Instance;

/**
 * Représente un DAO de type Instance utilisant comme source de données une bdd.
 */
public class JpaInstanceDao extends JpaDao<Instance> implements InstanceDao {

	private static JpaInstanceDao instance;

	/**
	 * Constructeur par défault.
	 */
	private JpaInstanceDao() {
		super(Instance.class);
	}

	/**
	 * Retourne une instance de JpaInstanceDao.
	 * @return JpaInstanceDao : une nouvelle instance ou une déjà existante
	 */
	public static JpaInstanceDao getInstance() {
		if (instance == null) {
			instance = new JpaInstanceDao();
		}
		return instance;
	}

	@Override
	public boolean deleteAll() {
		return super.deleteAll();
	}

	@Override
	public Collection<Instance> findAll() {
		return super.findAll();
	}

	@Override
	public Instance find(Integer id) {
		return super.find(id);
	}

	@Override
	public void close() {
		super.close();
	}

	@Override
	public boolean delete(Instance obj) {
		return super.delete(obj);
	}

	@Override
	public boolean update(Instance obj) {
		return super.update(obj);
	}

	@Override
	public boolean create(Instance obj) {
		return super.create(obj);
	}

	@Override
	public Instance findByName(String name) {
		CriteriaBuilder cb = super.em.getCriteriaBuilder();
		CriteriaQuery<Instance> cq = cb.createQuery(Instance.class);
		Root<Instance> tasks = cq.from(Instance.class);
		cq.select(tasks).where(cb.like(tasks.get("nom"), "%" + name + "%"));
		return super.em.createQuery(cq).getSingleResult();
	}

}
