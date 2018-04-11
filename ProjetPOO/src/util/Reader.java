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
    private List<String> orders;
    private Integer nbVerticesIntersections;
    private Integer departingDepot;
    private Integer arrivalDepot;
    private List<String> arcs;
    private List<String> distances;
    private List<String> locations;

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
        orders = new ArrayList();
        arcs = new ArrayList();
        distances = new ArrayList();
        locations = new ArrayList();
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
        
        scan.nextLine();
        String ligne = null;
        while (!(ligne = scan.nextLine().trim()).isEmpty()) {
            if(!ligne.startsWith("//")) products.add(ligne);
        }

        nbOrders = Integer.parseInt(readNextString(scan).trim());
        
        while (!(ligne = scan.nextLine().trim()).isEmpty()) {
            if(!ligne.startsWith("//")) orders.add(ligne);
        }
        
        nbVerticesIntersections = Integer.parseInt(readNextString(scan).trim());
        departingDepot = Integer.parseInt(readNextString(scan).trim());
        arrivalDepot = Integer.parseInt(readNextString(scan).trim());
        
        scan.nextLine();
        while (!(ligne = scan.nextLine().trim()).isEmpty()) {
            if(!ligne.startsWith("//")) arcs.add(ligne);
        }
        
        while (!(ligne = scan.nextLine().trim()).isEmpty()) {
            if(!ligne.startsWith("//")) distances.add(ligne);
        }
        
        while (scan.hasNext() && !(ligne = scan.nextLine().trim()).isEmpty()) {
            if(!ligne.startsWith("//")) locations.add(ligne);
        }
    }

    @Override
    public String toString() {
        return "Reader{" 
                + "\tinstanceFile=" + instanceFile + ",\n"
                + "\tnbLocations=" + nbLocations + ",\n"
                + "\tnbProducts=" + nbProducts + ",\n"
                + "\tnbBoxesTrolley=" + nbBoxesTrolley + ",\n"
                + "\tnbDimensionsCapacity=" + nbDimensionsCapacity + ",\n"
                + "\tcapaBox=" + capaBox + ",\n"
                + "\tmixedOrders=" + mixedOrders + ",\n"
                + "\tproducts=" + products + ",\n"
                + "\tnbOrders=" + nbOrders + ",\n"
                + "\torders=" + orders + ",\n"
                + "\tnbVerticesIntersections=" + nbVerticesIntersections + ",\n"
                + "\tdepartingDepot=" + departingDepot + ",\n"
                + "\tarrivalDepot=" + arrivalDepot + ",\n"
                + "\tarcs=" + arcs + ",\n"
                + "\tdistances=" + distances + ",\n"
                + "\tlocations=" + locations + "\n"
                + '}';
    }
}