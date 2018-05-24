package util;

import dao.BoxDao;
import dao.DaoFactory;
import dao.PersistenceType;
import dao.ProdQtyDao;
import dao.TrolleyDao;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import metier.Box;
import metier.ProdQty;
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
        
        time = System.currentTimeMillis();
        saveAll();
        System.out.println("SAVE DATA EXECUTION TIME: " + (System.currentTimeMillis() - time) + "ms");
    }
    
    public void saveAll(){
        DaoFactory fabrique = DaoFactory.getDaoFactory(PersistenceType.JPA);
        TrolleyDao trolleyManager = fabrique.getTrolleyDao();
        for(Trolley t: trolleys){
            trolleyManager.create(t);
        }
    }
    
    /**
     * Retourne sous forme de chaine de caractère la soluttion à écrire.
     * @return String
     */
    private String getContentSolution() {
        String solution = "//NbTournees\n" + nbTrolleys;
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
    private void writeSolutions() throws Exception  {
        /*
        Création d'un writer sur l'instance de type File : instanceFile pour
        écrire les données dans le fichier de solution.
        */
        PrintWriter pw = new PrintWriter(new FileWriter(instanceFile));
        if (pw != null) {
            // On écrit le contenu dans le fichier
            pw.print(this.getContentSolution());
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