package test;

import util.Reader;
import algo.Recherche;
import metier.Instance;
import util.Writer;

/**
 * Classe permettant de tester la classe Reader, la classe Recherche et la classe
 * Writer avec persistance.
 */
public class TestAllJPA {
    public static void main(String[] args) throws Exception {
        String fileName = "instance_200000.txt";

        /*Reader*/
        Instance inst = Reader.read(fileName, true); 
        //System.out.println(r);

        /*Recherche*/
        inst = Recherche.run(inst);

        /*Writer*/
        Writer.save(fileName, inst, true);
    }
}