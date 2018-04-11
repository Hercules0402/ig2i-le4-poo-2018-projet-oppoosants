package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import metier.Arc;
import metier.Location;
import metier.Order;
import metier.Product;

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
    private List<Product> products;
    private Integer nbOrders;
    private List<Order> orders;
    private Integer nbVerticesIntersections;
    private Integer departingDepot;
    private Integer arrivalDepot;
    private List<Arc> arcs;
    private List<Arc> distances;
    private List<Location> locations;

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
        this.instanceFile = new File(filename);
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
        
        capaBox = new ArrayList();
        for(String s : readNextString(scan).split("\\s"))
            capaBox.add(Integer.parseInt(s.trim()));
      
        mixedOrders = readNextString(scan).equals("1");
        
        scan.nextLine();
        String ligne = null;
        List<String> s_products = new ArrayList();
        while (!(ligne = scan.nextLine().trim()).isEmpty()) {
            if(!ligne.startsWith("//")) s_products.add(ligne);
        }
        products = createProducts(s_products);

        nbOrders = Integer.parseInt(readNextString(scan).trim());
        
        List<String> s_orders = new ArrayList();
        while (!(ligne = scan.nextLine().trim()).isEmpty()) {
            if(!ligne.startsWith("//")) s_orders.add(ligne);
        }
        orders = createOrders(s_orders);
        
        nbVerticesIntersections = Integer.parseInt(readNextString(scan).trim());
        departingDepot = Integer.parseInt(readNextString(scan).trim());
        arrivalDepot = Integer.parseInt(readNextString(scan).trim());
        
        scan.nextLine();
        List<String> s_arcs = new ArrayList();
        while (!(ligne = scan.nextLine().trim()).isEmpty()) {
            if(!ligne.startsWith("//")) s_arcs.add(ligne);
        }
        arcs = createArcs(s_arcs, false);
        
        List<String> s_distances = new ArrayList();
        while (!(ligne = scan.nextLine().trim()).isEmpty()) {
            if(!ligne.startsWith("//")) s_distances.add(ligne);
        }
        distances = createArcs(s_distances, true);
        
        List<String> s_locations = new ArrayList();
        while (scan.hasNext() && !(ligne = scan.nextLine().trim()).isEmpty()) {
            if(!ligne.startsWith("//")) s_locations.add(ligne);
        }
        locations = createLocations(s_locations);
    }
    
    public static List<Product> createProducts(List<String> s_products){
        List<Product> list = new ArrayList();
        for(String s : s_products){
            String ss[] = s.split("\\s");
            Product p = new Product(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]), Integer.parseInt(ss[2]), Integer.parseInt(ss[3]));
            list.add(p);
        }
        return list;
    }
    
    public static List<Order> createOrders(List<String> s_orders){
        List<Order> list = new ArrayList();
        list = null;
        return list;
    }
    
    public static List<Arc> createArcs(List<String> s_arcs, boolean isShortestPath){
        List<Arc> list = new ArrayList();
        list = null;
        return list;
    }
    
    public static List<Location> createLocations(List<String> s_locations){
        List<Location> list = new ArrayList();
        for(String s : s_locations){
            String ss[] = s.split("\\s");
            Location l = new Location(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]), Integer.parseInt(ss[2]), ss[3].substring(1, ss[3].length()-1));
            list.add(l);
            System.out.println(l.getName());
        }
        return list;
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