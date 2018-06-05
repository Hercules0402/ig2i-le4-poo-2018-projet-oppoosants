package algogen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import metier.Box;
import metier.Instance;
import metier.Location;
import metier.Order;
import metier.ProdQty;
import util.Reader;

public class GAColis {
    private final static int CB_CYCLES = 2;
    private final static boolean DEBUG = false;
    
    private static Instance instance;
    private static Order order;
    
    private static ProdQty[] data;
    private static List<ArrayList<Integer>> popList;
    private static Map<Integer, Integer> results;
    
    public static List<Box> run(Order o, Instance inst){
        GAColis.order = o;
        GAColis.instance = inst;

        genereration();
        printPopList("Génération population initiale");
        
        for(int i=1; i<=CB_CYCLES; i++){
            printCycle(i, CB_CYCLES);
            
            evaluation();
            printResults("Evaluation");

            selection();
            printPopList("Selection");

            crossover();
            printPopList("Cross-over");

            mutation();
            printPopList("Mutation");
        }
        
        return null;
    }
    
    public static void genereration(){
        data = basicsmart(false);
        popList = new ArrayList<ArrayList<Integer>>();
        
        ArrayList<Integer> list = new ArrayList<>();
        for(int j=0; j<data.length; j++) list.add(j);
        popList.add(list);
                
        for(int i=2; i<11; i++){ //On génére les 9 suivants
            ArrayList<Integer> nList = new ArrayList<>(list);  
            //On interverti 2par2, puis 3par3, puis NparN
            for(int k=0; k<i; k++)
                for(int j=0; j<nList.size()-(3*i); j=j+3*i)
                    Collections.swap(nList, i+k+j, 2*i+k+j);

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
        Map.Entry<Integer, Integer> min1 = null;
        Map.Entry<Integer, Integer> min2 = null;

        for(Map.Entry<Integer, Integer> en : results.entrySet()){
            if (min1 == null || en.getValue().compareTo(min1.getValue()) < 0){
                min1 = en;
            }                   
        }
        for(Map.Entry<Integer, Integer> en : results.entrySet()){
            if (en != min1 && (min2 == null || (en.getValue().compareTo(min2.getValue()) < 0))) {
                min2 = en;
            }                  
        }
 
        List<ArrayList<Integer>> newPopList = new ArrayList<ArrayList<Integer>>();
        newPopList.add(popList.get(min1.getKey()));
        newPopList.add(popList.get(min2.getKey()));
        popList = new ArrayList<ArrayList<Integer>>(newPopList);
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
    
    public static void mutation(){
        //TODO: Local search as mutation operator
        for(int i=2; i<10; i++){
            ArrayList<Integer> list = popList.get(i);
            for(int j=0; j<5; j++){
                int pos1 = new Random().nextInt(list.size());
                int pos2 = new Random().nextInt(list.size());
                while(pos1 == pos2)
                    pos2 = new Random().nextInt(list.size());
                //System.out.println("[DEBUG] Liste " + (i+1) + ": Permutation de l'elmt " + pos1 + "(" + list.get(pos1) + ")" + " et de l'elmt" + pos2+ "(" + list.get(pos2) + ")");
                Collections.swap(list, pos1, pos2);
            }
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
            while(j<n && loadW<W && loadV<Vol && cost<L) {
                loadW += genome.get(i-1).getProduct().getWeight() * genome.get(i-1).getQuantity();
                loadV += genome.get(i-1).getProduct().getVolume() * genome.get(i-1).getQuantity();
                if(i == j){
                    cost += dep.getDistances().get(genome.get(j-1).getProduct().getLoc())
                            + genome.get(j-1).getProduct().getLoc().getDistances().get(arr);
                } else { //TODO: Trier par locations
                    if(!genome.get(j-2).getProduct().getLoc().equals(genome.get(j-1).getProduct().getLoc())) {
                    //System.out.println("j-2 distances" + genome.get(j-2).getProduct().getLoc());
                    //System.out.println("j-1" + genome.get(j-1).getProduct().getLoc());
                    //boolean dedans = genome.get(j-2).getProduct().getLoc().getDistances().containsKey(genome.get(j-1).getProduct().getLoc());
                    //if(!dedans) System.out.println("PAS DEDANS");
                        cost -= genome.get(j-2).getProduct().getLoc().getDistances().get(arr);
                        if(genome.get(j-2).getProduct().getLoc().getDistances().containsKey(genome.get(j-1).getProduct().getLoc()))
                            cost += genome.get(j-2).getProduct().getLoc().getDistances().get(genome.get(j-1).getProduct().getLoc());
                        else
                            cost += genome.get(j-1).getProduct().getLoc().getDistances().get(genome.get(j-2).getProduct().getLoc());
                        cost += genome.get(j-1).getProduct().getLoc().getDistances().get(arr);
                    } else {
                        
                    }
                }
                if(loadW<=W && loadV<=Vol && cost<=L){
                    if(V[i-1] + cost < V[j]){
                        V[j] = V[i-1]+cost;
                        P[j] = i-1;
                    }
                    j = j+1;
                }
                //System.out.println(loadW + "<" + W + " et " + loadV + "<" + Vol);
            }
        }
        /*for(int i=1; i<n; i++){
            System.out.println("V:" + V[i] + " P:" + P[i]);
        }*/
        List<Box> boxes = extractSolution(P, genome);
        //System.out.println(boxes);
        return V[n-1];
    }
    
    public static List<Box> extractSolution(Integer[] P, ArrayList<ProdQty> genome){
        int n = P.length-1;
        
        int i;
        ArrayList<Box> boxes = new ArrayList<>();
        for(i=1; i<=n; i++){
            Box box = new Box(i, instance.getWeightMaxBox(), instance.getVolumeMaxBox(), order, 0, 0, instance);
            boxes.add(box);
        }

        int t = 0;
        int j = n-1;
        do {
            t++;
            i = P[j];
            for(int k=i+1; k<j; k++) {
                boxes.get(t).addProduct(genome.get(k));
            }
            j = i;
        } while(i!=0);
        
        ArrayList<Box> notEmptyBoxes = new ArrayList<>();
        for(Box b : boxes)
            if(!b.getProdQtys().isEmpty())
                notEmptyBoxes.add(b);
        
        //System.out.println(notEmptyBoxes);
        //System.out.println(notEmptyBoxes.size());
        for(int k=1; k<P.length-1; k++){
            System.out.printf(P[k] + ", ");
        }
        System.out.println("");
        for(Box b: notEmptyBoxes){
            for(ProdQty pq: b.getProdQtys())
                System.out.printf(pq.getProduct().getLoc().getIdLocation()+", ");
            System.out.printf(" | ");
        }    
        System.out.println("");
        return notEmptyBoxes;
    }
    
    /**
     * Fonctions d'affichages
     */
    
    public static void printPopList(String etape){
        if(DEBUG != true) return;
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
        if(DEBUG != true) return;
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
        System.out.printf(etape + " : ");
        for (Map.Entry<Integer, Integer> e : results.entrySet()){
            System.out.printf(e.getKey()+1 + "=" + e.getValue() + ", ");
        }
        System.out.println("");
    }
    
    public static void printCycle(int i, int t){
        System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
        System.out.println("                     Cycle " + i + "/" + t);
    }
    
    /**
     * Main test
     */
    
    public static void main(String[] args) {
        String fileName = "instance_40000.txt";
        Instance inst = Reader.read(fileName, false); 
        GAColis.run(inst.getOrders().get(0), inst);
    }
}