package test;

import java.util.ArrayList;
import metier.Trolley;
import util.Reader;
import algo.Recherche;
import metier.Instance;

public class TestRecherche {
    public static void main(String[] args) throws Exception {    
        Instance inst = Reader.read("instance_200000.txt", false); 
        Recherche sol = new Recherche(inst.getOrders(), inst.getProducts(), inst.getNbBoxesTrolley(),inst.getCapaBox().get(0), inst.getCapaBox().get(1),inst);
        ArrayList<Trolley> solutions = sol.lookup();
        System.out.println(solutions);
        System.out.println("Coût de la solution (à vol d'oiseau): " + sol.getCout());
    }
}