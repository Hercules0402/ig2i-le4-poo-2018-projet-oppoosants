package test;

import util.Reader;
import algo.Recherche;
import metier.Instance;

/**
 * Classe permettant de tester la classe Recherche.
 */
public class TestRecherche {
    public static void main(String[] args) throws Exception {    
        Instance inst = Reader.read("instance_200000.txt", false); 
        inst = Recherche.run(inst);
        System.out.println(inst.getTrolleys());
    }
}