/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import metier.Location;
import metier.Trolley;
import metier.Box;
import metier.Instance;
import util.Distances;
import util.Reader;
import util.Writer;

/**
 * Classe ClarkeAndWright
 * Elle implémente cet algo
 * @author seb
 */
public class ClarkeAndWright {
    
    /**
     * Classe interne permettant de stocker l'ensemble de paire de tournées avec 
     * le gain de celles-ci
     */
    class Economie{
        Trolley t1, t2;
        double gain;
        
        public Economie(Trolley t1, Trolley t2){
            this.t1 = t1;
            this.t2 = t2;
        }
    }
    
    // Attributs
    
    private Location depot;
    private List<Trolley> trolleys;
    private List<Box> boxes;
    private List<Economie> lesEconomies;
    private Instance instance;
    
    // Constructeurs
    
    public ClarkeAndWright(){
        this.depot = new Location();
        this.trolleys = new ArrayList<>();
        this.boxes = new ArrayList<>();
        this.lesEconomies = new ArrayList<>();
    }

    public ClarkeAndWright(Instance instance) {
        this();
        this.depot = instance.getGraph().getDepartingDepot();
        this.boxes = instance.getBoxes();
        this.instance = instance;
    }
    
    // Méthodes
    
    /**
     * Permet de créer les différentes routes (liaisons entre les locs)
     */
    public void initialiserRoutes(){
        /*Set<Point> ensPoint = new HashSet<>();
        for (Box c:this.boxes){
            ensPoint.add(c);
        }
        // appel de la fonction initialiserRoutes() de la classe Point
        this.depot.initialiserRoutes(ensPoint); 
        ensPoint.add(this.depot);
        for (Box c:this.boxes){
            // appel de la fonction initialiserRoutes() de la classe Point
            c.initialiserRoutes(ensPoint);            
        }*/
    }

    private double coutFusion(Trolley t1, Trolley t2){
        Box b1 = t1.getBoxes().get(t1.getBoxes().size() - 1);
        Box b2 = t2.getBoxes().get(0);
                
        Location locBox1 = b1.getProdQtys().get(b1.getProdQtys().size() - 1).getProduct().getLoc();
        Location locBox2 = b2.getProdQtys().get(0).getProduct().getLoc();
              
        double coutRetour = this.depot.getDistances().get(locBox1);
               
        double coutAller = this.depot.getDistances().get(locBox2);
              
        double coutFusion = 0;
        if(locBox1.getDistances().get(locBox2) == null) {
            coutFusion = 0;
        }
        else {
            coutFusion = locBox1.getDistances().get(locBox2);
        }
        
        return coutRetour + coutAller - coutFusion;
    }

    /**
     * Permet de créer une tournée pour chaque client, calculer le gain de 
     * celle-ci, d'ajouter la paire de tournées dans notre tableau d'économie et
     * trié dans l'ordre défini le gain notre tableau d'économie
     */
    public void initialiserCWA(){
        for(Box b:this.boxes){
            Trolley t = new Trolley();
            t.setNbColisMax(this.instance.getNbBoxesTrolley());
            t.addBox2(b);
            this.trolleys.add(t);
        }
        
        for(Trolley t1:this.trolleys){
            for(Trolley t2:this.trolleys){
                if(t1 == t2) continue;
                
                Economie eco =  new Economie(t1, t2);
                eco.gain = coutFusion(t1, t2);
                this.lesEconomies.add(eco);
            }
        }
        
        Collections.sort(this.lesEconomies,new Comparator<Economie>(){
            @Override
            public int compare(Economie o1, Economie o2) {
                return o1.gain > o2.gain ? -1 : 1;
            }            
        });
    }
    
    /**
     * Permet de fusionner les différentes tournées tel que le gain de la fusion
     * soit le plus grand possible
     */
    public  void fusion(){
        this.trolleys.clear();
        for(Economie e:this.lesEconomies){
            Trolley t1 = e.t1;
            Trolley t2 = e.t2;
            if(t1.getBoxes()== null || t2.getBoxes()== null) continue;
            double gain = coutFusion(t1, t2);
            if(gain <= 0) continue;
            if (t1.getBoxes().size() + t2.getBoxes().size() >= this.instance.getNbBoxesTrolley()){
                this.trolleys.add(t1);
                this.trolleys.add(t2);
                continue;
            }
            List<Box> ensBoxes = new ArrayList<>();
            ensBoxes.addAll(t2.getBoxes());
            t1.addBoxes(ensBoxes);  
            ensBoxes.clear();
            t2.setBoxes(null);
            this.trolleys.add(t1);            
        }
        nettoyerTrolley();
    }
    
    /**
     * Permet de supprimer toutes les tournées nulles de notre liste de tournées
     */
    public void nettoyerTrolley(){
        Iterator<Trolley> it = this.trolleys.iterator();
        while(it.hasNext()){
           if (it.next().getBoxes() == null) it.remove();
        }
    }

    public Instance getNewInstance() {
        this.instance.setTrolleys(trolleys);
        return this.instance;
    }
    
    public static void main(String[] args) {
        String fileName = "instance_0116_131940_Z2.txt";

        /*Reader*/
        Instance inst = Reader.read(fileName, false);
        
        Recherche sol = new Recherche(inst.getOrders(), inst.getProducts(), inst.getNbBoxesTrolley(),inst.getWeightMaxBox(), inst.getVolumeMaxBox(),inst);
        inst = sol.lookup();
        int distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));
        Writer.save(fileName, inst, false);
        
        for(Trolley t : inst.getTrolleys()){
            inst.getBoxes().addAll(t.getBoxes());
        }
        
        ClarkeAndWright cwa = new ClarkeAndWright(inst);
        cwa.initialiserRoutes();
        cwa.initialiserCWA();
        cwa.fusion();
        inst = cwa.getNewInstance();
        
        Writer.save("toto.txt", inst, false);
        
        distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
        System.out.println(Distances.formatDistance(distance));
    }
}
