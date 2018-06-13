package algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import metier.Location;
import metier.Trolley;
import metier.Box;
import metier.Instance;
import metier.Order;
import metier.ProdQty;
import metier.Product;
import util.Distances;
import util.Reader;
import util.Writer;

/**
 * Classe ClarkeAndWright.
 * Elle implémente cet algo.
 */
public class ClarkeAndWrightSpecial {

    /**
     * Classe interne permettant de stocker l'ensemble de paire de tournées avec 
     * le gain de celles-ci
     */
    class Economie{
        Box b1, b2;
        double gain;

        public Economie(Box b1, Box b2){
            this.b1 = b1;
            this.b2 = b2;
        }

        @Override
        public String toString() {
            return b1.getIdBox()+ " ||| " + b2.getIdBox()+ " ||| " + gain;
        }
    }

    // Attributs

    private Location depot;
    private Set<Box> boxes;
    private Set<Order> orders;
    private List<Economie> economies;
    private Instance instance;

    // Constructeurs

    public ClarkeAndWrightSpecial(){
        this.depot = new Location();
        this.boxes = new HashSet<>();
        this.orders = new HashSet<>();
        this.economies = new ArrayList<>();
    }

    public ClarkeAndWrightSpecial(Instance instance) {
        this();
        this.depot = instance.getGraph().getDepartingDepot();
        this.orders = new HashSet<>(instance.getOrders());
        this.instance = instance;
    }

    // Méthodes

    /**
     * Permet de déterminer le coût d'une fusion entre deux boxes.
     * @param b1
     * @param b2
     * @return double
     */
    private double coutFusion(Box b1, Box b2){
        Location locBox1 = b1.getProdQtys().get(b1.getProdQtys().size() - 1).getProduct().getLoc();
        Location locBox2 = b2.getProdQtys().get(0).getProduct().getLoc();
  
        double coutRetour = this.depot.getDistances().get(locBox1);
   
        double coutAller = this.depot.getDistances().get(locBox2);
  
        double coutFusion = 0;
        if (locBox1.getDistances().get(locBox2) == null) {
            coutFusion = 0;
        }
        else {
            coutFusion = locBox1.getDistances().get(locBox2);
        }

        return coutRetour + coutAller - coutFusion;
    }

    /**
     * Permet de créer une box pour chaque ProductQty, calculer le gain de 
     * celle-ci, d'ajouter la paire de tournées dans notre tableau d'économie et
     * trié dans l'ordre défini le gain notre tableau d'économie.
     */
    private void initialiserCWA(){
        int count = 1;
        // Création d'une boxe pour chaque chaque ProdQty
        for(Order o : this.orders){
            for(ProdQty pq : o.getProdQtys()) {                
                Box b = new Box(count++, instance.getWeightMaxBox(), instance.getVolumeMaxBox(), o, 0, 0, instance);
                b.addProductClarkeAndWright(pq);
                this.boxes.add(b);
            }
        }
        // Création d'un objet Economie permettant d'associer le coût d'une fusion
        // entre deux boxes
        for(Box b1:this.boxes){
            for(Box b2:this.boxes){
                if(b1 == b2) continue;
                
                Economie eco =  new Economie(b1, b2);
                eco.gain = this.coutFusion(b1, b2);
                this.economies.add(eco);
            }
        }
        // Tri décroissant des ensembles de couples de trolleys par coût de fusion
        Collections.sort(this.economies,new Comparator<Economie>(){
            @Override
            public int compare(Economie o1, Economie o2) {
                if(o1.gain == o2.gain) return 0;
                else return o1.gain > o2.gain ? -1 : 1;
            }
        });
    }

    /**
     * Permet de fusionner les différentes boxes tel que le gain de la fusion
     * soit le plus grand possible.
     */
    private void fusion(){
        this.boxes.clear();
        for (Economie e : this.economies){
            Box b1 = e.b1;
            Box b2 = e.b2;
            // Si les boxes sont null alors on passe au couple suivant.
            if (b1.getProdQtys() == null || b2.getProdQtys() == null) continue;
            double gain = this.coutFusion(b1, b2);
            if (gain <= 0) continue;
            // Si l'addition des capacités de stockage des prodQtys des boxes est
            // supérieure à la le volume maximale et la hauteur maximale des prodQtys 
            //pour une boxe alors la fusion est impossible et on passe au couple suivant
            if(b1.getVolume() + b2.getVolume() > this.instance.getVolumeMaxBox() 
                    || b1.getWeight() + b2.getWeight() > this.instance.getWeightMaxBox()){
                this.boxes.add(b1);
                this.boxes.add(b2);
                continue;
            }
            // Fusion des prodQtys des deux boxes et fusion des boxes
            List<ProdQty> ensProdQtys = new ArrayList<>();
            ensProdQtys.addAll(b2.getProdQtys());
            b1.addProducts(ensProdQtys);
            ensProdQtys.clear();           
            
            // Suppression de b2 après la fusion avec b1
            b2.setProdQtys(null);
            // AJout de b1 à l'ensemble des boxes fusionnées
            this.boxes.add(b1);
        }
        this.nettoyerBox();
        this.cleanIdBox();
    }

    /**
     * Permet de supprimer toutes les boxes nulles de notre liste de boxes.
     */
    private void nettoyerBox(){
        Iterator<Box> it = this.boxes.iterator();
        while(it.hasNext()){
           if (it.next().getProdQtys() == null) it.remove();
        }
    }

    /**
     * Permet de réarranger les ids des boxes.
     */
    private void cleanIdBox(){
        int count = 1;
        for(Box b : this.boxes) {
            b.setIdBox(count++);
            //this.rangeBoxes(t);
        }
    }

    /**
     * Permet de trier par ordre croissant les boxes d'un trolley
     * @param t 
     */
    /*private void rangeBoxes(Trolley t) {
        Collections.sort(t.getBoxes(),new Comparator<Box>(){
            @Override
            public int compare(Box o1, Box o2) {
                if(o1.getIdBox() == o2.getIdBox()) return 0;
                return o1.getIdBox() < o2.getIdBox() ? -1 : 1;
            }
        });
    }*/

    /**
     * Permet de récupérer l'instance.
     * @return Instance
     */
    public Instance getNewInstance() {
        this.instance.setBoxes(new ArrayList<>(boxes));
        return this.instance;
    }

    /**
     * Permet de lancer l'algo.
     */
    public void run() {
        //System.out.println("1");
        this.initialiserCWA();
        //System.out.println("2");
        this.fusion();
        //System.out.println("3");
    }

    public static void main(String[] args) {
        String fileName = "instance_0606_136178_Z1.txt";//instance_0116_131940_Z2

        Instance inst = Reader.read(fileName, false);
        
        inst = Recherche.run(inst);
        for(Trolley t : inst.getTrolleys()){
            inst.getBoxes().addAll(t.getBoxes());
        }
        System.out.println("Nb init boxes : " + inst.getBoxes().size());
        //int distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        //System.out.println(Distances.formatDistance(distance));
        
        /*for(Trolley t : inst.getTrolleys()){
            inst.getBoxes().addAll(t.getBoxes());
        }
        
        ClarkeAndWright cwa = new ClarkeAndWright(inst);
        cwa.run();
        inst = cwa.getNewInstance();*/
        
        ClarkeAndWrightSpecial cwas = new ClarkeAndWrightSpecial(inst);
        cwas.run();
        inst = cwas.getNewInstance();
        System.out.println("Nb init boxes : " + inst.getBoxes().size());
        
        
        //distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        //System.out.println(Distances.formatDistance(distance));
    }
}
