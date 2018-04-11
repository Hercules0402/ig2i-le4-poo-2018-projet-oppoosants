package metier;

/**
 * Définition de la classe Chariot.
 * @author Sébastien
 */
public class Trolley {
    
    // Attributs
    private int id;
    private static int nbColisMax;
    
    // Constructeurs

    public Trolley() {
        this.nbColisMax = 0;
    }

    public Trolley(int nbColisMax) {
        this();
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
    
    // Méthodes
    
    @Override
    public String toString() {
        return "metier.Trolley[ identifiant=" + this.id + ", nombre de colis max=" + this.nbColisMax + " ]";
    }
}
