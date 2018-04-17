package util;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import metier.Parcel;
import metier.Product;
import metier.Trolley;

/**
 * Permet d'écrire des fichiers instances solutions.
 */
public class Writer {
    private File instanceFile;
    private Integer nbTrolleys;
    private List<Trolley> trolleys;

    /**
     * Constructeur par données.
     * @param filename TODO
     * @param trolleys TODO
     * @throws java.lang.Exception 
     */
    public Writer(String filename, List<Trolley> trolleys) throws Exception {
        Long time = System.currentTimeMillis();
        if (filename == null) {
            System.err.println("Une erreur a été rencontrée : Aucun nom de fichier fourni...");
            throw new Exception();
        }
        filename = filename.substring(0, filename.lastIndexOf(".")) + "_sol"
                + filename.substring(filename.lastIndexOf("."));
        this.instanceFile = new File(filename);
        this.nbTrolleys = trolleys.size();
        this.trolleys = trolleys;
        writeSolutions();        
        System.out.println("Fichier créé.");        
        System.out.println("WRITER EXECUTION TIME: " + (System.currentTimeMillis() - time) + "ms");
    }
    
    /**
     * Retourne sous forme de chaine de caractère la soluttion à écrire.
     * @return String
     */
    private String getContentSolution() {
        String solution = "//NbTournees\n" + nbTrolleys;
        for (Trolley t : trolleys){
            solution += "\n//IdTournes NbColis\n" + t.getId() + " " 
                    + Trolley.getNbColisMax() + "\n//IdColis IdCommandeInColis"
                    + " NbProducts IdProd1 QtyProd1 IdProd2 QtyProd2 ...";
            for (Parcel p : t.getParcels()) {
                solution += "\n" + p.getId() + " " + p.getOrder().getId()
                                + " " + p.getProducts().size();
                    
                Set keys = p.getProducts().keySet();
                Iterator it = keys.iterator();
                while (it.hasNext()){
                    Product cle = (Product) it.next(); 
                    Integer valeur = (Integer) p.getProducts().get(cle);
                    solution += " " + cle.getId() + " " + valeur;
                }
            }
        }        
        return solution;
    }
    
    /**
     * Permet d'écrire la solution dans le fichier.
     * @throws java.lang.Exception  
     */
    private void writeSolutions() throws Exception  {
        PrintWriter pw = new PrintWriter(new FileWriter(instanceFile));
        if (pw != null) {
            pw.println(this.getContentSolution());
            pw.close();
        }
        else {
            throw new Exception();
        }
    }

    @Override
    public String toString() {
        String retour = "Writer{" 
                + "\tinstanceFile=" + instanceFile + ",\n"
                + "\tnbTournees=" + nbTrolleys + ",\n"
                + "\ttournees=" + trolleys + ",\n"
                + '}';
        return retour;
    }
}