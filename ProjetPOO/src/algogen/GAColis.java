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

/**
 * Classe contenant l'algorithme génétique pour les colis.
 * Permettant d'optimiser les produits d'une commande au sein de colis.
 * @author Lucas
 */
public class GAColis {
    private final static int CB_CYCLES = 2; //Nombre de générations
    private final static int LOG_LEVEL = 2; //Niveau de logs: 0: None, 1: Cycles, 2: All
    
    private static Instance instance;
    private static Order order;
    
    private static ProdQty[] data;
    private static List<ArrayList<Integer>> popList;
    private static Map<Integer, Integer> results;
    
    /**
     * Permet d'executer l'algorithme.
     * @param o Commande contenant les produits à optimiser.
     * @param inst Instance
     * @return Liste de colis optimisés contenants les produits de la commande.
     */
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
    
    /**
     * Permet de générer la population initiale.
     */
    public static void genereration(){
        data = basicsmart(); //On récupére un tableau des ProdQty de la commande triés par location
        popList = new ArrayList<ArrayList<Integer>>(); //On initialise la liste de population
        
        //On ajoute à la liste de population un premier genome dans l'ordre (1, 2, 3.. n)
        ArrayList<Integer> list = new ArrayList<>();
        for(int j=0; j<data.length; j++) list.add(j);
        popList.add(list);
                
        for(int i=2; i<11; i++){ //On génére les 9 genomes suivants
            ArrayList<Integer> nList = new ArrayList<>(list);  
            //On interverti 2par2, puis 3par3, etc
            for(int k=0; k<i; k++)
                for(int j=0; j<nList.size()-(3*i); j=j+3*i)
                    Collections.swap(nList, i+k+j, 2*i+k+j);
            //On ajoute à la liste de population
            popList.add(nList);
        }
    }
    
    /**
     * Permet d'évaluer les genomes de la population actuelle.
     */
    public static void evaluation(){
        results = new HashMap<>(); //On initialise la Map contenant les résultats
        int i = 0;
        for(ArrayList<Integer> genome : popList){ //Pour chaque genome:
            ArrayList<ProdQty> products = new ArrayList<>(); //On crée une liste de produits
            for(Integer nb : genome) //Pour chaque index du genome:
                products.add(data[nb]); //On ajoute à la liste le produit de data correspondant a l'index
            //La fonction split qui décompose notre liste de produits ordonnés en une liste de box, nous retourne le cout
            int r = split(products); 
            results.put(i, r); //On insére les résultats dans la Map
            i++;
        }
    }
    
    /**
     * Permet de sélectionner les deux meilleurs génomes de la population.
     */
    public static void selection(){
        Map.Entry<Integer, Integer> min1 = null;
        Map.Entry<Integer, Integer> min2 = null;

        //On récupère dans les résultat le meilleur genome min1
        for(Map.Entry<Integer, Integer> en : results.entrySet()){
            if (min1 == null || en.getValue().compareTo(min1.getValue()) < 0){
                min1 = en;
            }                   
        }
        //On récupère dans les résultat le second meilleur genome min2
        for(Map.Entry<Integer, Integer> en : results.entrySet()){
            if (en != min1 && (min2 == null || (en.getValue().compareTo(min2.getValue()) < 0))) {
                min2 = en;
            }                  
        }
        
        //On crée une nouvelle liste de population, auquel on ajoute ces 2 meilleurs résultats
        List<ArrayList<Integer>> newPopList = new ArrayList<ArrayList<Integer>>();
        newPopList.add(popList.get(min1.getKey()));
        newPopList.add(popList.get(min2.getKey()));
        //On remplace la liste de pop actuelle, par la nouvelle crée juste au desssus
        popList = new ArrayList<ArrayList<Integer>>(newPopList);
    }
    
    /**
     * Permet d'effectuer des croissements entre les 2 meilleurs genomes.
     */
    public static void crossover(){
        ArrayList<Integer> p1 = popList.get(0); //On récupere le premier genome (meilleur)
        ArrayList<Integer> p2 = popList.get(1); //On récupere le deuxieme genome (2nd meilleur)
        
        //Pour creer 8 nouveaux genomes, on fait appel 4 fois à la fonction d'OrderedCrossover (OX) 
        //qui va retourner 2 mélanges des 2 génomes passés en paramètres, qu'on ajoute à la liste de population
        for(int i=0; i<4; i++) { 
            Map<Integer, ArrayList<Integer>> cs = OX.crossover(p1, p2);
            popList.add(cs.get(0));
            popList.add(cs.get(1));
        }
    }
    
    /**
     * Permet de réaliser des mutations aléatoires.
     */
    public static void mutation(){
        //TODO: Local search as mutation operator
        for(int i=2; i<10; i++){ //Sur les genomes 2 à 10 (tous sauf les 2 meilleurs que l'on garde tel quels)
            ArrayList<Integer> list = popList.get(i); //On recup le genome voulu
            for(int j=0; j<5; j++){ //On lui permute 5 fois deux produits au hasard
                int pos1 = new Random().nextInt(list.size());
                int pos2 = new Random().nextInt(list.size());
                while(pos1 == pos2) //Si pos1 et pos2 sont les mêmes, on retire pos2 jusqu'a ce qu'ils ne le soient plus
                    pos2 = new Random().nextInt(list.size());
                Collections.swap(list, pos1, pos2);
            }
        }
    }
    
    //FONCTIONS BASICSMART (POUR GENERATION), SPLIT (POUR EVALUATION) ET EXTRACT SOLUTION

