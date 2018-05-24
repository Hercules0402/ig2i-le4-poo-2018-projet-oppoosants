package dao;

import metier.Instance;

/**
 * Interface générique représentant un DAO de type Instance.
 */
public interface InstanceDao extends DAO<Instance> {

	/**
	 * Permet derechercher une instance par nom.
	 * @param name TODO
	 * @return Instance
	 */
	public Instance findByName(String name);

}
