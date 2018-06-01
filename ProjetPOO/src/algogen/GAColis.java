package algogen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import metier.Instance;
import metier.Location;
import metier.Order;
import metier.ProdQty;
import metier.Product;

public class GAColis {
    private static Order order;
    
    private static Instance instance;
    private static List<Product> products;
    private static int nbColisMax;
    private static int weightMaxBox;
    private static int volumeMaxBox;
    
    private static List<ArrayList<Integer>> popList;
    private static Map<Integer, Integer> results;
    private static int tempBest;
    
    private static ProdQty[] data;
    
    public static List<ArrayList<Integer>> runTest(Order o, Instance inst){
        GAColis.run(o, inst);
        return popList;
    }
    
    public static List<Product> run(Order o, Instance inst){
        GAColis.order = o;
        
        instance = inst;
        products = new ArrayList<>(inst.getProducts());
        nbColisMax = inst.getNbBoxesTrolley();
        weightMaxBox = inst.getWeightMaxBox();
        volumeMaxBox = inst.getVolumeMaxBox();
    
        genereration();
        printPopList("Génération population initiale");
        
        evaluation();
        printResults("Evaluation");
        
        selection();
        printPopList("Selection");
        
        crossover();
        printPopList("Cross-over");
        
        return null;
    }
    
    public static void genereration(){
        data = basicsmart(false);
        popList = new ArrayList<ArrayList<Integer>>();
        
        ArrayList<Integer> list = new ArrayList<>();
        for(int j=0; j<data.length; j++) list.add(j);
        popList.add(list);
                
        for(int i=0; i<9; i++){
            ArrayList<Integer> nList = new ArrayList<Integer>(list);
            Collections.shuffle(nList);
            popList.add(nList);
        }
    }
    
    public static void evaluation(){
        results = new HashMap<>();
        int i = 0;
        for(ArrayList<Integer> genome : popList){
            ArrayList<ProdQty> products = new ArrayList<>();
            for(Integer nb : genome)
                products.add(data[nb]);
            int r = split(products);
            results.put(i, r);
            i++;
        }
    }
    
    public static void selection(){
        Map.Entry<Integer, Integer> max1 = null;
        Map.Entry<Integer, Integer> max2 = null;

        for(Map.Entry<Integer, Integer> en : results.entrySet()){
            if (max1 == null || en.getValue().compareTo(max1.getValue()) > 0){
                max1 = en;
            }                   
        }
        for(Map.Entry<Integer, Integer> en : results.entrySet()){
            if (en != max1 && (max2 == null || (en.getValue().compareTo(max2.getValue()) >0))) {
                max2 = en;
            }                  
        }
 
        List<ArrayList<Integer>> newPopList = new ArrayList<ArrayList<Integer>>();
        newPopList.add(popList.get(max1.getKey()));
        newPopList.add(popList.get(max2.getKey()));
        popList = new ArrayList<ArrayList<Integer>>(newPopList);
        
        tempBest = max1.getValue();
    }
    
    public static void crossover(){
        ArrayList<Integer> p1 = popList.get(0);
        ArrayList<Integer> p2 = popList.get(1);
        
        for(int i=0; i<4; i++) {
            Map<Integer, ArrayList<Integer>> cs = OX.crossover(p1, p2);
            popList.add(cs.get(0));
            popList.add(cs.get(1));
        }
    }
    
    /**
     * Fonctions basicsmart(pour generation) et split (pour evaluation)
     */
    
    public static ProdQty[] basicsmart(boolean random){
        List<ProdQty> listPq = order.getProdQtys();
        
        int tot = listPq.size();

        ProdQty[] tab = new ProdQty[tot];
        
        if(random) Collections.shuffle(listPq);
        else Collections.sort(listPq);

        for(int i=0; i<listPq.size(); i++){
            tab[i] = listPq.get(i);
        }
        return tab;
    }
    
    public static int split(ArrayList<ProdQty> genome){
        int n = genome.size();
        Integer V[] = new Integer[n+1];
        Integer P[] = new Integer[n+1];
        int W = instance.getWeightMaxBox();
        int Vol = instance.getVolumeMaxBox();
        int L = Integer.MAX_VALUE;
        
        Location dep = instance.getGraph().getDepartingDepot();
        Location arr  = instance.getGraph().getArrivalDepot();
        
        V[0] = 0;
        for(int i=1; i<n; i++){
            V[i] = Integer.MAX_VALUE;
        }
        for(int i=1; i<n; i++){
            int loadW = 0, loadV = 0, cost = 0, j = i;
            do {
                loadW += genome.get(i-1).getProduct().getWeight() * genome.get(i-1).getQuantity();
                loadV += genome.get(i-1).getProduct().getVolume() * genome.get(i-1).getQuantity();
                if(i == j){
                    cost = cost + dep.getDistances().get(genome.get(j-1).getProduct().getLoc())
                            + genome.get(j-1).getProduct().getLoc().getDistances().get(arr);
                } else { //TODO: Trier par locations
                    cost = cost - genome.get(j-2).getProduct().getLoc().getDistances().get(arr)
                            + genome.get(j-2).getProduct().getLoc().getDistances().get(genome.get(j-1).getProduct().getLoc())
                            + genome.get(j-1).getProduct().getLoc().getDistances().get(arr);
                }
                if(loadW<=W && loadV<=Vol && cost<=L){
                    if(V[i-1] + cost < V[j]){
                        V[j] = V[i-1]+cost;
                        P[j] = i-1;
                    }
                    j = j+1;
                }
            } while(j>n || loadW>W || loadV>Vol ||cost>L);
        }
        /*for(int i=1; i<n; i++){
            System.out.println("V:" + V[i] + " P:" + P[i]);
        }*/
        return V[n-1];
    }
    
    /**
     * Fonctions d'affichages
     */
    
    public static void printPopList(String etape){
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
        System.out.println(etape + " : ");
        int i = 1;
        for(ArrayList<Integer> list : popList) {
            System.out.printf("Liste produits " + i + ": ");
            for(Integer nb : list)
                System.out.printf(nb+ "("+data[nb].getProduct().getIdProduct() + "x"+ data[nb].getQuantity() + "), ");
            System.out.println("");
            i++;
        }
    }
    
    public static void printResults(String etape){
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
        System.out.printf(etape + " : ");
        for (Map.Entry<Integer, Integer> e : results.entrySet()){
            System.out.printf(e.getKey() + "=" + e.getValue() + ", ");
        }
        System.out.println("");
    }
}