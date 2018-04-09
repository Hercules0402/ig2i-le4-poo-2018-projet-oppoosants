package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Permet de lire des fichiers instances.
 */
public class Reader {
    private File instanceFile;
    private Integer nbLocations;
    private Integer nbProducts;
    private Integer nbBoxesTrolley;
    private Integer nbDimensionsCapacity;
    private Integer[] capaBox;
    private Boolean mixedOrders;

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
        readAll();
    }

    /**
     * Permet de récupérer les différentes lignes.
     * @param scan Scanner
     * @return String
     */
    private String readNextString(Scanner scan) {
        while (scan.hasNext("//.*")) {
            scan.nextLine();
        }
        return scan.nextLine();
    }

    public void readAll() throws FileNotFoundException {
        Scanner scan = new Scanner(this.instanceFile);
        
        nbLocations = Integer.parseInt(readNextString(scan).trim());
        nbProducts = Integer.parseInt(readNextString(scan).trim());
        nbBoxesTrolley = Integer.parseInt(readNextString(scan).trim());
        nbDimensionsCapacity = Integer.parseInt(readNextString(scan).trim());
        
        capaBox = new Integer[nbDimensionsCapacity];
        String s[] = readNextString(scan).split("\\s");
        for(int i=0; i<s.length; i++)
            capaBox[i] = Integer.parseInt(s[i].trim());
      
        mixedOrders = readNextString(scan).equals("1");
    }

    @Override
    public String toString() {
        return "Reader{" + "instanceFile=" + instanceFile 
                + ", nbLocations=" + nbLocations 
                + ", nbProducts=" + nbProducts 
                + ", nbBoxesTrolley=" + nbBoxesTrolley 
                + ", nbDimensionsCapacity=" + nbDimensionsCapacity 
                + ", capaBox=" + capaBox 
                + ", mixedOrders=" + mixedOrders + '}';
    }
}