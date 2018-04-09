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
    
    public int getNbLocations() throws FileNotFoundException {
        return readNextInt(new Scanner(this.instanceFile));
    }
    
    public static void main(String[] args) throws Exception {
        Reader r = new Reader("instance_0116_131940_Z2");
        System.out.println(r.getNbLocations());
    }
}