    /**
     * Permet de récuperer des données ProdQty de la commande, ordonnées par location.
     * @return Tableau de ProdQty 
     */
    public static ProdQty[] basicsmart(){
        List<ProdQty> listPq = order.getProdQtys(); //On récupère la liste des ProdQty de la commande.
        Collections.sort(listPq); //On les trie par location.
        
        //On crée un tableau de ProdQty que l'on remplit des données de la liste précédente.
        ProdQty[] tab = new ProdQty[listPq.size()]; 
        for(int i=0; i<listPq.size(); i++){
            tab[i] = listPq.get(i);
        }
        return tab; //On retourne ce tableau
    }
    
    /**
     * Permet de trouver la meilleure manière de décomposer le genome en colis.
     * Issu du pseudo-code Figure 2 du papier de Christian Prins
     * @param genome Liste de ProdQty
     * @return Coût de la solution
     */
    public static int split(ArrayList<ProdQty> genome){
        int n = genome.size();
        Integer V[] = new Integer[n+1];
        Integer P[] = new Integer[n+1];
        int W = instance.getWeightMaxBox();
        int Vol = instance.getVolumeMaxBox();
        int L = Integer.MAX_VALUE;
        
        Location dep = instance.getGraph().getDepartingDepot();
        Location arr  = instance.getGraph().getArrivalDepot();
        
        V[0] = 0; //On initialise le premier élement du tableau V à 0, et tout le reste à plus l'infini
        for(int i=1; i<n; i++){
            V[i] = Integer.MAX_VALUE;
        }
        for(int i=1; i<n; i++){ //Pour chaque élement du genome
            int loadW = 0, loadV = 0, cost = 0, j = i; //On initialise le poid, le volume et le cout, ainsi que j à i
            while(j<n && loadW<W && loadV<Vol && cost<L) { //Si le volume, le poids et le cout sont plus petit que leur limite et que j est plus petit que n
                //On ajoute le poids du produit multiplié par sa quantité à la charge de poids actuelle.
                loadW += genome.get(i-1).getProduct().getWeight() * genome.get(i-1).getQuantity();
                loadV += genome.get(i-1).getProduct().getVolume() * genome.get(i-1).getQuantity(); //De même pour le volume
                if(i == j){
                    //On ajoute au cout l'aller-retour entre le produit et les dépots.
                    cost += dep.getDistances().get(genome.get(j-1).getProduct().getLoc())
                            + genome.get(j-1).getProduct().getLoc().getDistances().get(arr);
                } else { //TODO: Trier par locations
                    //Si la location du produit n'est pas la même que celle du produit précédent 
                    if(!genome.get(j-2).getProduct().getLoc().equals(genome.get(j-1).getProduct().getLoc())) { 
                        //On retire au cout le retour du produit précédent au dépot d'arrivée
                        cost -= genome.get(j-2).getProduct().getLoc().getDistances().get(arr);
                        //En fonction de l'ordre de la location/location précédente (comme on ne connait les distances que dans un sens)
                        //On ajoute au cout la distance entre ces deux points
                        if(genome.get(j-2).getProduct().getLoc().getDistances().containsKey(genome.get(j-1).getProduct().getLoc()))
                            cost += genome.get(j-2).getProduct().getLoc().getDistances().get(genome.get(j-1).getProduct().getLoc());
                        else
                            cost += genome.get(j-1).getProduct().getLoc().getDistances().get(genome.get(j-2).getProduct().getLoc());
                        //On ajoute au cout le retour du produit actuel au dépot d'arrivée
                        cost += genome.get(j-1).getProduct().getLoc().getDistances().get(arr);
                    }
                }
                if(loadW<=W && loadV<=Vol && cost<=L){ //Si le volume, le poids et le cout ne dépassent pas leur limite
                    if(V[i-1] + cost < V[j]){
                        V[j] = V[i-1]+cost;
                        P[j] = i-1;
                    }
                    j = j+1;
                }
            }
        }
        /*for(int i=1; i<n; i++){
            System.out.println("V:" + V[i] + " P:" + P[i]);
        }*/
        List<Box> boxes = extractSolution(P, genome);
        return V[n-1];
    }
    
    /**
     * Permet d'extraire une solution à partir des données de la fonction split.
     * Issu du pseudo-code Figure 3 du papier de Christian Prins
     * @param P Tableau de labels.
     * @param genome Liste des ProdQty.
     * @return Liste de colis contenant les ProdQty de manière optimisée.
     */
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
        
        ArrayList<Box> notEmptyBoxes = new ArrayList<>(); //On recrée une liste de colis non vides
        for(Box b : boxes) //Pour chaque colis 
            if(!b.getProdQtys().isEmpty()) //Si il n'est pas vide
                notEmptyBoxes.add(b); //On l'ajoute à cette nouvelle liste
        /*for(Box b: notEmptyBoxes){
            for(ProdQty pq: b.getProdQtys())
                System.out.printf(pq.getProduct().getLoc().getIdLocation()+", ");
            System.out.printf(" | ");
        }    
        System.out.println("");*/
        return notEmptyBoxes;
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
        int i = 1;
        for(ArrayList<Integer> list : popList) {
            System.out.printf("Liste produits " + i + ": ");
            for(Integer nb : list)
                System.out.printf(nb+ "("+data[nb].getProduct().getIdProduct() + "x"+ data[nb].getQuantity() + "), ");
            System.out.println("");
            i++;
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
     * Permet de tester GAColis.
     * En executant l'algorithme sur la première commande uniquement d'une instance.
     * @param args 
     */
    public static void main(String[] args) {
        String fileName = "instance_40000.txt";
        Instance inst = Reader.read(fileName, false); 
        GAColis.run(inst.getOrders().get(0), inst);
    }
}