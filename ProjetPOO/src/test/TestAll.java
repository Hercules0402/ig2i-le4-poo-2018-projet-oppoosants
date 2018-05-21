package test;

import java.util.List;
import metier.Trolley;
import util.Reader;
import util.Recherche;
import util.Writer;

/**
 * Classe permettant de créer une solution 1.
 * Lit le fichier d'instances, génère une solution, et l'écrit dans un fichier texte.
 */
public class TestAll {
    
    public static void main(String[] args) throws Exception {
        String fileName = "instance_buged.txt";
        
        /*Reader*/
        Reader r = new Reader(fileName); 
        //System.out.println(r);
        /*for(Order o : r.getOrders()){
            for(ProdQty pq : o.getProdQtys()){
                System.out.println(pq.getProduct() + " : " + pq.getQuantity());
            }
        }*/
        
        /*Recherche*/
        Recherche sol = new Recherche(r.getOrders(), r.getProducts(), r.getNbBoxesTrolley(),r.getCapaBox().get(0), r.getCapaBox().get(1));
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
        
        /*Writer*/
        Writer w = new Writer(fileName, trolleys);
        
        /*Checker*/
        String[] name = {""};
        name[0] = fileName.substring(0, fileName.lastIndexOf("."));
        System.out.println("\n\nChecker de l'instance : "+ fileName);
        checker.Checker.main(name);
    }
}