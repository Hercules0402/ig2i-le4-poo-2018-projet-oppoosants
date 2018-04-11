package test;

import util.Reader;

public class TestReader {
    public static void main(String[] args) throws Exception {
        Reader r = new Reader("instance_simple.txt");
        System.out.println(r);
    }
}