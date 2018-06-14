package algogen;

import java.io.File;
import util.Reader;
import algo.Recherche;
import metier.Instance;
import util.CopyPaste;
import util.Distances;
import util.Writer;

public class TestAllGAReset {

    /**
     * Permet de tester la fonction GATourneeReset pour toutes les instances les unes après les autres.
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        String base = "../instances/";//10
        String stockage = "./testInstance/";

        File baseDossier = new File(base);
        File stockageDossier = new File(stockage);
        Boolean copie;
        if (baseDossier.isDirectory()) { //Vérifie si "instances" est bien un dossier
            if (stockageDossier.isDirectory()) { //Vérifie si "testInstance" est bien un dossier
                File[] instances = baseDossier.listFiles(); //Récupère la liste des fichiers instances dans le dossier "instances"
                for(File instance : instances) {
                    //Copie des fichiers du dossier "instances" dans le dossier "testInstance"
                    copie = CopyPaste.copyPaste(instance.toPath(), new File(stockage + instance.getName()).toPath());
                    if (copie) { //Si la copie a eu lieu
                        //Reader
                        Instance inst = Reader.read(stockage + instance.getName(), false);
                        //Algorithme naif puis GATourneeReset
                        inst = Recherche.run(inst);
                        inst.setTrolleys(GATourneeReset.run(inst));
                        int distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
                        System.out.println(Distances.formatDistance(distance));
                        //Writer
                        Writer.save(stockage + instance.getName(), inst, false);
                        //Checker
                        String[] name = {""};
                        name[0] = stockage + instance.getName().substring(0, instance.getName().lastIndexOf("."));
                        System.out.println("\n\nChecker de l'instance : "+ instance.getName());
                        checker.Checker.main(name);
                    }
                }
            }
            else {
                System.err.println("Erreur : Aucun dossier de sortie fourni");
            }
        }
        else {
            System.err.println("Erreur : Aucun dossier de base fourni");
        }
    }
}
