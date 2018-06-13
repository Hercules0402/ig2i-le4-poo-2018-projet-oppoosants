package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Classe permettant de réaliser un copier-coller de fichier.
 */
public class CopyPaste {
    /**
     * Permet de copier un fichier dans un dossier
     * @param source
     * @param destination
     * @return boolean
     */
    public static boolean copyPaste(Path source, Path destination) {
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
}
