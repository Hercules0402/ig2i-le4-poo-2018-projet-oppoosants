package test;

import java.util.ArrayList;
import java.util.List;
import metier.Trolley;
import util.Reader;
import util.Recherche;
import util.Writer;

public class TestAll {
    public static void main(String[] args) throws Exception {
        String fileName = "instance_simple.txt";
        
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