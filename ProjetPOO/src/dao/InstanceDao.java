package dao;

import metier.Instance;

/**
 * Interface générique représentant un Dao de type Instance.
 */
public interface InstanceDao extends Dao<Instance> {

	/**
	 * Permet de rechercher une instance par nom.
	 * @param name
	 * @return Instance : instance trouvée
	 */
	public Instance findByName(String name);

}
