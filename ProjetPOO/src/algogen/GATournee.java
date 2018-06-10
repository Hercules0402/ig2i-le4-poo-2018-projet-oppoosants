package algogen;

import algo.Recherche;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import metier.Box;
import metier.Instance;
import metier.Location;
import metier.Trolley;
import util.Distances;
import util.Reader;
import util.Writer;

public class GATournee {
    private static int CB_CYCLES = 5000;
    private final static int LOG_LEVEL = 0; //0: None, 1: Cycles, 2: All
    
    private static Instance instance;
    private static Location dep;
    private static Location arr;
    
    private static List<ArrayList<Trolley>> popList;
    private static Map<Integer, Integer> results;
    
    private static List<Box> boxes;
    
    private static ArrayList<Trolley> tempBest;
    
    public static ArrayList<Trolley> runx(Instance inst, int x){
        CB_CYCLES = x;
        return run(inst);
    }

    public static ArrayList<Trolley> run(Instance inst){
        instance = inst;
        dep = inst.getGraph().getDepartingDepot();
        arr = inst.getGraph().getArrivalDepot();
        
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
            
            printBest();
        }
        
        return tempBest;
    }
    
    public static void genereration(){
        popList = new ArrayList<ArrayList<Trolley>>();
        
        ArrayList<Trolley> trolleys = new ArrayList<>(instance.getTrolleys());
        popList.add(trolleys);
        
        boxes = new ArrayList<Box>();
        for(Trolley t : trolleys)
            for(Box b : t.getBoxes())
                boxes.add(b);
        
        for(int i=0; i<9; i++){ //On génére les 9 suivants
            ArrayList<Trolley> nTrolleys = new ArrayList<>();
            int tot = boxes.size();
            for(Trolley t : trolleys){
                Trolley dd = new Trolley(t.getIdTrolley(),t.getNbColisMax(),instance);
                dd.addBoxes(t.getBoxes());
                nTrolleys.add(dd);
            }        
            
            for(int j=0; j<4; j++){ 
                int p1 = new Random().nextInt(tot - 1) + 1;
                int p2 = new Random().nextInt(tot - 1) + 1;
                while(p1 == p2)
                    p2 = new Random().nextInt(tot - 1) + 1;
                nTrolleys = swapBoxes(nTrolleys, p1, p2);
            }
           
            for(int j=0; j<5; j++){ 
                int val = new Random().nextInt(nTrolleys.size());
                if(nTrolleys.get(val).getBoxes().size() == nTrolleys.get(val).getNbColisMax()){
                    int rand  = new Random().nextInt(nTrolleys.get(val).getBoxes().size());
                    int p1 = nTrolleys.get(val).getBoxes().get(rand).getIdBox();
                    int p2 = new Random().nextInt(tot) + 1;
                    while(p1 == p2)
                        p2 = new Random().nextInt(tot) + 1;
                    nTrolleys = moveBox(nTrolleys, p1, p2);
                }
            }

            popList.add(nTrolleys);
        }
    }
    
    public static void evaluation(){
        results = new HashMap<>();
        int i = 0;
        for(ArrayList<Trolley> genome : popList){
            int r = Distances.calcDistance(genome, dep, arr);
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
 
        List<ArrayList<Trolley>> newPopList = new ArrayList<ArrayList<Trolley>>();
        newPopList.add(popList.get(min1.getKey()));
        newPopList.add(popList.get(min2.getKey()));
        tempBest = popList.get(min1.getKey());
        popList = new ArrayList<ArrayList<Trolley>>(newPopList);
    }
    
    public static void crossover(){
        ArrayList<Trolley> p1 = popList.get(0);
        ArrayList<Trolley> p2 = popList.get(1);
        
        List<Integer> l1 = new ArrayList();
        int rab = -1;
        for(Trolley t : p1){
            for(Box b : t.getBoxes())
                l1.add(b.getIdBox());
            for(int i=1; i<(t.getNbColisMax()-t.getBoxes().size()+1); i++) {
                l1.add(rab);
                rab--;
            }
        }
        
        rab = -1;
        List<Integer> l2 = new ArrayList();
        for(Trolley t : p2){
            for(Box b : t.getBoxes())
                l2.add(b.getIdBox());
            for(int i=1; i<(t.getNbColisMax()-t.getBoxes().size()+1); i++) {
                l2.add(rab);
                rab--;
            }
        }
        
        for(int i=0; i<4; i++) {
            Map<Integer, ArrayList<Integer>> cs = OX.crossover(l1, l2);
            
            ArrayList<Integer> nl1  = cs.get(0);
            ArrayList<Integer> nl2  = cs.get(1);
            
            int id1 = 1, id2 = 1;
            ArrayList<Trolley> c1 = new ArrayList();
            ArrayList<Trolley> c2 = new ArrayList();
            Trolley trolley1 = new Trolley(id1, instance.getNbBoxesTrolley(), instance);
            Trolley trolley2 = new Trolley(id2, instance.getNbBoxesTrolley(), instance);
            
            int place1 = 0, place2 = 0;
            for(int j=0; j<nl1.size(); j++){
                if(place1 >= instance.getNbBoxesTrolley()) {
                    id1++;
                    c1.add(trolley1);
                    trolley1 = new Trolley(id1, instance.getNbBoxesTrolley(), instance);
                    place1 = 0;
                }
                if(nl1.get(j)-1 >= 0)
                    trolley1.addBox(boxes.get(nl1.get(j)-1));
                place1++;
                
                if(place2 >= instance.getNbBoxesTrolley()) {
                    id2++;
                    c2.add(trolley2);
                    trolley2 = new Trolley(id2, instance.getNbBoxesTrolley(), instance);
                    place2 = 0;
                }
                if(nl2.get(j)-1 >= 0)
                    trolley2.addBox(boxes.get(nl2.get(j)-1));
                 place2++;
            }
            c1.add(trolley1);
            c2.add(trolley2);
            
            popList.add(c1);
            popList.add(c2);
        }
    }
    
    public static void mutation(){
        int tot = boxes.size();
        for(int i=2; i<10; i++){
            for(int j=0; j<2; j++){
                int p1 = new Random().nextInt(tot - 1) + 1;
                int p2 = new Random().nextInt(tot - 1) + 1;
                while(p1 == p2)
                    p2 = new Random().nextInt(tot - 1) + 1;
                swapBoxes(popList.get(i), p1, p2);
            }
            
            for(int j=0; j<3; j++){ 
                int val = new Random().nextInt(popList.get(i).size());
                if(popList.get(i).get(val).getBoxes().size() == popList.get(i).get(val).getNbColisMax()){
                    int rand  = new Random().nextInt(popList.get(i).get(val).getBoxes().size());
                    int p1 = popList.get(i).get(val).getBoxes().get(rand).getIdBox();
                    int p2 = new Random().nextInt(tot) + 1;
                    while(p1 == p2)
                        p2 = new Random().nextInt(tot) + 1;
                    moveBox(popList.get(i), p1, p2);
                }
            }
        }
    }
    
    /**
     * Fonction swapBoxes (pour generation et mutation)
     */
    
    public static ArrayList<Trolley> swapBoxes(ArrayList<Trolley> sol, int idBox1, int idBox2){
        Trolley t1 = null;
        Box b1 = null;  
        Trolley t2 = null;
        Box b2 = null;
        
        int i1 = 0;
        int i2 = 0;
        
        for(Trolley t : sol){
            for(Box b : t.getBoxes()){
                if(b.getIdBox() == idBox1) {
                    b1 = b;
                    t1 = t;
                }
                if(b.getIdBox() == idBox2) {
                    b2 = b;
                    t2 = t;
                }
            }
        }
        
        i1 = t1.getBoxes().indexOf(b1);
        i2 = t2.getBoxes().indexOf(b2);
        t1.getBoxes().remove(b1);
        t2.getBoxes().remove(b2);

        if(i1 > t1.getBoxes().size()) i1--;
        t1.getBoxes().add(i1, b2);
        t2.getBoxes().add(i2, b1);
        
        return sol;
    }
    
    public static ArrayList<Trolley> moveBox(ArrayList<Trolley> sol, int idBox1, int idBox2){
        Trolley t1 = null;
        Box b1 = null;  
        Trolley t2 = null;
        Box b2 = null;
        
        int pos = 0;
        
        for(Trolley t : sol){
            for(Box b : t.getBoxes()){
                if(b.getIdBox() == idBox1) {
                    b1 = b;
                    t1 = t;
                }
                if(b.getIdBox() == idBox2) {
                    b2 = b;
                    t2 = t;
                }
            }
        }

        if(t2.getBoxes().size() == t2.getNbColisMax()) {
            //System.out.println("[ERREUR] Trolley complet");
            return sol;
        } else {
            //System.out.println("go move " + idBox1 + " a la pos " + (idBox2+1));
        }
        
        pos = t2.getBoxes().indexOf(b2)+1;
        
        t1.getBoxes().remove(b1);

        t2.getBoxes().add(pos, b1);
        
        return sol;
    }
    
    /**
     * Fonctions d'affichages
     */

    public static void printPopList(String etape){
        if(LOG_LEVEL < 2) return;
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
        System.out.println(etape + " : ");
        int i = 1, j =1;
        for(ArrayList<Trolley> list : popList) {
            System.out.printf("Solution num " + i + ": ");
            for(Trolley t : list) {
                System.out.printf("Trolley " + j + " (");
                for(Box b : t.getBoxes())
                   System.out.printf(b.getIdBox() + ", ");
                System.out.printf(") ");
                j++;
            }
            i++;
            j = 1;
            System.out.println("");
        }
    }
    
    public static void printResults(String etape){
        if(LOG_LEVEL < 2) return;
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
        System.out.printf(etape + " : ");
        for (Map.Entry<Integer, Integer> e : results.entrySet()){
            System.out.printf(e.getKey()+1 + "=" + e.getValue() + ", ");
        }
        System.out.println("");
    }
    
    public static void printBest(){
        if(LOG_LEVEL < 1) return;
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
        System.out.println("Meilleur actuel : ");
        int r = Distances.calcDistance(tempBest, dep, arr);
        String rs = Distances.formatDistance(r);
        System.out.println(r + " (" + rs + ") : " + tempBest);
    }
    
    public static void printCycle(int i, int t){
        if(LOG_LEVEL < 1) return;
        System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
        System.out.println("                     Cycle " + i + "/" + t);
    }
    
    /**
     * Main test
     */
    
    public static void main(String[] args) {
        String fileName = "instance_40000.txt";
        Instance inst = Reader.read(fileName, false); 
        inst = Recherche.run(inst);
        
        ArrayList<Trolley> tournees = GATournee.run(inst);
        inst.setTrolleys(tournees);
          
        /*Writer*/
        Writer.save(fileName, inst, false);

        /*Checker*/
        String[] name = {""};
        name[0] = fileName.substring(0, fileName.lastIndexOf("."));
        System.out.println("\n\nChecker de l'instance : "+ fileName);
        checker.Checker.main(name);
    }
}