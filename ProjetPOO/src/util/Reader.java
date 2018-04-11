package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
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
        Long time = System.currentTimeMillis();
        if (filename == null) {
            System.err.println("Une erreur a été rencontrée : Impossible d'ouvrir le fichier ...");
            throw new Exception();
        }
        this.instanceFile = new File(filename);
        System.out.println("Fichier trouvé.");

        readAll();
        System.out.println("EXECUTION TIME: " + (System.currentTimeMillis() - time) + "ms");
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

        nbOrders = Integer.parseInt(readNextString(scan).trim());
        
        List<String> s_orders = new ArrayList();
        while (!(ligne = scan.nextLine().trim()).isEmpty()) {
            if(!ligne.startsWith("//")) s_orders.add(ligne);
        }
        
        nbVerticesIntersections = Integer.parseInt(readNextString(scan).trim());
        departingDepot = Integer.parseInt(readNextString(scan).trim());
        arrivalDepot = Integer.parseInt(readNextString(scan).trim());
        
        scan.nextLine();
        List<String> s_arcs = new ArrayList();
        while (!(ligne = scan.nextLine().trim()).isEmpty()) {
            if(!ligne.startsWith("//")) s_arcs.add(ligne);
        }
        
        List<String> s_distances = new ArrayList();
        while (!(ligne = scan.nextLine().trim()).isEmpty()) {
            if(!ligne.startsWith("//")) s_distances.add(ligne);
        }
        
        List<String> s_locations = new ArrayList();
        while (scan.hasNext() && !(ligne = scan.nextLine().trim()).isEmpty()) {
            if(!ligne.startsWith("//")) s_locations.add(ligne);
        }
        
        locations = createLocations(s_locations);
        products = createProducts(s_products);
        orders = createOrders(s_orders);
        arcs = createArcs(s_arcs, false);
        distances = createArcs(s_distances, true);
    }
    
    public List<Product> createProducts(List<String> s_products){
        List<Product> list = new ArrayList();
        for(String s : s_products){
            String ss[] = s.split("\\s");
            Location loc = locations.get(Integer.parseInt(ss[1]));
            Product p = new Product(Integer.parseInt(ss[0]), loc, Integer.parseInt(ss[2]), Integer.parseInt(ss[3]));
            list.add(p);
        }
        return list;
    }
    
    public List<Order> createOrders(List<String> s_orders){
        List<Order> list = new ArrayList();
        for(String s : s_orders){
            String ss[] = s.split("\\s");
            Integer id = Integer.parseInt(ss[0]);
            Integer m = Integer.parseInt(ss[1]);
            Integer nb = Integer.parseInt(ss[2]);
            HashMap<Product, Integer> map = new HashMap();
            for(int i=3; i<(3+nb*2); i+=2){
                Product prod = products.get(Integer.parseInt(ss[i])-1);
                Integer quantite = Integer.parseInt(ss[i+1]);
                map.put(prod, quantite);
            }
            Order o = new Order(id, m, nb, map);
            list.add(o);
        }
        return list;
    }
    
    public List<Arc> createArcs(List<String> s_arcs, boolean isShortestPath){
        List<Arc> list = new ArrayList();
        for(String s : s_arcs){
            String ss[] = s.split("\\s");
            Location start = locations.get(Integer.parseInt(ss[0]));
            Location end = locations.get(Integer.parseInt(ss[1]));
            Arc a = new Arc(start, end, Integer.parseInt(ss[2]), isShortestPath);
            list.add(a);
        }
        return list;
    }
    
    public List<Location> createLocations(List<String> s_locations){
        List<Location> list = new ArrayList();
        for(String s : s_locations){
            String ss[] = s.split("\\s");
            Location l = new Location(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]), Integer.parseInt(ss[2]), ss[3].substring(1, ss[3].length()-1));
            list.add(l);
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