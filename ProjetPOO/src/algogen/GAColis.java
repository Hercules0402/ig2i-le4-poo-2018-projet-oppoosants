package algogen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import metier.Box;
import metier.Instance;
import metier.Order;
import metier.ProdQty;
import metier.Product;
import util.Distances;


public class GAColis {
    private static Order order;
    
    private static Instance instance;
    private static List<Product> products;
    private static int nbColisMax;
    private static int weightMaxBox;
    private static int volumeMaxBox;
    
    private static List<List<Box>> popList;
    private static Map<Integer, Integer> results;
    private static int tempBest;
    
    public static List<List<Box>> runTest(Order o, Instance inst){
        GAColis.run(o, inst);
        return popList;
        
    }
    public static List<Box> run(Order o, Instance inst){
        GAColis.order = o;
        
        instance = inst;
        products = new ArrayList<>(inst.getProducts());
        nbColisMax = inst.getNbBoxesTrolley();
        weightMaxBox = inst.getWeightMaxBox();
        volumeMaxBox = inst.getVolumeMaxBox();
    
        genereration();
        System.out.println("- - - - - - - - - - - - - - - -");
        System.out.println("Population initiale: ");
        int i = 1;
        for(List<Box> l : popList) {
            System.out.println("Liste de colis numéro " + i + ":");
            int j = 1;
            for(Box c : l) {
                System.out.printf("Colis numéro " + j + ": ");
                for(ProdQty pq : c.getProdQtys()) {
                    System.out.printf(pq.getProduct().getLoc().getIdLocation() + ", ");
                }
                System.out.println("");
                j++;
            }
            System.out.println("");
            i++;
        }
        System.out.println("- - - - - - - - - - - - - - - -");
        
        evaluation();
        System.out.println("Evaluation: " + results);
        System.out.println("- - - - - - - - - - - - - - - -");
        
        selection();
        System.out.println("Selection: ");
        i = 1;
        for(List<Box> l : popList) {
            System.out.println("Liste de colis numéro " + i + ":");
            int j = 1;
            for(Box c : l) {
                System.out.printf("Colis numéro " + j + ": ");
                for(ProdQty pq : c.getProdQtys()) {
                    System.out.printf(pq.getProduct().getLoc().getIdLocation() + ", ");
                }
                System.out.println("");
                j++;
            }
            System.out.println("");
            i++;
        }
        System.out.println("- - - - - - - - - - - - - - - -");
        return null;
    }
    
    public static void genereration(){
        popList = new ArrayList<List<Box>>();
        for(int i=0; i<10; i++){
            if(i==0) {
                //On génere un genome via algo basique smart
                List<Box> smartGenome = basicsmart(false);
                popList.add(smartGenome);
            } else {
                //On en génere 9 full random
                List<Box> randomGenome = basicsmart(true);
                popList.add(randomGenome);
            }
        }
    }
    
    public static void evaluation(){
        results = new HashMap<>();
        int i = 0;
        for(List<Box> genome : popList){
            int r = Distances.calcDistanceBox(genome, instance.getGraph().getArrivalDepot(), instance.getGraph().getDepartingDepot());
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

        List<List<Box>> newPopList = new ArrayList<List<Box>>();
        newPopList.add(popList.get(max1.getKey()));
        newPopList.add(popList.get(max2.getKey()));
        popList = new ArrayList<List<Box>>(newPopList);
        
        tempBest = max1.getValue();
    }
    
    public static List<Box> basicsmart(boolean random){
        ArrayList<Box> colis = new ArrayList();
        int idBox = 1, qt = 0;
        Product p = null;
        
        Box box = new Box(idBox, weightMaxBox, volumeMaxBox, order, 0, 0, instance);

        List<ProdQty> listPq = order.getProdQtys();
        
        if(random) Collections.shuffle(listPq);
        else Collections.sort(listPq);
        
        for(ProdQty pq : listPq) {
            p = pq.getProduct();
            qt = pq.getQuantity();

            if (box.getVolume() + (p.getVolume() * qt) < volumeMaxBox &&
                    box.getWeight() + (p.getWeight() * qt) < weightMaxBox) {
                box.addProduct(p, qt);
            } else { 
                colis.add(box);
                idBox++;
                box = new Box(idBox, weightMaxBox, volumeMaxBox, order, 0, 0, instance);
                box.addProduct(p, qt);
            }

        }
        colis.add(box);
        return colis;
    }

}