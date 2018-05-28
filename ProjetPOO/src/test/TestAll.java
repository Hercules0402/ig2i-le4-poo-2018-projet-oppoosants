package test;

import java.util.List;
import metier.Trolley;
import util.Reader;
import algo.Recherche;
import util.Writer;

/**
 * Classe permettant de créer une solution 1.
 * Lit le fichier d'instances, génère une solution, et l'écrit dans un fichier texte.
 */
public class TestAll {

    public static void main(String[] args) throws Exception {
        String fileName = "instance_40000.txt";

        /*Reader*/
        Reader r = new Reader(fileName, false); 
        //System.out.println(r);
        /*for(Order o : r.getOrders()){
            for(ProdQty pq : o.getProdQtys()){
                System.out.println(pq.getProduct() + " : " + pq.getQuantity());
            }
        }*/

        /*Recherche*/
        Recherche sol = new Recherche(r.getOrders(), r.getProducts(), r.getNbBoxesTrolley(),r.getCapaBox().get(0), r.getCapaBox().get(1),r.getInstance());
        /*for(Order o : sol.getOrderList()){
            for(ProdQty pq : o.getProdQtys()){
                System.out.println(pq.getProduct() + " : " + pq.getQuantity());
            }
        }*/
        List<Trolley> trolleys = sol.lookup();
        /*for(Trolley t : trolleys) {
            for(Box b : t.getBoxes()) {
                for(ProdQty pq : b.getProdQtys()) {
                    System.out.println(pq);
                }
            }
        }*/
        //System.out.println(calcDistance(trolleys, r.getDistances(), r.getDepartingDepot(), r.getArrivalDepot())+"");
        
        /*Writer*/
        Writer w = new Writer(fileName, trolleys, false);

        /*Checker*/
        String[] name = {""};
        name[0] = fileName.substring(0, fileName.lastIndexOf("."));
        System.out.println("\n\nChecker de l'instance : "+ fileName);
        checker.Checker.main(name);
    }
    
   /* public static int calcDistance(List<Trolley> trolleys, List<Arc> distances, Location dep, Location arr){
        int d = 0;
        for (Trolley t : trolleys) {
            List<Location> locations = new ArrayList();
            for (Box b : t.getBoxes()) {
                for (ProdQty pq : b.getProdQtys()) {
                    locations.add(pq.getProduct().getLoc());
                }
            }
            
            d += dep.getDistances().get(locations.get(0)); //Distance entre le depart et la premiere loc
            for (int i=1; i<locations.size(); i++) {
                System.out.println(locations.get(i-1).getDistances().get(locations.get(i)));
              //d += locations.get(i-1).getDistances().get(locations.get(i)); //Distance entre loc n et n+1
            }
            d += locations.get(locations.size() - 1).getDistances().get(arr);; //Distance entre la derniere loc et l'arrivee
        }
        return d;
    }*/
}