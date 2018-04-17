/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;
import metier.Trolley;
import util.Reader;
import util.Recherche;

/**
 *
 * @author nicol
 */
public class TestRecherche {
    public static void main(String[] args) throws Exception {    
        //public Recherche(ArrayList<Order> orderList, ArrayList<Product> productList, int nbColisMax, int weightMax_parcel, int volumeMax_parcel)
        Reader r = new Reader("instance_simple.txt");
        Recherche sol = new Recherche(r.getOrders(),r.getProducts(),r.getNbBoxesTrolley(),(int) r.getCapaBox().get(0), (int) r.getCapaBox().get(1));
        ArrayList<Trolley> solutions = new ArrayList();
        solutions = sol.lookup();
        System.out.println(solutions);
    }
}
