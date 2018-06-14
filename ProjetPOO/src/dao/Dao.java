package dao;

import java.util.Collection;

/**
 * Interface générique représentant un Dao.
 * @param <T>
 */
public interface Dao<T> {
    /**
	 * Méthode permettant de créer un objet.
	 * @param obj
	 * @return boolean : créé ou pas
	 */
	public boolean create(T obj);

	/**
	 * Méthode permettant de rechercher un objet.
	 * @param id
	 * @return object : objet trouvé
	 */
	public T find(Integer id);

	/**
	 * Méthode permettant de rechercher une collection d'objet.
	 * @return collection of object : liste d'objets trouvés
	 */
	public Collection<T> findAll();

	/**
	 * Méthode permettant de mettre à jour un objet.
	 * @param obj
	 * @return boolean : mis à jour ou pas
	 */
	public boolean update(T obj);

	/**
	 * Méthode permettant de supprimer un objet.
	 * @param obj
	 * @return boolean : supprimé ou pas
	 */
	public boolean delete(T obj);

	/**
	 * Méthode permettant de supprimer un ensemble d'objet.
	 * @return boolean : objets supprimés ou pas
	 */
	public boolean deleteAll();

	/**
	 * Méthode permettant de fermer la connexion à la source des données (bdd ou fichier).
	 */
	public void close();
}
