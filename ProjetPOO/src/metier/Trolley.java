package metier;

import java.util.HashSet;
import java.util.Set;

/**
 * Définition de la classe Chariot.
 * @author Sébastien
 */
public class Trolley {
    
    // Attributs
    private int id;
    private static int nbColisMax;
    private Set<Parcel> parcels;
    
    // Constructeurs

    public Trolley() {
        this.nbColisMax = 0;
        this.parcels = new HashSet<>();
    }

    public Trolley(int nbColisMax) {
        this();
        this.nbColisMax = nbColisMax;
    }
    
    public Trolley(int id, int nbColisMax, Set<Parcel> parcels) {
        this();
        this.id = id;
        this.nbColisMax = nbColisMax;
        this.parcels = parcels;
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

    public Set<Parcel> getParcels() {
        return parcels;
    }   
    
    // Méthodes
    
    @Override
    public String toString() {
        return "metier.Trolley[ identifiant=" + this.id + ", nombre de colis max=" + this.nbColisMax + " ]";
    }
}
