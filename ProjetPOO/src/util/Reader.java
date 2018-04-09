package util;

import java.io.File;
import java.io.FileNotFoundException;
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
            System.err.println("Une erreur a été rencontrée : Impossible d'ouvrir le fichier ...");
            throw new Exception();
        }
        this.instanceFile = new File(filename + ".txt");
        System.out.println("Fichier trouvé.");
        
    }

    public File getInstanceFile() {
        return instanceFile;
    }

    /**
     * Permet de récupérer les différents paramètres de type int comme NbLocations.
     * @param scan TODO
     * @return int
     */
    private int readNextInt(Scanner scan) {
        while (scan.hasNext("//.*")) {
            scan.nextLine();
        }
        return Integer.parseInt(scan.next());
    }

    /**
     * Retourne le nombre de localisations.
     * @param scan TODO
     * @return int
     */
    public int getNbLocations(Scanner scan) {
        return readNextInt(scan);
    }

    /**
     * Retourne le nombre de produits.
     * @param scan TODO
     * @return int
     */
    public int getNbProducts(Scanner scan) {
        return readNextInt(scan);
    }

    /**
     * Main.
     * @param args TODO
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        Reader r = new Reader("instance_0116_131940_Z2");
        Scanner scan = null;
        try {
            scan = new Scanner(r.getInstanceFile());
            System.out.println("NbLocations : " + r.getNbLocations(scan));
            System.out.println("NbProducts : " + r.getNbProducts(scan));
        } catch (FileNotFoundException ex) {
            System.err.println("Une erreur a été rencontrée : Fichier introuvable...");
        }
    }
}
