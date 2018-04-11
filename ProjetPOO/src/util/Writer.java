package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    public Writer(String filename, List<Trolley> trolleys) throws IOException {
        filename = filename.substring(0, filename.lastIndexOf(".") - 1) + "_sol"
                + filename.substring(filename.lastIndexOf("."));
        this.instanceFile = new File(filename);
        System.out.println("Fichier trouvé.");
        this.nbTrolleys = trolleys.size();
        this.trolleys = trolleys;
        if(this.instanceFile.exists()) {
            this.instanceFile.delete();
        }
        writeSolutions();
    }
    
    public void writeSolutions() throws IOException  {        
       BufferedWriter writer = new BufferedWriter(new FileWriter(instanceFile));             
       if (writer != null) {
           writer.write("//NbTournees\n" + nbTrolleys);  
           for (Trolley t : trolleys){
                writer.write("\n//IdTournes NbColis\n" + t.getId() + " " + Trolley.getNbColisMax() + "\n");
                writer.write("//IdColis IdCommandeInColis NbProducts IdProd1 QtyProd1 IdProd2 QtyProd2 ...");
                for (Parcel p : t.getParcels()) {
                    String s = "\n" + p.getId() + " " + p.getOrder().getId()
                                + " " + p.getProducts().size();
                    
                    Set cles = p.getProducts().keySet();
                    Iterator it = cles.iterator();
                    while (it.hasNext()){
                        Product cle = (Product) it.next(); 
                        Integer valeur = (Integer) p.getProducts().get(cle);
                        s += " " + cle.getId() + " " + valeur;
                   }
                   writer.write(s);
               }
           }
           /*for (int i = 0; i < nbTrolleys; i++) {
               
               
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
               
           }*/
           writer.close();
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