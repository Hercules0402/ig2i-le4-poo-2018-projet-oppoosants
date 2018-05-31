package test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import metier.Trolley;
import util.Reader;
import algo.Recherche;
import java.util.List;
import metier.Instance;
import util.Distances;
import util.Writer;

public class TestInstance {
    public static boolean copier(Path source, Path destination) {
        try {
            Files.copy(source, destination,StandardCopyOption.REPLACE_EXISTING);
            // Il est également possible de spécifier des options de copie. 
            // Ici : écrase le fichier destination s'il existe et copie les attributs de la source sur la destination.
           //Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        String base = "../instances/";//10
        String stockage = "./testInstance/";

        File baseDossier = new File(base);
        File stockageDossier = new File(stockage);
        Boolean copie;
        if (baseDossier.isDirectory()) {
            if (stockageDossier.isDirectory()) {
                File[] instances = baseDossier.listFiles();
                for(File instance : instances) {
                    copie = copier(instance.toPath(), new File(stockage + instance.getName()).toPath());
                    if (copie) {
                        Instance inst = Reader.read(stockage + instance.getName(), false);
                        Recherche sol = new Recherche(inst.getOrders(), inst.getProducts(), inst.getNbBoxesTrolley(),inst.getWeightMax_box(), inst.getVolumeMax_box(),inst);
                        List<Trolley> trolleys = sol.lookup();
                        int distance = Distances.calcDistance(trolleys, inst.getGraph().getDepartingDepot(), inst.getGraph().getArrivalDepot());
                        System.out.println(Distances.formatDistance(distance));
                        Writer w = new Writer(stockage + instance.getName(), trolleys, false);
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
