package util;

import java.io.File;
import java.util.Scanner;

/**
 * Permet de lire des fichiers instances.
 * @author Sébastien CORNUEL
 */
public class Reader {

	/**
	 * Représente un fichier instance à lire.
	 */
	private File instanceFile;

	/**
	 * Constructeur par données.
	 * @param filename TODO
	 * @throws java.lang.Exception
	 */
	public Reader(String filename) throws Exception {
		if (filename == null) {
			System.err.println("Erreur : Impossible d'ouvrir le fichier ...");
			System.err.println("=> Vérifier que le fichier existe bien dans le dossier.");
			throw new Exception();
		}
		this.instanceFile = new File(filename + ".txt");
		
	}
	
	/**
	 * Permet de récupérer les différents paramètres de type int comme NbLocations.
	 * @param scan TODO
	 * @return int
	 */
	private int lireProchainEntier(Scanner scan) {
		while (scan.hasNext("//.*")) {
			scan.nextLine();
		}
		return Integer.parseInt(scan.next());
	}
}
