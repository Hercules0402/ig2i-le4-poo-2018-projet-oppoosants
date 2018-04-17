package test;

import java.util.ArrayList;
import metier.Trolley;
import util.Reader;
import util.Recherche;

/**
 * Classe permettant de tester la classe Recherche.
 * Lit le fichier d'instances, et génère une solution simple de parcours de la zone de picking.
 */
public class TestRecherche {
    public static void main(String[] args) throws Exception {
        
        Reader r = new Reader("instance_simple.txt");
        Recherche sol = new Recherche( r.getOrders(), r.getProducts(), r.getNbBoxesTrolley(), r.getCapaBox().get(0), r.getCapaBox().get(1));
        ArrayList<Trolley> solutions = new ArrayList();
        
        solutions = sol.lookup();
        System.out.println(solutions);
    }
}
