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
        String fileName = "instance_200000.txt";
        
        /*Reader*/
        Reader r = new Reader(fileName); 
        System.out.println(r);
        
        /*Recherche*/
        Recherche sol = new Recherche(r.getOrders(), r.getProducts(), r.getNbBoxesTrolley(),r.getCapaBox().get(0), r.getCapaBox().get(1));
        List<Trolley> trolleys = sol.lookup();
        
        /*Writer*/
        Writer w = new Writer(fileName, trolleys);
    }
}