package util;

import dao.DaoFactory;
import dao.InstanceDao;
import dao.PersistenceType;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import metier.Arc;
import metier.Graph;
import metier.Instance;
import metier.Location;
import metier.Order;
import metier.ProdQty;
import metier.Product;

/**
 * Permet de lire des fichiers instances.
 */
public class Reader {
    private static Instance inst;
    private static File instanceFile;

    /**
     * Permet d'enregistrer les données dans la base de données et/ou stocker
     * dans un objet de type Instance.
     * @param filename
     * @param save 
     */
    public static Instance read(String filename, boolean save) {
         
        Long time = System.currentTimeMillis();
        if (filename == null) {
            System.err.println("Une erreur a été rencontrée : Impossible d'ouvrir le fichier ...");
            return null;
        }
        
        Reader.inst = new Instance(filename.substring(0,filename.indexOf('.')));
        
        Reader.instanceFile = new File(filename);
        System.out.println("Fichier trouvé.");

        try {
            readAll();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("READER EXECUTION TIME: " + (System.currentTimeMillis() - time) + "ms");

        if(save){
            time = System.currentTimeMillis();
            saveAll();
            System.out.println("SAVE DATA EXECUTION TIME: " + (System.currentTimeMillis() - time) + "ms");
        }
        return inst;
    }
    
    /**
     * Permet d'enregistrer les données dans la base de données.
     */
    public static void saveAll() {
        DaoFactory fabrique = DaoFactory.getDaoFactory(PersistenceType.JPA);

        InstanceDao instanceManager = fabrique.getInstanceDao();
        instanceManager.create(inst);

        /*System.out.println("[DEBUG] Enregistrement locations");
        LocationDao locationManager = fabrique.getLocationDao();
        for(Location l: inst.getLocations()) {
            locationManager.create(l);
        }

        System.out.println("[DEBUG] Enregistrement produits");
        ProductDao productManager = fabrique.getProductDao();
        for(Product p: inst.getProducts()) {
            productManager.create(p);
        }

        System.out.println("[DEBUG] Enregistrement arcs");
        ArcDao arcManager = fabrique.getArcDao();
        for(Arc a: inst.getArcs()) {
            arcManager.create(a);
        }

        System.out.println("[DEBUG] Enregistrement prodQty");
        OrderDao orderManager = fabrique.getOrderDao();
        for(Order o: inst.getOrders()) {
             orderManager.create(o);
        }

        System.out.println("[DEBUG] Enregistrement graph");
        GraphDao graphManager = fabrique.getGraphDao();        
        graphManager.create(inst.getGraph());*/
    }

    /**
     * Permet de récupérer les différentes lignes.
     * @param scan Scanner
     * @return String
     */
    private static String readNextString(Scanner scan) {
        while (scan.hasNext("//.*")) {
            scan.nextLine();
        }
        return scan.nextLine();
    }

    /**
     * Permet de lire les données écrites dans un fichier instance.
     * @throws FileNotFoundException 
     */
    public static void readAll() throws FileNotFoundException {
        Scanner scan = new Scanner(Reader.instanceFile);
        
        int nbLocations = Integer.parseInt(readNextString(scan).trim());
        int nbProducts = Integer.parseInt(readNextString(scan).trim());
        int nbBoxesTrolley = Integer.parseInt(readNextString(scan).trim());
        int nbDimensionsCapacity = Integer.parseInt(readNextString(scan).trim());
        
        inst.setNbBoxesTrolley(nbBoxesTrolley);

        List<Integer> capaBox = new ArrayList();
        for(String s : readNextString(scan).split("\\s")) {
            capaBox.add(Integer.parseInt(s.trim()));
        }

        inst.setWeightMaxBox(capaBox.get(0));
        inst.setVolumeMaxBox(capaBox.get(1));
        
        boolean mixedOrders = readNextString(scan).equals("1");

        scan.nextLine();
        String ligne = null;
        List<String> s_products = new ArrayList();
        while (!(ligne = scan.nextLine().trim()).isEmpty()) {
            if(!ligne.startsWith("//")) s_products.add(ligne);
        }

        int nbOrders = Integer.parseInt(readNextString(scan).trim());

        List<String> s_orders = new ArrayList();
        while (!(ligne = scan.nextLine().trim()).isEmpty()) {
            if(!ligne.startsWith("//")) s_orders.add(ligne);
        }

        int nbVerticesIntersections = Integer.parseInt(readNextString(scan).trim());
        int departingDepot = Integer.parseInt(readNextString(scan).trim());
        int arrivalDepot = Integer.parseInt(readNextString(scan).trim());

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

        createLocations(s_locations);
        createProducts(s_products);
        createOrders(s_orders);
        createArcs(s_arcs, false);
        createArcs(s_distances, true);
        
        Graph g = new Graph(inst.getLocations().get(departingDepot), inst.getLocations().get(arrivalDepot),
                nbLocations, nbProducts, nbVerticesIntersections,inst.getArcs(), inst);
        inst.setGraph(g);
    }

    public static void createProducts(List<String> s_products){
        List<Product> list = new ArrayList();
        for(String s : s_products){
            String ss[] = s.split("\\s");
            Location loc = inst.getLocations().get(Integer.parseInt(ss[1]));
            Product p = new Product(Integer.parseInt(ss[0]), loc, Integer.parseInt(ss[2]), Integer.parseInt(ss[3]), inst);
            list.add(p);
        }
        inst.setProducts(list);
    }

    public static void createOrders(List<String> s_orders){
        List<Order> list = new ArrayList();
        for(String s : s_orders){
            String ss[] = s.split("\\s");
            Integer id = Integer.parseInt(ss[0]);
            Integer m = Integer.parseInt(ss[1]);
            Integer nb = Integer.parseInt(ss[2]);
            ArrayList<ProdQty> prodQtys = new ArrayList<>();
            for(int i=3; i<(3+nb*2); i+=2){
                Product prod = inst.getProducts().get(Integer.parseInt(ss[i])-1);
                Integer quantite = Integer.parseInt(ss[i+1]);
                prodQtys.add(new ProdQty(prod, quantite));
            }
            Order o = new Order(id, m, nb, prodQtys, inst);
            list.add(o);
        }
        inst.setOrders(list);
    }

    public static void createArcs(List<String> s_arcs, boolean isShortestPath){
        List<Arc> list = new ArrayList();
        for(String s : s_arcs){
            String ss[] = s.split("\\s");
            Location start = inst.getLocations().get(Integer.parseInt(ss[0]));
            Location end = inst.getLocations().get(Integer.parseInt(ss[1]));
            
            Arc a = new Arc(start, end, Integer.parseInt(ss[2]), isShortestPath, inst);
            if(isShortestPath) {
                //System.out.println("Ajout à " + start.getName() + " du chemin vers " + end.getName());
                start.addDistance(end, Integer.parseInt(ss[2]));
            }
            list.add(a);
        }
        if(isShortestPath)
            inst.setDistances(list);
        else
            inst.setArcs(list);
    }

    public static void createLocations(List<String> s_locations){
        List<Location> list = new ArrayList();
        for(String s : s_locations){
            String ss[] = s.split("\\s");
            Location l = new Location(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]), Integer.parseInt(ss[2]), ss[3].substring(1, ss[3].length()-1), inst);
            list.add(l);
        }
        inst.setLocations(list);
    }
}