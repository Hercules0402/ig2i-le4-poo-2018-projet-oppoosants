package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
    private List<Integer> capaBox;
    private Boolean mixedOrders;
    private List<String> products;
    private Integer nbOrders;

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
        
        capaBox = new ArrayList();
        products = new ArrayList();
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
        
        for(String s : readNextString(scan).split("\\s"))
            capaBox.add(Integer.parseInt(s.trim()));
      
        mixedOrders = readNextString(scan).equals("1");
        
        String ligne = null;
        while (!(ligne = scan.nextLine()).isEmpty()) {
            if(!ligne.startsWith("//")) products.add(ligne);
        }

        nbOrders = Integer.parseInt(readNextString(scan).trim());
    }

    @Override
    public String toString() {
        return "Reader{" + "instanceFile=" + instanceFile 
                + ", nbLocations=" + nbLocations 
                + ", nbProducts=" + nbProducts 
                + ", nbBoxesTrolley=" + nbBoxesTrolley 
                + ", nbDimensionsCapacity=" + nbDimensionsCapacity 
                + ", capaBox=" + capaBox 
                + ", mixedOrders=" + mixedOrders 
                + ", products=" + products
                + ", nbOrders=" + nbOrders
                + '}';
    }
}