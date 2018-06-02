package test;

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
        inst = Recherche.run(inst);

        /*Writer*/
        Writer.save(fileName, inst, false);
    }
}