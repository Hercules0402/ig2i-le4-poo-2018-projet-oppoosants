package metier;

import java.io.Serializable;
import static java.lang.Math.pow;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Classe définissant une location.
 */
@Entity
public class Location implements Serializable {
    private static final long serialVersionUID = 1L;
    
    //Attributs
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column
    private Integer abscisse;
    
    @Column
    private Integer ordonnee;
    
    @Column
    private String name;
    
    //Constructeurs

    public Location() {
        this.abscisse = 0;
        this.ordonnee = 0;
        this.name = "Nouvelle Location";
    }

    public Location(Integer id, Integer abscisse, Integer ordonnee, String name) {
        this();
        this.id = id;
        this.abscisse = abscisse;
        this.ordonnee = ordonnee;
        this.name = name;
    }

    //Accesseurs
    public Integer getId() {
        return id;
    }
    
    public double getAbscisse() {
        return abscisse;
    }

    public void setAbscisse(Integer abscisse) {
        this.abscisse = abscisse;
    }

    public double getOrdonnee() {
        return ordonnee;
    }

    public void setOrdonnee(Integer ordonnee) {
        this.ordonnee = ordonnee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    // Méthodes
    
    //Calcul de la distance à vol d'oiseau entre 2 locations.
    //Permet d'avoir un ordre d'idée de la distance à parcourir.
    public double getDistanceTo(Location loc){
        return Math.sqrt(
                  pow((this.abscisse - loc.getAbscisse()), 2) 
                + pow((this.ordonnee - loc.getOrdonnee()), 2)
        );
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.abscisse);
        hash = 67 * hash + Objects.hashCode(this.ordonnee);
        hash = 67 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Location other = (Location) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.abscisse, other.abscisse)) {
            return false;
        }
        if (!Objects.equals(this.ordonnee, other.ordonnee)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "metier.Location[ identifiant=" + this.id + ", abscisse=" + this.abscisse
                + ", ordonnee=" + this.ordonnee + ", nom=" + this.name + " ]";
    }  
}