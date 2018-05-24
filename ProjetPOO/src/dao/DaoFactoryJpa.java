package dao;

/**
 * Représente une frabrique DAO de type JPA.
 */
public class DaoFactoryJpa extends DaoFactory {    

    @Override
    public ArcDao getArcDao() {
        return JpaArcDao.getInstance();
    }

    @Override
    public BoxDao getBoxDao() {
        return JpaBoxDao.getInstance();
    }

    @Override
    public GraphDao getGraphDao() {
        return JpaGraphDao.getInstance();
    }

    @Override
    public LocationDao getLocationDao() {
        return JpaLocationDao.getInstance();
    }

    @Override
    public OrderDao getOrderDao() {
        return JpaOrderDao.getInstance();
    }

    @Override
    public ProdQtyDao getProdQtyDao() {
        return JpaProdQtyDao.getInstance();
    }

    @Override
    public ProductDao getProductDao() {
        return JpaProductDao.getInstance();
    }

    @Override
    public TrolleyDao getTrolleyDao() {
        return JpaTrolleyDao.getInstance();
    }

    @Override
    public InstanceDao getInstanceDao() {
        return JpaInstanceDao.getInstance();
    }
}
