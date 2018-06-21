package dao;

/**
 * Représente une frabrique DAO.
 */
public abstract class DaoFactory {
    /**
	 * Permet de retourner un objet DaoFactory.
	 * @param type.
	 * @return DaoFactory : objet DaoFacctory créé
	 */
	public static DaoFactory getDaoFactory(PersistenceType type) {
		if (type.equals(PersistenceType.JPA)) {
			return new DaoFactoryJpa();
		}
		return null;
	}

	/**
	 * Retourne un ArcDao.
	 * @return ArcDao : instance ArcDao
	 */
	public abstract ArcDao getArcDao();

    /**
	 * Retourne un BoxDao.
	 * @return BoxDao : instance BoxDao
	 */
	public abstract BoxDao getBoxDao();

    /**
	 * Retourne un GraphDao.
	 * @return GraphDao : instance GraphDao
	 */
	public abstract GraphDao getGraphDao();

    /**
	 * Retourne un LocationDao.
	 * @return LocationDao : instance LocationDao
	 */
	public abstract LocationDao getLocationDao();

    /**
	 * Retourne un OrderDao.
	 * @return OrderDao : instance OrderDao
	 */
	public abstract OrderDao getOrderDao();

    /**
	 * Retourne un ProdQtyDao.
	 * @return ProdQtyDao : instance ProdQtyDao
	 */
	public abstract ProdQtyDao getProdQtyDao();

    /**
	 * Retourne un ProductDao.
	 * @return ProductDao : instance ProductDao
	 */
	public abstract ProductDao getProductDao();

    /**
	 * Retourne un TrolleyDao.
	 * @return TrolleyDao : instance TrolleyDao
	 */
	public abstract TrolleyDao getTrolleyDao();

    /**
	 * Retourne un InstanceDao.
	 * @return InstanceDao : instance InstanceDao
	 */
	public abstract InstanceDao getInstanceDao();
}
