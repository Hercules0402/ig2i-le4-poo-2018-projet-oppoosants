package util;

import dao.ArcDao;
import dao.DaoFactory;
import dao.GraphDao;
import dao.InstanceDao;
import dao.LocationDao;
import dao.OrderDao;
import dao.PersistenceType;
import dao.ProductDao;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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
    private Instance instance;

    /**
     * Constructeur par données.
     * @param filename TODO
     * @param save Enregistrer ou non les données en base
     * @throws java.lang.Exception
     */
    public Reader(String filename, boolean save) throws Exception {
        Long time = System.currentTimeMillis();
        if (filename == null) {
            System.err.println("Une erreur a été rencontrée : Impossible d'ouvrir le fichier ...");
            throw new Exception();
        }
        this.instanceFile = new File(filename);
        System.out.println("Fichier trouvé.");

        this.instance = new Instance((this.instanceFile.getName() != null) ? this.instanceFile.getName().substring(0,this.instanceFile.getName().indexOf('.')) : "");

        readAll();
        System.out.println("READER EXECUTION TIME: " + (System.currentTimeMillis() - time) + "ms");

        if(save){
            time = System.currentTimeMillis();
            saveAll();
            System.out.println("SAVE DATA EXECUTION TIME: " + (System.currentTimeMillis() - time) + "ms");
        }
    }

    public void saveAll() {
        DaoFactory fabrique = DaoFactory.getDaoFactory(PersistenceType.JPA);

        InstanceDao instanceManager = fabrique.getInstanceDao();
        instanceManager.create(this.getInstance());

        System.out.println("[DEBUG] Enregistrement locations");
        LocationDao locationManager = fabrique.getLocationDao();
        for(Location l: this.getLocations()) {
            locationManager.create(l);
        }

        System.out.println("[DEBUG] Enregistrement produits");
        ProductDao productManager = fabrique.getProductDao();
        for(Product p: this.getProducts()) {
            productManager.create(p);
        }

        System.out.println("[DEBUG] Enregistrement arcs");
        ArcDao arcManager = fabrique.getArcDao();
        for(Arc a: this.getArcs()) {
            arcManager.create(a);
        }

        System.out.println("[DEBUG] Enregistrement prodQty");
        OrderDao orderManager = fabrique.getOrderDao();
        for(Order o: this.getOrders()) {
             orderManager.create(o);
        }

        System.out.println("[DEBUG] Enregistrement graph");
        GraphDao graphManager = fabrique.getGraphDao();
        Graph g = new Graph(this.getDepartingDepot(), this.getArrivalDepot(),
                this.getNbLocations(), this.getNbProducts(), 
                this.getNbVerticesIntersections(), this.getArcs(), this.getInstance());
        graphManager.create(g);
    }

    /**
     * Permet de récupérer une ligne au sein du fichier instance tout en passant les lignes titres
     * @param scan Scanner itérateur de lignes
     * @return String ligne récupérée
     */
    private String readNextString(Scanner scan) {
        while (scan.hasNext("//.*")) {
            scan.nextLine();
        }
        return scan.nextLine();
    }

    /**
     * Permet de récupérer une ligne au sein du fichier instance tout en passant les lignes titres
     * @throws java.io.FileNotFoundException
     */
    public void readAll() throws FileNotFoundException {
        Scanner scan = new Scanner(this.instanceFile);

        nbLocations = Integer.parseInt(readNextString(scan).trim());
        nbProducts = Integer.parseInt(readNextString(scan).trim());
        nbBoxesTrolley = Integer.parseInt(readNextString(scan).trim());
        nbDimensionsCapacity = Integer.parseInt(readNextString(scan).trim());

        capaBox = new ArrayList();
        for(String s : readNextString(scan).split("\\s")) {
            capaBox.add(Integer.parseInt(s.trim()));
        }

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
            Product p = new Product(Integer.parseInt(ss[0]), loc, Integer.parseInt(ss[2]), Integer.parseInt(ss[3]), this.instance);
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
            ArrayList<ProdQty> prodQtys = new ArrayList<>();
            for(int i=3; i<(3+nb*2); i+=2){
                Product prod = products.get(Integer.parseInt(ss[i])-1);
                Integer quantite = Integer.parseInt(ss[i+1]);
                prodQtys.add(new ProdQty(prod, quantite));
            }
            Order o = new Order(id, m, nb, prodQtys, this.instance);
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
            Arc a = new Arc(start, end, Integer.parseInt(ss[2]), isShortestPath, this.instance);
            list.add(a);
        }
        return list;
    }
    public List<Location> createLocations(List<String> s_locations){
        List<Location> list = new ArrayList();
        for(String s : s_locations){
            String ss[] = s.split("\\s");
            Location l = new Location(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]), Integer.parseInt(ss[2]), ss[3].substring(1, ss[3].length()-1), this.instance);
            list.add(l);
        }
        return list;
    }

    public Integer getNbBoxesTrolley() {
        return nbBoxesTrolley;
    }

    public List<Integer> getCapaBox() {
        return capaBox;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public List<Arc> getArcs() {
        return arcs;
    }

    public List<Arc> getDistances() {
        return distances;
    }

    public Integer getNbLocations() {
        return nbLocations;
    }

    public Integer getNbProducts() {
        return nbProducts;
    }

    public Integer getNbVerticesIntersections() {
        return nbVerticesIntersections;
    }

    public Location getDepartingDepot() {
        return locations.get(departingDepot);
    }

    public Location getArrivalDepot() {
        return locations.get(arrivalDepot);
    }

    public Instance getInstance() {
        return instance;
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