package util;

import dao.DaoFactory;
import dao.PersistenceType;
import dao.TrolleyDao;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import metier.Box;
import metier.Instance;
import metier.ProdQty;
import metier.Trolley;

/**
 * Permet d'écrire des fichiers instances solutions.
 */
public class Writer {
    private static File instanceFile;
    private static Instance inst;

    public static void save(String filename, Instance instance, boolean save){
        inst = instance;
        Long time = System.currentTimeMillis();
        if (filename == null) {
            return;
        }
        filename = filename.substring(0, filename.lastIndexOf(".")) + "_sol"
                + filename.substring(filename.lastIndexOf("."));
        instanceFile = new File(filename);
        try {
            writeSolutions();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Fichier créé.");
        System.out.println("WRITER EXECUTION TIME: " + (System.currentTimeMillis() - time) + "ms");

        if(save){
            time = System.currentTimeMillis();
            saveAll();
            System.out.println("SAVE DATA EXECUTION TIME: " + (System.currentTimeMillis() - time) + "ms");
        }
    }

    public static void saveAll(){
        DaoFactory fabrique = DaoFactory.getDaoFactory(PersistenceType.JPA);
        TrolleyDao trolleyManager = fabrique.getTrolleyDao();
        for(Trolley t: inst.getTrolleys()){
            trolleyManager.create(t);
        }
    }

    /**
     * Retourne sous forme de chaine de caractère la soluttion à écrire.
     * @return String
     */
    private static String getContentSolution(List<Trolley> trolleys) {
        String solution = "//NbTournees\n" + trolleys.size();
        for (Trolley t : trolleys){
            solution += "\n//IdTournes NbColis\n" + t.getIdTrolley()+ " "
                    + t.getBoxes().size() + "\n//IdColis IdCommandeInColis"
                    + " NbProducts IdProd1 QtyProd1 IdProd2 QtyProd2 ...";
            for (Box b : t.getBoxes()) {
                /*
                Attention le nombre de produits à récupérer pour chaque colis
                est le nombre produits différents dans le colis.
                Or comme notre ensemble de produits dans un colis est une map
                avec comme clé le produit  et que le produit est mis à jour 
                s'il existe déjà, on peut utiliser la méthode .getProducts().size()
                pour récupérer le nombre de produits différents dans un colis.
                */
                solution += "\n" + b.getIdBox()+ " " + b.getOrder().getIdOrder()
                                + " " + b.getProdQtys().size();
                    
                for(ProdQty prodQty : b.getProdQtys()) {
                    solution += " " + prodQty.getProduct().getIdProduct()+ " " + prodQty.getQuantity();
                }
            }
        }        
        return solution;
    }

    /**
     * Permet d'écrire la solution dans le fichier.
     * @throws java.lang.Exception
     */
    private static void writeSolutions() throws Exception {
        /*
        Création d'un writer sur l'instance de type File : instanceFile pour
        écrire les données dans le fichier de solution.
        */
        PrintWriter pw = new PrintWriter(new FileWriter(instanceFile));
        if (pw != null) {
            // On écrit le contenu dans le fichier
            pw.print(getContentSolution(inst.getTrolleys()));
            pw.close();
        }
        else {
            throw new Exception();
        }
    }
}