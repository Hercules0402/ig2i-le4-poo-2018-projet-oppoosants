package test;

import java.io.File;
import util.Reader;
import algo.Recherche;
import metier.Instance;
import util.CopyPaste;
import util.Distances;
import util.Writer;

/**
 * Classe permettant de passer au checker chaque instance.
 */
public class TestInstance {  

    public static void main(String[] args) throws Exception {
        String base = "../instances/";
        String stockage = "./testInstance/";

        File baseDossier = new File(base);
        File stockageDossier = new File(stockage);
        Boolean copie;
        if (baseDossier.isDirectory()) {
            if (stockageDossier.isDirectory()) {
                File[] instances = baseDossier.listFiles();
                for(File instance : instances) {
                    copie = CopyPaste.copyPaste(instance.toPath(), new File(stockage + instance.getName()).toPath());
                    if (copie) {
                        Instance inst = Reader.read(stockage + instance.getName(), false);
                        inst = Recherche.run(inst);
                        int distance = Distances.calcDistance(inst.getTrolleys(), inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
                        System.out.println(Distances.formatDistance(distance));
                        Writer.save(stockage + instance.getName(), inst, false);
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
