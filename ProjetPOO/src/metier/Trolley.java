package metier;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe définissant un chariot.
 */
public class Trolley {
    
    private Integer id;
    private static Integer nbColisMax;
    private List<Box> boxes;

    public Trolley() {
        Trolley.nbColisMax = 0;
        this.boxes = new ArrayList<>();
    }

    public Trolley(int nbColisMax) {
        this();
        Trolley.nbColisMax = nbColisMax;
    }
    
    public Trolley(Integer id, Integer nbColisMax, List<Box> boxes) {
        this();
        this.id = id;
        Trolley.nbColisMax = nbColisMax;
        this.boxes = boxes;
    }
    
    public Trolley(Integer id, Integer nbColisMax) {
        this();
        this.id = id;
        Trolley.nbColisMax = nbColisMax;
    }

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
    
    /**
     * Ajoute une box à un chariot.
     * @param b la box en question.
     * @return true si l'ajout est fait, false sinon.
     */
    public boolean addBox(Box b) {   
        if(Trolley.nbColisMax < boxes.size() + 1) return false;
        boxes.add(b);
        return true;
    }

    @Override
    public String toString() {
        return "\nTrolley{" + "id=" + id + ", boxes=" + boxes + '}';
    }
}