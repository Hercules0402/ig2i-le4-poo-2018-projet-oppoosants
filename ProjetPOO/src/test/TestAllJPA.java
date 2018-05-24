package test;

import java.util.List;
import metier.Trolley;
import util.Reader;
import util.Recherche;
import util.Writer;

/**
 * Classe permettant de tester la classe Reader.
 */
public class TestAllJPA {
    public static void main(String[] args) throws Exception {
        String fileName = "instance_200000.txt";
        
        /*Reader*/
        Reader r = new Reader(fileName, false);
        //System.out.println(r);

        /*Recherche*/
        Recherche sol = new Recherche(r.getOrders(), r.getProducts(), r.getNbBoxesTrolley(),r.getCapaBox().get(0), r.getCapaBox().get(1), r.getInstance());
        List<Trolley> trolleys = sol.lookup();        
       
        /*Writer*/
        Writer w = new Writer(fileName, trolleys, false);
    }
}