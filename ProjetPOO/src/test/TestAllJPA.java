package test;

import java.util.List;
import metier.Trolley;
import util.Reader;
import algo.Recherche;
import metier.Instance;
import util.Writer;

/**
 * Classe permettant de tester la classe Reader.
 */
public class TestAllJPA {
    public static void main(String[] args) throws Exception {
        String fileName = "instance_200000.txt";

        /*Reader*/
        Instance inst = Reader.read(fileName, false); 
        //System.out.println(r);

        /*Recherche*/
        Recherche sol = new Recherche(inst.getOrders(), inst.getProducts(), inst.getNbBoxesTrolley(),inst.getWeightMax_box(), inst.getVolumeMax_box(),inst);
        inst = sol.lookup();

        /*Writer*/
        Writer w = new Writer(fileName, inst.getTrolleys(), false);
    }
}