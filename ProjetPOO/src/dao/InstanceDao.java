package dao;

import metier.Instance;

/**
 * Interface générique représentant un Dao de type Instance.
 */
public interface InstanceDao extends Dao<Instance> {

	/**
	 * Permet derechercher une instance par nom.
	 * @param name
	 * @return Instance
	 */
	public Instance findByName(String name);

}
