package dao;

import metier.Location;

/**
 * Interface générique représentant un DAO de type Location.
 */
public interface LocationDao extends DAO<Location> {
    /**
	 * Permet de rechercher une location par nom.
	 * @param name
	 * @return Location
	 */
	public Location findByName(String name);
}
