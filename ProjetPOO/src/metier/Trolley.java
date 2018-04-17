package metier;

import java.util.HashSet;
import java.util.Set;

/**
 * Définition de la classe Chariot.
 */
public class Trolley {
    
    // Attributs
    private Integer id;
    private static Integer nbColisMax;
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
    
    public Trolley(Integer id, Integer nbColisMax, Set<Parcel> parcels) {
        this();
        this.id = id;
        this.nbColisMax = nbColisMax;
        this.parcels = parcels;
    }
    
    public Trolley(Trolley trolley) {
        this.id = trolley.getId();
        this.parcels = trolley.getParcels();
        this.nbColisMax = trolley.getNbColisMax();
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
    
    public boolean addParcel(Parcel p) {
        if(this.nbColisMax < parcels.size() + 1) return false;
        parcels.add(p);
        return true;
    }
    
    
    // Méthodes

    @Override
    public String toString() {
        return "Trolley{" + "id=" + id + ", parcels=" + parcels + '}';
    }
    
}