package test;

import metier.Instance;
import util.Reader;

/**
 * Classe permettant de tester la classe Reader. 
 */
public class TestReader {
    public static void main(String[] args) throws Exception {
        Instance inst = Reader.read("instance_simple.txt", false); 
        System.out.println(inst);
    }
}