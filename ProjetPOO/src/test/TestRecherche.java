package test;

import util.Reader;
import algo.Recherche;
import metier.Instance;

/**
<<<<<<< HEAD
 * Classe permettant de tester la classe Recherche.
=======
 * Classe permettant de tester la classe Recherche (algorithme naif).
>>>>>>> version-3.0
 */
public class TestRecherche {
    public static void main(String[] args) throws Exception {    
        Instance inst = Reader.read("instance_200000.txt", false); 
        inst = Recherche.run(inst);
        System.out.println(inst.getTrolleys());
    }
}