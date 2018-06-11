package algogen;

import algo.Recherche;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import metier.Box;
import metier.Instance;
import metier.Location;
import metier.Trolley;
import util.Distances;
import util.Reader;
import util.Writer;

/**
 * Classe contenant l'algorithme génétique pour les tournées.
 * Permettant d'optimiser tous les colis dans des tournées.
 * Cet algorithme possède une population de 20 genomes, chaque génération 20 nouveaux
 * genomes sont crées à partir des 20 meilleurs, avec un coefficient linéaire ou exponentiel
 * (plus le genome est bon, plus il a de chances de se reproduire)
 * @author Lucas
 */
public class GATourneeV2 {
    private static int CB_CYCLES = 1000; //Nombre de générations
    private final static int LOG_LEVEL = 2; //Niveau de logs: 0: None, 1: Cycles, 2: All
    
    private static Instance instance;
    private static Location dep; 
    private static Location arr;
    
    private static List<ArrayList<Trolley>> popList;
    private static Map<Integer, Integer> results;
    
    private static List<Box> boxes;
    
    private static ArrayList<Trolley> tempBest;
    
    /**
     * Permet d'executer X fois l'algorithme ci-dessous.
     * @param inst Instance
     * @param x int Nombre de fois
     * @return Liste de tournées
     */
    public static ArrayList<Trolley> runx(Instance inst, int x){
        CB_CYCLES = x;
        return run(inst);
    }

    /**
     * Permet d'executer l'algorithme.
     * @param inst Instance
     * @return Liste de tournées contenant tout les colis de la meilleure manière possible.
     */
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
    
    /**
     * Permet de générer la population initiale.
     */
    public static void genereration(){
        popList = new ArrayList<ArrayList<Trolley>>(); //On initialise la liste de population
        
        ArrayList<Trolley> trolleys = new ArrayList<>(instance.getTrolleys());
        popList.add(trolleys); //On y ajoute le 1er genome, la meilleure manière de faire actuelle via Recherche simple
        
        boxes = new ArrayList<Box>(); //On initialise la liste des colis
        for(Trolley t : trolleys) //On y ajoute tout les colis de toutes les tournées
            for(Box b : t.getBoxes())
                boxes.add(b);
        
        for(int i=0; i<39; i++){ //On génére les 39 genomes suivants
            //On crée une nouvelle liste de tournées, auquel on ajoute toutes les tournées 
            //(pas opti mais seul moyen trouvé pour casser complétement les références)
            ArrayList<Trolley> nTrolleys = new ArrayList<>();
            for(Trolley t : trolleys){
                Trolley dd = new Trolley(t.getIdTrolley(),t.getNbColisMax(),instance);
                dd.addBoxes(t.getBoxes());
                nTrolleys.add(dd);
            }       
            
            int tot = boxes.size();
            for(int j=0; j<60; j++){  //On réalise 60 déplacement aléatoires entre deux boites
                int p1 = new Random().nextInt(tot - 1) + 1;
                int p2 = new Random().nextInt(tot - 1) + 1;
                while(p1 == p2) //Si p1 et p2 sont les mêmes, on retire p2 jusqu'a ce qu'ils ne le soient plus
                    p2 = new Random().nextInt(tot - 1) + 1;
                nTrolleys = swapBoxes(nTrolleys, p1, p2);
            }
           
            for(int j=0; j<60; j++){ //On réalise 60 tentatives de déplacement d'une boite aléatoire vers une autre tournée
                int val = new Random().nextInt(nTrolleys.size()); //On sélectionne une tournée au hasard
                if(nTrolleys.get(val).getBoxes().size() == nTrolleys.get(val).getNbColisMax()){ //Si elle est complete
                    int rand  = new Random().nextInt(nTrolleys.get(val).getBoxes().size()); //On sélectionne un colis de cette tournée au hasard
                    int p1 = nTrolleys.get(val).getBoxes().get(rand).getIdBox(); //On récupere l'id du colis séléctionné
                    int p2 = new Random().nextInt(tot) + 1;
                    while(p1 == p2)
                        p2 = new Random().nextInt(tot) + 1;
                    nTrolleys = moveBox(nTrolleys, p1, p2);
                }
            }

            popList.add(nTrolleys); //On ajoute la tournée à la liste de populations
        }
    }
    
