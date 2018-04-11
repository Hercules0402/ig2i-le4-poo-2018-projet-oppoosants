package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Permet d'écrire des fichiers instances solutions.
 */
public class Writer {
    private File instanceFile;
    private Integer nbTournees;
    private List<List> tournees;

    public Writer(String filename, List<List> tournees) throws IOException {
        filename = filename.substring(0, filename.lastIndexOf(".") - 1) + "_sol"
                + filename.substring(filename.lastIndexOf("."));
        this.instanceFile = new File(filename);
        System.out.println("Fichier trouvé.");
        this.nbTournees = tournees.size();
        this.tournees = tournees;
        if(this.instanceFile.exists()) {
            this.instanceFile.delete();
        }
        writeSolutions();
    }
    
    public void writeSolutions() throws IOException  {        
       BufferedWriter writer = new BufferedWriter(new FileWriter(instanceFile));             
       if (writer != null) {
           writer.write("//NbTournees\n" + nbTournees);           
           for (int i = 0; i < tournees.size(); i++) {
               String s = tournees.get(i).get(0).toString().replace("[", "");
               s = s.replace("]", "");
               writer.write("\n//IdTournes NbColis\n" + s + "\n");
               writer.write("//IdColis IdCommandeInColis NbProducts IdProd1 QtyProd1 IdProd2 QtyProd2 ...");
               s = tournees.get(i).get(1).toString().replace("[", "");
               s = s.replace("]", "");
               String[] tab = s.split(", ");
               for (int j = 0; j < tab.length; j++){
                   writer.write("\n" + tab[j]);
               }
               
           }
           writer.close();
       }
    }

    @Override
    public String toString() {
        String retour = "Writer{" 
                + "\tinstanceFile=" + instanceFile + ",\n"
                + "\tnbTournees=" + nbTournees + ",\n"
                + "\ttournees=" + tournees + ",\n"
                + '}';
        
        return retour;
    }
    
    
}
