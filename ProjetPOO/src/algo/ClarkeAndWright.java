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

/**
 * Classe ClarkeAndWright.
 * Elle implémente cet algo en travaillant sur les trolleys et les boxes.
 */
public class ClarkeAndWright {

    /**
     * Classe interne permettant de stocker l'ensemble de paire de trolleys avec 
     * le gain de celles-ci.
     */
    class Economie{
        Trolley t1, t2;
        double gain;

        public Economie(Trolley t1, Trolley t2){
            this.t1 = t1;
            this.t2 = t2;
        }

        @Override
        public String toString() {
            return t1.getIdTrolley()+ " ||| " + t2.getIdTrolley()+ " ||| " + gain;
        }
    }

    // Attributs

    private Location depot;
    private Set<Trolley> trolleys;
    private Set<Box> boxes;
    private List<Economie> economies;
    private Instance instance;

    // Constructeurs

    public ClarkeAndWright(){
        this.depot = new Location();
        this.trolleys = new HashSet<>();
        this.boxes = new HashSet<>();
        this.economies = new ArrayList<>();
    }

    public ClarkeAndWright(Instance instance) {
        this();
        this.depot = instance.getGraph().getDepartingDepot();
        this.boxes = new HashSet<>(instance.getBoxes());
        this.instance = instance;
    }

    // Méthodes

    /**
     * Permet de déterminer le coût d'une fusion entre deux trolleys.
     * @param t1
     * @param t2
     * @return double
     */
    private double coutFusion(Trolley t1, Trolley t2){
        Box b1 = t1.getBoxes().get(t1.getBoxes().size() - 1);
        Box b2 = t2.getBoxes().get(0);

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
     * Permet de créer un trolley pour chaque box, calculer le gain de 
     * celle-ci, d'ajouter la paire de trolleys dans notre tableau d'économie et
     * trié dans l'ordre défini le gain notre tableau d'économie.
     */
    private void initialiserCWA(){
        int count = 1;
        // Création d'un trolley pour chaque box
        for(Box b:this.boxes){
            Trolley t = new Trolley(count++,this.instance.getNbBoxesTrolley(),this.instance);
            t.addBoxClarkeAndWright(b);
            this.trolleys.add(t);
        }

        // Création d'un objet Economie permettant d'associer le coût d'une fusion
        // entre deux trolleys
        for(Trolley t1:this.trolleys){
            for(Trolley t2:this.trolleys){
                if(t1 == t2) continue;
                
                Economie eco =  new Economie(t1, t2);
                eco.gain = this.coutFusion(t1, t2);
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
     * Permet de fusionner les différentes trolleys tel que le gain de la fusion
     * soit le plus grand possible.
     */
    private void fusion(){
        this.trolleys.clear();
        for (Economie e : this.economies){
            Trolley t1 = e.t1;
            Trolley t2 = e.t2;
            // Si les trolleys sont null alors on passe au couple suivant.
            if (t1.getBoxes() == null || t2.getBoxes() == null) continue;
            double gain = this.coutFusion(t1, t2);
            if (gain <= 0) continue;
            // Si l'addition des capacités de stockage des boxes des trolleys est
            // supérieure à la capacité maximale de stockage des boxes pour un
            // trolley alors la fusion est impossible et on passe au couple suivant
            if (t1.getBoxes().size() + t2.getBoxes().size() > this.instance.getNbBoxesTrolley()){
                this.trolleys.add(t1);
                this.trolleys.add(t2);
                continue;
            }
            // Fusion des boxes des deux trolleys et fusion des trolleys
            List<Box> ensBoxes = new ArrayList<>();
            ensBoxes.addAll(t2.getBoxes());
            t1.addBoxes(ensBoxes);  
            ensBoxes.clear();
            // Suppression de t2 après la fusion avec t1
            t2.setBoxes(null);
            // AJout de t1 à l'ensemble des trolleys fusionnées
            this.trolleys.add(t1);
        }
        this.nettoyerTrolley();
        this.cleanIdTrolley();
    }

    /**
     * Permet de supprimer toutes les trolleys nulles de notre liste de trolleys.
     */
    private void nettoyerTrolley(){
        Iterator<Trolley> it = this.trolleys.iterator();
        while(it.hasNext()){
           if (it.next().getBoxes() == null) it.remove();
        }
    }

    /**
     * Permet de réarranger les ids des trolleys.
     */
    private void cleanIdTrolley(){
        int count = 1;
        for(Trolley t : this.trolleys) {
            t.setIdTrolley(count++);
            this.rangeBoxes(t);
        }
    }

    /**
     * Permet de trier par ordre croissant les boxes d'un trolley
     * @param t 
     */
    private void rangeBoxes(Trolley t) {
        Collections.sort(t.getBoxes(),new Comparator<Box>(){
            @Override
            public int compare(Box o1, Box o2) {
                if(o1.getIdBox() == o2.getIdBox()) return 0;
                return o1.getIdBox() < o2.getIdBox() ? -1 : 1;
            }
        });
    }

    /**
     * Permet de récupérer l'instance.
     * @return Instance : instance modifiée
     */
    private Instance getNewInstance() {
        this.instance.setTrolleys(new ArrayList<>(trolleys));
        return this.instance;
    }

    /**
     * Permet de lancer l'algo.
     * @return Instance : instance modifiée
     */
    public Instance run() {
        this.initialiserCWA();
        this.fusion();
        List<Box> boxes = new ArrayList<>();
        for(Trolley t : trolleys) boxes.addAll(t.getBoxes());
        this.instance.setBoxes(boxes);
        return this.getNewInstance();
    }    
}
