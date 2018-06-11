package test;

import algo.ClarkeAndWright;
import algo.Recherche;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import metier.Instance;
import metier.Trolley;
import util.CopyPaste;
import util.Distances;
import util.Reader;
import util.Writer;

public class TestAllCwa {

    public static void main(String[] args) throws Exception {
        String base = "../instances/";//10
        String stockage = "./testCWA/";

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
                        
                        for(Trolley t : inst.getTrolleys()){
                            inst.getBoxes().addAll(t.getBoxes());
                        }

                        ClarkeAndWright cwa = new ClarkeAndWright(inst);
                        inst = cwa.run();
                        
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
