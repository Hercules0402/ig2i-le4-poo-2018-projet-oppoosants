package test;

import util.Reader;

/**
 * Classe permettant de tester la classe Reader.
 */
public class TestReader {
    public static void main(String[] args) throws Exception {
        Reader r = new Reader("instance_simple.txt");
        System.out.println(r);
    }
}