    /**
     * Permet d'évaluer les genomes de la population actuelle.
     */
    public static void evaluation(){
        results = new HashMap<>(); //On initialise la Map des résultats
        int i = 0;
        for(ArrayList<Trolley> genome : popList){ //Pour chaque genome de la population
            int r = Distances.calcDistance(genome, dep, arr); //On calcul sont coût/distance
            results.put(i, r); //On met les résultats dans la Map
            i++;
        }
        results = sortByComparator(results);
    }
    
    static class MyComparator implements Comparator<Entry<Integer, Integer>> {
        public int compare(Entry<Integer, Integer> o1, Entry<Integer, Integer> o2) {
            return o1.getValue().compareTo(o2.getValue());
        }
    }
    
    public static Map<Integer, Integer> sortByComparator(Map<Integer, Integer> unsortMap) {
        List<Entry<Integer, Integer>> list = new LinkedList<Entry<Integer, Integer>>(unsortMap.entrySet());

        Collections.sort(list, new MyComparator());

        Map<Integer, Integer> sortedMap = new LinkedHashMap<Integer, Integer>();
        for (Entry<Integer, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
    
    /**
     * Permet de sélectionner les deux meilleurs génomes de la population.
     */
    public static void selection(){
        List<ArrayList<Trolley>> newPopList = new ArrayList<ArrayList<Trolley>>();
        HashMap newResults = new HashMap<Integer,Integer>();
        int i=0;
        for (Map.Entry<Integer,Integer> e : results.entrySet()) {
            if(i>=20) break;
            if(i==0) tempBest = popList.get(e.getKey());
            newPopList.add(popList.get(e.getKey()));
            newResults.put(i, e.getValue());
            i++;
        }
        results = newResults;
        popList = new ArrayList<ArrayList<Trolley>>(newPopList);
    }
    
    public static int getLinearRandomNumber(int maxSize){
        int randomMultiplier = maxSize * (maxSize + 1) / 2;
        Random r=new Random();
        int randomInt = r.nextInt(randomMultiplier);

        int linearRandomNumber = 0;
        for(int i=maxSize; randomInt >= 0; i--){
            randomInt -= i;
            linearRandomNumber++;
        }

        return linearRandomNumber-1;
    }
    
    public static int getExponentialRandomNumber(int maxSize){
        Random r = new Random();
        double d = Math.log(1-r.nextDouble())/(-maxSize);
        int res = (int) Math.round(d*maxSize);
        return res;
    }
    
    /**
     * Permet d'effectuer des croissements entre les 2 meilleurs genomes.
     */
    public static void crossover(){
        //Pour creer 8 nouveaux genomes, on fait appel 4 fois à la fonction CrossOver
        //qui va retourner 2 mélanges des 2 génomes passés en paramètres, qu'on ajoute à la liste de population
        for(int i=0; i<10; i++) {
            int r1 = getExponentialRandomNumber(20);
            int r2 = getExponentialRandomNumber(20);
            while(r1 == r2) 
                r2 = getExponentialRandomNumber(20);
            //System.out.println("Crossover entre la " + r1 + " et la " + r2);

            ArrayList<Trolley> p1 = popList.get(r1); //On récupere le premier genome (meilleur)
            ArrayList<Trolley> p2 = popList.get(r2); //On récupere le deuxieme genome (2nd meilleur)
            
            //On va convertir notre liste de colis en une liste d'entiers étant les id des colis
            //pour pouvoir utiliser la fonction d'OrderedCrossover (OX).
            List<Integer> l1 = new ArrayList();
            int rab = -1;
            for(Trolley t : p1){
                for(Box b : t.getBoxes())
                    l1.add(b.getIdBox());
                //Si la tournée n'est pas remplie de colis au max, on ajoute des ids négatifs
                //pour que les tournées non completes soient gardées au croissement
                //et non pas qu'elles soit remplies au max par les colis normallement situés dans la tournée suivante.
                for(int j=1; j<(t.getNbColisMax()-t.getBoxes().size()+1); j++) {
                    l1.add(rab);
                    rab--;
                }
            }

            //De même pour le second genome
            rab = -1;
            List<Integer> l2 = new ArrayList();
            for(Trolley t : p2){
                for(Box b : t.getBoxes())
                    l2.add(b.getIdBox());
                for(int j=1; j<(t.getNbColisMax()-t.getBoxes().size()+1); j++) {
                    l2.add(rab);
                    rab--;
                }
            }

            Map<Integer, ArrayList<Integer>> cs = OX.crossover(l1, l2);
           
            ArrayList<Integer> nl1  = cs.get(0);
            ArrayList<Integer> nl2  = cs.get(1);
            
            //Une fois les 2 mélanges récupérés, il faut les reconvertir en des listes de colis
            int id1 = 1, id2 = 1;
            ArrayList<Trolley> c1 = new ArrayList(); //On crée donc 2 listes de colis
            ArrayList<Trolley> c2 = new ArrayList();
            Trolley trolley1 = new Trolley(id1, instance.getNbBoxesTrolley(), instance); //Et 2 premiers colis
            Trolley trolley2 = new Trolley(id2, instance.getNbBoxesTrolley(), instance);
            
            int place1 = 0, place2 = 0;
            for(int j=0; j<nl1.size(); j++){ //Pour chaque élement de la liste d'entiers
                if(place1 >= instance.getNbBoxesTrolley()) { //Si il n'y a plus de place dans la tournée
                    c1.add(trolley1); //On ajoute la tournée à la liste des tournées
                    id1++; //On incrémente l'id de la prochaine tournée
                    trolley1 = new Trolley(id1, instance.getNbBoxesTrolley(), instance); //On remplace la tournée par une nouvelle
                    place1 = 0; //On remet la place à 0
                }
                if(nl1.get(j)-1 >= 0) //Si l'entier contenu est positif (c'est une vrai colis, pas un espace tampon)
                    trolley1.addBox(boxes.get(nl1.get(j)-1)); //On l'ajoute à la tournée
                place1++; //On incrémente la place dans tout les cas
                
                //De même pour la 2ième liste
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
            //On ajoute la derniere tournées à la liste de tournées, pour les 2
            c1.add(trolley1);
            c2.add(trolley2);
            
            //On ajoute finalement les 2 mélanges à la liste de populations
            popList.add(c1);
            popList.add(c2);
        }
    }
    
    /**
     * Permet de réaliser des mutations aléatoires.
     */
    public static void mutation(){
        int tot = boxes.size();
        for(int i=2; i<40; i++){ //Sur les genomes 2 à 10 (tous sauf les 2 meilleurs que l'on garde tel quels)
            for(int j=0; j<20; j++){ //On réalise 1 déplacement aléatoires entre deux boites
                int p1 = new Random().nextInt(tot - 1) + 1;
                int p2 = new Random().nextInt(tot - 1) + 1;
                while(p1 == p2) //Si p1 et p2 sont les mêmes, on retire p2 jusqu'a ce qu'ils ne le soient plus
                    p2 = new Random().nextInt(tot - 1) + 1;
                swapBoxes(popList.get(i), p1, p2);
            }
            
            for(int j=0; j<30; j++){ //On réalise 2 tentatives de déplacement d'une boite aléatoire vers une autre tournée
                int val = new Random().nextInt(popList.get(i).size()); //On sélectionne une tournée au hasard
                if(popList.get(i).get(val).getBoxes().size() == popList.get(i).get(val).getNbColisMax()){ //Si elle est complete
                    int rand  = new Random().nextInt(popList.get(i).get(val).getBoxes().size()); //On sélectionne un colis de cette tournée au hasard
                    int p1 = popList.get(i).get(val).getBoxes().get(rand).getIdBox(); //On récupere l'id du colis séléctionné
                    int p2 = new Random().nextInt(tot) + 1;
                    while(p1 == p2)
                        p2 = new Random().nextInt(tot) + 1;
                    moveBox(popList.get(i), p1, p2);
                }
            }
        }
    }
    
    //FONCTIONS SWAPBOXES ET MOVEBOX (POUR GENERATION ET MUTATION) 
    
    /**
     * Permet d'intervertir deux colis au sein d'une solution.
     * @param sol Liste de tournées (solution)
     * @param idBox1 id du premier colis
     * @param idBox2 id du second colis
     * @return Liste de tournées avec les deux colis intervertis
     */
    public static ArrayList<Trolley> swapBoxes(ArrayList<Trolley> sol, int idBox1, int idBox2){
        Trolley t1 = null;
        Box b1 = null;  
        Trolley t2 = null;
        Box b2 = null;
        
        //On parcourt les colis de toutes les tournées, pour récuperer le colis dont l'id est idBox1 
        //et la tournée qui le comprend, et de même pour le colis dont l'id est idBox2
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
        
        //On récupère les positions actuelles des deux colis
        int i1 = t1.getBoxes().indexOf(b1); 
        int i2 = t2.getBoxes().indexOf(b2);
        //On supprime les deux colis de leurs tournées respectives
        t1.getBoxes().remove(b1);
        t2.getBoxes().remove(b2);

        if(i1 > t1.getBoxes().size()) //Si la position i1 est supérieure à la taille de la liste du fait de la suppresion, on la diminue d'un
            i1--;
        //On ajoute à chaque tournée l'autre colis, a la position du précédent.
        t1.getBoxes().add(i1, b2);
        t2.getBoxes().add(i2, b1);
        
        return sol;
    }
    
    /**
     * Permet de déplacer un colis à la position d'un autre colis. 
     * Sans déplacer ce second colis !
     * Si jamais la tournée vers lequel on souhaite déplacer le colis est complète, alors on ne déplace pas le colis
     * @param sol Liste de tournées (solution)
     * @param idBox1 id du colis à déplacer
     * @param idBox2 id du colis dont on va déplacer le 1er colis à coté
     * @return Liste de tournées avec le colis déplacé (si on a pu le faire)
     */
    public static ArrayList<Trolley> moveBox(ArrayList<Trolley> sol, int idBox1, int idBox2){
        Trolley t1 = null;
        Box b1 = null;  
        Trolley t2 = null;
        Box b2 = null;
        
        //On parcourt les colis de toutes les tournées, pour récuperer le colis dont l'id est idBox1 
        //et la tournée qui le comprend, et de même pour le colis dont l'id est idBox2
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

        if(t2.getBoxes().size() == t2.getNbColisMax()) //Si jamais la tournée de destination est complète
            return sol; //On retourne la solution donnée, sans modifications

        int pos = t2.getBoxes().indexOf(b2)+1; //On récupère la position du colis 2, que l'on incrémente de 1
        
        t1.getBoxes().remove(b1); //On supprime le colis 1 de sa tournée

        t2.getBoxes().add(pos, b1); //Et on l'ajoute à l'autre tournée, à la position calculée précédement
        
        return sol;
    }
    
    //FONCTIONS D'AFFICHAGE

    /**
     * Permet d'afficher proprement la liste de population et l'étape actuelle.
     * Est affiché uniquement si le niveau de LOG_LEVEL est au moins de 2
     * @param etape String étape en cours (ex: Séléction)
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
    
    /**
     * Permet d'afficher proprement les résultats de la population et l'étape actuelle.
     * Est affiché uniquement si le niveau de LOG_LEVEL est au moins de 2
     * @param etape String étape en cours (ex: Evaluation)
     */
    public static void printResults(String etape){
        if(LOG_LEVEL < 2) return;
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
        System.out.printf(etape + " : ");
        for (Map.Entry<Integer, Integer> e : results.entrySet()){
            System.out.printf(e.getKey()+1 + "=" + e.getValue() + ", ");
        }
        System.out.println("");
    }
    
    /**
     * Permet d'afficher le meilleur genome actuel.
     * Est affiché uniquement si le niveau de LOG_LEVEL est au moins de 1
     */
    public static void printBest(){
        if(LOG_LEVEL < 1) return;
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
        System.out.println("Meilleur actuel : ");
        int r = Distances.calcDistance(tempBest, dep, arr);
        String rs = Distances.formatDistance(r);
        System.out.println(r + " (" + rs + ") : " + tempBest);
    }
    
    /**
     * Permet d'afficher proprement le numéro de cycle en cours.
     * Est affiché uniquement si le niveau de LOG_LEVEL est au moins de 1
     * @param i int Numéro du cycle actuel
     * @param t int Nombre total de cycles à faire
     */
    public static void printCycle(int i, int t){
        if(LOG_LEVEL < 1) return;
        System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
        System.out.println("                     Cycle " + i + "/" + t);
    }
    
    //FONCTION DE TEST
    
    /**
     * Permet de tester GATourneeV2.
     * En executant l'algorithme sur une instance, et en la passant au checker.
     * @param args 
     */
    public static void main(String[] args) {
        /*Reader*/
        String fileName = "instance_0606_136178_Z1.txt";
        Instance inst = Reader.read(fileName, false); 
        inst = Recherche.run(inst);
        
        ArrayList<Trolley> tournees = GATourneeV2.run(inst);
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