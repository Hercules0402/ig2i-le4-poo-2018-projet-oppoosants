package dao;

import metier.Location;

/**
 * Interface générique représentant un Dao de type Location.
 */
public interface LocationDao extends Dao<Location> {
    /**
	 * Permet de rechercher une location par nom.
	 * @param name
	 * @return Location location trouvée
	 */
	public Location findByName(String name);
}
