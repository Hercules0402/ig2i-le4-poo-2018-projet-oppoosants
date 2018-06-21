package dao;

/**
 * Représente une frabrique DAO.
 */
public abstract class DaoFactory {
    /**
	 * Peremt de retourner un DaoFactory.
	 * @param type.
	 * @return DaoFactory
	 */
	public static DaoFactory getDaoFactory(PersistenceType type) {
		if (type.equals(PersistenceType.JPA)) {
			return new DaoFactoryJpa();
		}
		return null;
	}

	/**
	 * Retourne un ArcDao.
	 * @return ArcDao
	 */
	public abstract ArcDao getArcDao();

    /**
	 * Retourne un BoxDao.
	 * @return BoxDao
	 */
	public abstract BoxDao getBoxDao();

    /**
	 * Retourne un GraphDao.
	 * @return GraphDao
	 */
	public abstract GraphDao getGraphDao();

    /**
	 * Retourne un LocationDao.
	 * @return LocationDao
	 */
	public abstract LocationDao getLocationDao();

    /**
	 * Retourne un OrderDao.
	 * @return OrderDao
	 */
	public abstract OrderDao getOrderDao();

    /**
	 * Retourne un ProdQtyDao.
	 * @return ProdQtyDao
	 */
	public abstract ProdQtyDao getProdQtyDao();

    /**
	 * Retourne un ProductDao.
	 * @return ProductDao
	 */
	public abstract ProductDao getProductDao();

    /**
	 * Retourne un TrolleyDao.
	 * @return TrolleyDao
	 */
	public abstract TrolleyDao getTrolleyDao();

    /**
	 * Retourne un InstanceDao.
	 * @return InstanceDao
	 */
	public abstract InstanceDao getInstanceDao();
}
