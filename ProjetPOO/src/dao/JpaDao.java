package dao;

import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Représente un Dao utilisant comme source de données une bdd.
 * @param <T>
 */
public abstract class JpaDao<T> implements Dao<T> {
    protected EntityManagerFactory emf;

	protected EntityManager em;

	protected final static String PERSISTENCE_UNIT = "pu";

    protected Class<T> entite;

	/**
	 * Constructeur par données.
	 * @param entite
	 */
	public JpaDao(Class<T> entite) {
		this.entite = entite;
		this.emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
		this.em = this.emf.createEntityManager();
	}

	@Override
	public boolean create(T obj) {
		EntityTransaction et = this.em.getTransaction();

		try {
			et.begin();
			em.persist(obj);
			et.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean update(T obj) {
		EntityTransaction et = this.em.getTransaction();

		try {
			et.begin();
			em.merge(obj);
			et.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean delete(T obj) {
		EntityTransaction et = this.em.getTransaction();

		try {
			et.begin();
			em.remove(obj);
			et.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public void close() {
		if (this.emf != null) {
			this.emf.close();
		}

		if (this.em != null) {
			this.em.close();
		}
	}

	@Override
	public T find(Integer id) {
		return this.em.find(this.entite, id);
	}

	@Override
	public Collection<T> findAll() {
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(this.entite);
		Root<T> tacks = cq.from(this.entite);
		cq.select(tacks);
		return this.em.createQuery(cq).getResultList();
	}

	@Override
	public boolean deleteAll() {
		EntityTransaction et = this.em.getTransaction();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaDelete<T> cd = cb.createCriteriaDelete(this.entite);
			et.begin();
			int nbDelete = em.createQuery(cd).executeUpdate();
			et.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}