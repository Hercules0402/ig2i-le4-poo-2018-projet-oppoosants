package metier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Définition de la classe Chariot.
 */
public class Trolley {
    
    // Attributs
    private Integer id;
    private static Integer nbColisMax;
    private List<Box> boxes;
    
    // Constructeurs

    public Trolley() {
        this.nbColisMax = 0;
        this.boxes = new ArrayList<>();
    }

    public Trolley(int nbColisMax) {
        this();
        this.nbColisMax = nbColisMax;
    }
    
    public Trolley(Integer id, Integer nbColisMax, List<Box> boxes) {
        this();
        this.id = id;
        this.nbColisMax = nbColisMax;
        this.boxes = boxes;
    }
    
    public Trolley(Integer id, Integer nbColisMax) {
        this();
        this.id = id;
        this.nbColisMax = nbColisMax;
    }
    
    // Accesseurs

    public int getId() {
        return id;
    }

    public static int getNbColisMax() {
        return nbColisMax;
    }

    public static void setNbColisMax(int nbColisMax) {
        Trolley.nbColisMax = nbColisMax;
    }

    public List<Box> getBoxes() {
        return boxes;
    }
    
    public boolean addBox(Box p) {   
        if(this.nbColisMax < boxes.size() + 1) return false;
        boxes.add(p);
        return true;
    }
    
    
    // Méthodes

    @Override
    public String toString() {
        return "\nTrolley{" + "id=" + id + ", boxes=" + boxes + '}';
    }
    
}