/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import metier.Location;

/**
 * Interface générique représentant un DAO de type Location.
 */
public interface LocationDao extends DAO<Location>{
    /**
	 * Permet de rechercher une location par nom.
	 * @param name TODO
	 * @return Location
	 */
	public Location findByName(String name);
}
