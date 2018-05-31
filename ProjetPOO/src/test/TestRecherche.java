package test;

import java.util.ArrayList;
import metier.Trolley;
import util.Reader;
import algo.Recherche;
import metier.Instance;

public class TestRecherche {
    public static void main(String[] args) throws Exception {    
        Instance inst = Reader.read("instance_200000.txt", false); 
        Recherche sol = new Recherche(inst.getOrders(), inst.getProducts(), inst.getNbBoxesTrolley(),inst.getWeightMax_box(), inst.getVolumeMax_box(),inst);
        inst = sol.lookup();
        System.out.println(inst.getTrolleys());
        System.out.println("Coût de la solution (à vol d'oiseau): " + sol.getCout());
    }
}