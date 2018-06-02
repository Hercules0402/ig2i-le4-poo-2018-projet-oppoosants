package test;

import util.Reader;
import algo.Recherche;
import metier.Instance;

public class TestRecherche {
    public static void main(String[] args) throws Exception {    
        Instance inst = Reader.read("instance_200000.txt", false); 
        inst = Recherche.run(inst);
        System.out.println(inst.getTrolleys());
    }
}